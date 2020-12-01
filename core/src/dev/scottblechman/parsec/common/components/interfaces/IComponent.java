package dev.scottblechman.parsec.common.components.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IComponent {
    void draw(SpriteBatch batch, ShapeRenderer renderer);
    void update();
}
