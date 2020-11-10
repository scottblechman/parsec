package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.common.Constants;

public class LevelViewModel {

    World world;
    Body projectile;

    final float STEP_TIME = 1f/60f;
    float accumulator = 0;

    public LevelViewModel() {
        world = new World(new Vector2(0, 0), true);
        projectile = createBody(Constants.entities.PROJECTILE_INIT_POS, Constants.entities.PROJECTILE_RADIUS);
    }

    public Vector2 getProjectilePosition() {
        return projectile.getPosition();
    }

    protected void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        while (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, 6, 2);
        }
    }

    /**
     * Defines a circular body capable of moving in the world.
     * @param position initial x and y coordinates, in meters
     * @param radius circle radius, in meters
     * @return created body
     */
    private Body createBody(Vector2 position, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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
        float impulseX = start.x - end.x;
        // Assume that start pos y is always >= end pos y
        float impulseY = start.y - end.y;
        projectile.applyLinearImpulse(Constants.physics.FORCE_SCALAR * impulseX,
                Constants.physics.FORCE_SCALAR * impulseY, projectile.getPosition().x, projectile.getPosition().y, true);
    }
}
