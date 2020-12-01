package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.interfaces.IComponent;

public class TypewriterText implements IComponent {

    private float elapsedTime = 0f;
    private final String text;
    private boolean writing;

    // For accessing SFX service
    private final Parsec game;

    public TypewriterText(String text, boolean startImmediately, Parsec game) {
        this.text = text;
        writing = startImmediately;
        this.game = game;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        // Method intentionally empty
    }

    @Override
    public void update() {
        if(text != null && writing && elapsedTime <= text.length()) {
            elapsedTime += (Gdx.graphics.getDeltaTime() * Constants.Graphics.TYPEWRITER_SPEED);
            if(elapsedTime > text.length()) {
                writing = false;
                game.getSoundService().stopTypewriterSFX();
            }
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
        game.getSoundService().playTypewriterSFX();
    }

    public void reset() {
        writing = false;
        elapsedTime = 0;
    }
}
