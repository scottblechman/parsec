package dev.scottblechman.parsec.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.models.enums.EntityType;

public class Barrier {

    private final Body body;
    private final float width;
    private final float height;

    public static class Builder {
        private final World world;

        private Vector2 position = new Vector2();
        private float width;
        private float height;

        private Body body = null;

        public Builder(World world) {
            this.world = world;
        }

        public Builder position(Vector2 position) {
            this.position = position;
            return this;
        }

        public Builder width(float width) {
            this.width = width;
            return this;
        }

        public Builder height(float height) {
            this.height = height;
            return this;
        }

        public Barrier build() {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(this.position.x, this.position.y);

            this.body = world.createBody(bodyDef);

            PolygonShape rect = new PolygonShape();
            rect.setAsBox(width/2,height/2);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = rect;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0f;
            fixtureDef.restitution = 0f;

            fixtureDef.isSensor = false;

            body.createFixture(fixtureDef);
            body.getFixtureList().get(0).setUserData(EntityType.BARRIER);
            body.setUserData(EntityType.BARRIER);

            rect.dispose();

            return new Barrier(this);
        }
    }

    protected Barrier(Builder builder) {
        body = builder.body;
        width = builder.width;
        height = builder.height;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
