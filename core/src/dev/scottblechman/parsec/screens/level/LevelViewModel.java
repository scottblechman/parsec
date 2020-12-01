package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.Button;
import dev.scottblechman.parsec.common.components.Explosion;
import dev.scottblechman.parsec.common.components.StarField;
import dev.scottblechman.parsec.common.components.TypewriterText;
import dev.scottblechman.parsec.data.LevelService;
import dev.scottblechman.parsec.data.PrefsService;
import dev.scottblechman.parsec.listeners.ProjectileListener;
import dev.scottblechman.parsec.models.Barrier;
import dev.scottblechman.parsec.models.Moon;
import dev.scottblechman.parsec.models.Projectile;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.models.Star;
import dev.scottblechman.parsec.state.enums.ScreenState;

import java.util.ArrayList;
import java.util.List;

public class LevelViewModel {

    World world;
    Projectile projectile;
    Star star;
    ArrayList<Moon> moons;
    Barrier barrier;
    ProjectileListener contactListener;
    PrefsService prefsService;
    LevelService levelService;
    Parsec game;
    StarField starField;
    Button nextLevelButton;
    TypewriterText levelMessage;
    TypewriterText completeMessage;
    ArrayList<Explosion> explosions;

    static final float STEP_TIME = 1f/60f;
    float accumulator = 0;
    float timeout = 0; // Upper limit on projectile motion

    // Wait to apply gravity until projectile is in motion
    private boolean projectileInMotion = false;

    private int shotsAttempted = 0;

    private final ArrayList<Body> bodiesToDestroy = new ArrayList<>();

    // Body reset flags
    private boolean resetProjectile = false;
    private boolean resetMoons = false;
    private boolean resetBarrier = false;

    // Indicates whether to show an option to advance to the next level
    private boolean levelFinished = false;

    // Prevents planets on projectile starting orbit from increasing shot count
    private float invulnerability = 0f;

