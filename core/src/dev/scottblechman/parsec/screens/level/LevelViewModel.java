package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.common.Constants;

public class LevelViewModel {

    World world;
    Body projectile, sun;

    final float STEP_TIME = 1f/60f;
    float accumulator = 0;
    float timeout = 0; // Upper limit on projectile motion

    // Wait to apply gravity until projectile is in motion
    private boolean projectileInMotion = false;

    private int shotsAttempted = 0;

    public LevelViewModel() {
        world = new World(new Vector2(0, 0), true);
        projectile = createBody(Constants.entities.PROJECTILE_INIT_POS, Constants.entities.PROJECTILE_RADIUS, BodyDef.BodyType.DynamicBody);
        sun = createBody(Constants.entities.SUN_INIT_POS, Constants.entities.SUN_RADIUS, BodyDef.BodyType.StaticBody);
    }

    public Vector2 getProjectilePosition() {
        return projectile.getPosition();
    }

    public Vector2 getSunPosition() {
        return sun.getPosition();
    }

    public boolean isInMotion() {
        return projectileInMotion;
    }

    public int getShots() {
        return shotsAttempted;
    }

    protected void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        while (accumulator >= STEP_TIME) {
            // Apply forces before stepping world
            if (projectileInMotion) {
                // Check if we have gone over the projectile timeout
                timeout += accumulator;
                System.out.println(timeout);
                if(timeout >= Constants.game.PROJECTILE_TIMEOUT) {
                    reset(true);
                } else {
                    applyGravitationalForce(projectile);
                }
            }

            world.step(STEP_TIME, 6, 2);

            accumulator -= STEP_TIME;
        }
    }

    /**
     * Defines a circular body capable of moving in the world.
     * @param position initial x and y coordinates, in meters
     * @param radius circle radius, in meters
     * @param type the type of body: Static, Dynamic, or Kinematic
     * @return created body
     */
    private Body createBody(Vector2 position, float radius, BodyDef.BodyType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(position.x, position.y);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();

        return body;
    }

    /**
     * Uses the existing mouse drag information to apply an impulse to the projectile body.
     */
    protected void shootProjectile(Vector2 start, Vector2 end) {
        projectileInMotion = true;
        float impulseX = start.x - end.x;
        // Assume that start pos y is always >= end pos y
        float impulseY = start.y - end.y;
        projectile.applyLinearImpulse(Constants.physics.FORCE_SCALAR * impulseX,
                Constants.physics.FORCE_SCALAR * impulseY, projectile.getPosition().x, projectile.getPosition().y, true);
    }

    /**
     * Applies a linear force on the projectile to mimic gravitational pull from the sun.
     */
    private void applyGravitationalForce(Body body) {
        float radius = body.getFixtureList().get(0).getShape().getRadius();
        Vector2 vecDistance = body.getPosition().sub(sun.getPosition());
        float distance = vecDistance.len();
        vecDistance.scl(-1); // Vector negative
        float magnitude = Math.abs(vecDistance.x) + Math.abs(vecDistance.y);
        vecDistance.scl((1/magnitude) * radius/distance);
        vecDistance.scl(Constants.physics.FORCE_SCALAR * Constants.physics.GRAVITY_SCALAR);
        body.applyForceToCenter(vecDistance, true);
    }

    /**
     * Sets up a new shot after collision or timeout.
     * @param increment whether to add a shot to the total count
     */
    private void reset(boolean increment) {
        if(increment) {
            shotsAttempted++;
        }
        projectile = createBody(Constants.entities.PROJECTILE_INIT_POS, Constants.entities.PROJECTILE_RADIUS, BodyDef.BodyType.DynamicBody);
        projectileInMotion = false;
        timeout = 0;
    }
}
