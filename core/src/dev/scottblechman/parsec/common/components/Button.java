package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
    private String text;
    private Rectangle bounds;

    // Used for calculating rectangle position
    private final GlyphLayout glyphLayout;

    private final BitmapFont font;

    public Button(String text, Vector2 position, BitmapFont font) {
        this.text = text;
        this.glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, text);
        bounds = new Rectangle(position.x - (glyphLayout.width / 2), position.y, glyphLayout.width, glyphLayout.height);
        this.font = font;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, text, bounds.x, bounds.y + glyphLayout.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
