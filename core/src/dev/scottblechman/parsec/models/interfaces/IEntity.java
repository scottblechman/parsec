package dev.scottblechman.parsec.models.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface IEntity {
    Body getBody();
    Vector2 getPosition();
}
