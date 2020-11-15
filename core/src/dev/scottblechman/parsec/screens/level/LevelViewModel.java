package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.listeners.ProjectileListener;
import dev.scottblechman.parsec.models.Level;
import dev.scottblechman.parsec.models.Projectile;
import dev.scottblechman.parsec.models.enums.EntityType;
import dev.scottblechman.parsec.models.Star;

import java.util.ArrayList;

public class LevelViewModel {

    World world;
    Projectile projectile;
    Star star;
    ProjectileListener contactListener;
    Level level;

    static final float STEP_TIME = 1f/60f;
    float accumulator = 0;
    float timeout = 0; // Upper limit on projectile motion

    // Wait to apply gravity until projectile is in motion
    private boolean projectileInMotion = false;

    private int shotsAttempted = 0;

    private final ArrayList<Body> bodiesToDestroy = new ArrayList<>();

    // Body reset flags
    private boolean resetProjectile = false;

    public LevelViewModel() {
        world = new World(new Vector2(0, 0), true);
        projectile = new Projectile(world);
        star = new Star(world);
        contactListener = new ProjectileListener(this);
        world.setContactListener(contactListener);
        level = new Level(0);
    }

    public Vector2 getProjectilePosition() {
        return projectile.getPosition();
    }

    public Vector2 getSunPosition() {
        return star.getPosition();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInMotion() {
        return projectileInMotion;
    }

    public int getShots() {
        return shotsAttempted;
    }

    public int getLevelNumber() {
        return level.getLevelNumber() + 1;
    }

    protected void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

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
    }

    /**
     * Uses the existing mouse drag information to apply an impulse to the projectile body.
     */
    protected void shootProjectile(Vector2 start, Vector2 end) {
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
            shotsAttempted++;
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body b : bodies) {
            EntityType type = (EntityType) b.getUserData();

            if (type == EntityType.PROJECTILE) {
                bodiesToDestroy.add(b);
                break;
            }
        }
        resetProjectile = true;
        projectileInMotion = false;
        timeout = 0;
    }
}
