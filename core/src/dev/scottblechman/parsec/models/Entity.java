package dev.scottblechman.parsec.models;

import com.badlogic.gdx.math.Vector2;
import dev.scottblechman.parsec.models.enums.EntityType;

public class Entity {

    private final Vector2 position;
    private float radius;
    private EntityType type;

    public Entity() {
        position = new Vector2();
        radius = 0;
        type = EntityType.UNDEFINED;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }
}
