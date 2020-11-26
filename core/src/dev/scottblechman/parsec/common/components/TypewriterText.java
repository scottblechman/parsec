package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.interfaces.IComponent;

public class TypewriterText implements IComponent {

    private float elapsedTime = 0f;
    private final String text;
    private boolean writing;

    public TypewriterText(String text, boolean startImmediately) {
        this.text = text;
        writing = startImmediately;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        // Method intentionally empty
    }

    @Override
    public void update() {
        if(writing && elapsedTime <= text.length()) {
            elapsedTime += (Gdx.graphics.getDeltaTime() * Constants.Graphics.TYPEWRITER_SPEED);
        }
    }

    public String getText() {
        if(elapsedTime >= text.length()) {
            elapsedTime = text.length();
            return text;
        }
        return text.substring(0, Math.round(elapsedTime));
    }

    public void start() {
        writing = true;
    }

    public void reset() {
        writing = false;
        elapsedTime = 0;
    }
}