    public LevelViewModel(Parsec game) {
        prefsService = new PrefsService();
        boolean tutorial = prefsService.showTutorial();
        levelService = new LevelService(tutorial);

        world = new World(new Vector2(0, 0), true);
        projectile = new Projectile(world);
        star = new Star(world);
        moons = new ArrayList<>();
        moons.add(new Moon(world, levelService.getLevelRadius(), true));
        for(int i = 0; i < levelService.getMoonRadii().length; i++) {
            moons.add(new Moon(world, levelService.getMoonRadius(i), false));
        }
        if(levelService.createBarrier() && !Constants.Game.DEBUG_MODE) {
            barrier = new Barrier.Builder(world)
                    .position(Constants.Entities.BARRIER_INIT_POS)
                    .width(Constants.Entities.BARRIER_WIDTH)
                    .height(Constants.Entities.BARRIER_HEIGHT)
                    .build();
        }

        contactListener = new ProjectileListener(this);
        world.setContactListener(contactListener);
        this.game = game;
        starField = new StarField();
        nextLevelButton = new Button("NEXT LEVEL", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 2f, Constants.Camera.VIEWPORT_HEIGHT / 3f), game.getFont());
        levelMessage = new TypewriterText(levelService.getMessage(), true);
        completeMessage = new TypewriterText("LEVEL COMPLETE!", false);
        explosions = new ArrayList<>();
    }

    public Vector2 getProjectilePosition() {
        return projectile.getPosition();
    }

    public Vector2 getSunPosition() {
        return star.getPosition();
    }

    public boolean isInMotion() {
        return projectileInMotion;
    }

    public int getShots() {
        return shotsAttempted;
    }

    public int getLevelNumber() {
        return levelService.getLevelNumber() - levelService.tutorialLevels();
    }

    public List<Moon> getMoons() {
        return moons;
    }

    public Barrier getBarrier() {
        return barrier;
    }

    public boolean tutorialMode() {
        return prefsService.showTutorial();
    }

    public String getLevelMessage() {
        return levelMessage.getText().toUpperCase();
    }

    public String getCompleteMessage() {
        return completeMessage.getText();
    }

    public boolean shouldAlwaysAdvance() {
        return levelService.shouldAlwaysAdvance();
    }

    public boolean onTutorialLevel() {
        return levelService.onTutorialLevel();
    }

    public List<Vector3> getStarField() {
        return starField.getPool();
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }

    public float getTimeout() {
        return (float) Math.ceil(timeout);
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public Button getNextLevelButton() {
        return nextLevelButton;
    }

    public boolean isProjectileInvulnerable() {
        return invulnerability > 0f;
    }

    public float getRemainingProjectileInvulnerability() {
        return invulnerability;
    }

    public void dispose() {
        world.dispose();
    }

    protected void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        // Update non-physics graphics
        starField.update();
        levelMessage.update();
        completeMessage.update();
        ArrayList<Explosion> remainingExplosions = new ArrayList<>();
        for(Explosion e : explosions) {
            if(!e.isComplete()) {
                e.update();
                remainingExplosions.add(e);
            }
        }
        explosions = remainingExplosions;

        while (accumulator >= STEP_TIME) {
            // Apply forces before stepping world
            if (projectileInMotion) {
                // Check if we have gone over the projectile timeout
                timeout += accumulator;
                if(timeout >= Constants.Game.PROJECTILE_TIMEOUT) {
                    reset(true);
                } else {
                    applyGravitationalForce(projectile.getBody());
                }
            }

            destroyBodies();
            resetBodies();
            for(Moon moon : moons) {
                moon.update();
            }
            if(invulnerability > 0f) {
                invulnerability -= accumulator;
            } else {
                invulnerability = 0f;
            }
            world.step(STEP_TIME, 6, 2);

            accumulator -= STEP_TIME;
        }
    }

    /**
     * Remove any bodies no longer needed before the next world step.
     */
    private void destroyBodies() {
        for(Body b : bodiesToDestroy) {
            world.destroyBody(b);
        }
        bodiesToDestroy.clear();
    }

    /**
     * Re-creates any destroyed bodies before the next world step.
     */
    private void resetBodies() {
        if(resetProjectile) {
            projectile = new Projectile(world);
            resetProjectile = false;
        }
        if(resetMoons) {
            moons = new ArrayList<>();
            moons.add(new Moon(world, levelService.getLevelRadius(), true));
            for(int i = 0; i < levelService.getMoonRadii().length; i++) {
                moons.add(new Moon(world, levelService.getMoonRadius(i), false));
            }
            resetMoons = false;
        }

        if(resetBarrier && levelService.createBarrier() && !Constants.Game.DEBUG_MODE) {
            barrier = new Barrier.Builder(world)
                    .position(Constants.Entities.BARRIER_INIT_POS)
                    .width(Constants.Entities.BARRIER_WIDTH)
                    .height(Constants.Entities.BARRIER_HEIGHT)
                    .build();
            resetBarrier = false;
        }
    }

    /**
     * Uses the existing mouse drag information to apply an impulse to the projectile body.
     */
    protected void shootProjectile(Vector2 start, Vector2 end) {
        shotsAttempted++;
        projectileInMotion = true;
        float impulseX = start.x - end.x;
        // Assume that start pos y is always >= end pos y
        float impulseY = start.y - end.y;
        projectile.getBody().applyLinearImpulse(Constants.Physics.FORCE_SCALAR * impulseX,
                Constants.Physics.FORCE_SCALAR * impulseY, projectile.getPosition().x, projectile.getPosition().y, true);
    }

    /**
     * Applies a linear force on the projectile to mimic gravitational pull from the sun.
     */
    private void applyGravitationalForce(Body body) {
        float radius = body.getFixtureList().get(0).getShape().getRadius();
        Vector2 vecDistance = body.getPosition().sub(star.getPosition());
        float distance = vecDistance.len();
        vecDistance.scl(-1); // Vector negative
        float magnitude = Math.abs(vecDistance.x) + Math.abs(vecDistance.y);
        vecDistance.scl((1/magnitude) * radius/distance);
        vecDistance.scl(Constants.Physics.FORCE_SCALAR * Constants.Physics.GRAVITY_SCALAR);
        body.applyForceToCenter(vecDistance, true);
    }

    /**
     * Sets up a new shot after collision or timeout.
     * @param increment whether to add a shot to the total count
     */
    public void reset(boolean increment) {
        if(increment) {
            createSatelliteParticles();
            invulnerability = Constants.Entities.PROJECTILE_MAX_INVUL;
        } else {
            shotsAttempted = 0;
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body b : bodies) {
            EntityType type = (EntityType) b.getUserData();

            if (type == EntityType.PROJECTILE || (resetMoons && (type == EntityType.MOON || type == EntityType.TARGET_MOON || type == EntityType.BARRIER))) {
                bodiesToDestroy.add(b);
            }
        }
        resetProjectile = true;
        projectileInMotion = false;
        timeout = 0;
    }

    /**
     * Advances the level after the target moon has been hit.
     */
    public void nextLevel() {
        levelFinished = false;
        if(!levelService.onTutorialLevel()) {
            game.getScoreService().writeScore(levelService.getLevelNumber(), shotsAttempted);
        }
        if(levelService.lastLevel()) {
            game.navigateTo(ScreenState.SCORE);
        } else {
            resetMoons = true;
            resetBarrier = true;
            reset(false);
            levelService.nextLevel();
            if(levelService.onTutorialLevel()) {
                levelMessage = new TypewriterText(levelService.getMessage(), true);
            }
            completeMessage.reset();
        }
        // Hide tutorial if complete
        if(levelService.lastTutorialLevel()) {
            prefsService.markTutorialDone();
        }
    }

    /**
     * Creates a break between level completion and next level start by showing a button to advance.
     */
    public void finishLevel() {
        levelFinished = true;
        createSatelliteParticles();
        completeMessage.start();
    }

    public void createSatelliteParticles() {
        explosions.add(new Explosion(projectile.getBody().getPosition().cpy(), Constants.Entities.PROJECTILE_RADIUS));
    }
}
