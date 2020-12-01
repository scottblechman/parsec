package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.interfaces.IComponent;

public class Explosion implements IComponent {

    private final Vector2 center;
    private final int radius;

    // Tracks  the current size of the effect
    int currentRadius = 1;

    public Explosion(Vector2 center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        renderer.setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(center.x, center.y, currentRadius);
        renderer.end();
    }

    @Override
    public void update() {
        if(currentRadius < (radius * Constants.Graphics.EXPLOSION_RADIUS_SCALAR)) {
            currentRadius++;
        }
    }

    /**
     * Checks if the component is ready for removal from a pool
     * @return whether the explosion has reached its max radius
     */
    public boolean isComplete() {
        return currentRadius >= radius * Constants.Graphics.EXPLOSION_RADIUS_SCALAR;
    }
}
