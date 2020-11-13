package dev.scottblechman.parsec.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.models.enums.EntityType;

public class Entity {

    private final Vector2 position;
    private final float radius;
    private final BodyDef.BodyType bodyType;
    private final EntityType entityType;
    private final boolean isSensor;
    private final Body body;

    public static class Builder {
        private final World world;

        private Vector2 position = new Vector2();
        private float radius = 0;
        private BodyDef.BodyType bodyType = null;
        private EntityType entityType = EntityType.UNDEFINED;
        private boolean isSensor = false;
        private Body body = null;

        public Builder(World world) {
            this.world = world;
        }

        public Builder position(Vector2 position) {
            this.position = position;
            return this;
        }

        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder bodyType(BodyDef.BodyType type) {
            this.bodyType = type;
            return this;
        }

        public Builder entityType(EntityType type) {
            this.entityType = type;
            return this;
        }

        public Builder isSensor(boolean isSensor) {
            this.isSensor = isSensor;
            return this;
        }

        public Entity build() {
            // Create body
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = this.bodyType;
            bodyDef.position.set(this.position.x, this.position.y);

            this.body = world.createBody(bodyDef);

            CircleShape circle = new CircleShape();
            circle.setRadius(this.radius);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0f;
            fixtureDef.restitution = 0f;

            fixtureDef.isSensor = this.isSensor;

            body.createFixture(fixtureDef);
            body.getFixtureList().get(0).setUserData(this.entityType);
            body.setUserData(this.entityType);

            circle.dispose();

            return new Entity(this);
        }
    }

    protected Entity(Builder builder) {
        position = builder.position;
        radius = builder.radius;
        bodyType = builder.bodyType;
        entityType = builder.entityType;
        isSensor = builder.isSensor;
        body = builder.body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setPosition(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public float getRadius() {
        return radius;
    }

    public BodyDef.BodyType getBodyType() {
        return bodyType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean isSensor() {
        return isSensor;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return body.getFixtureList().get(0);
    }
}
