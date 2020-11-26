package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.scottblechman.parsec.common.Constants;

public class Button {
    private final String text;
    private final Rectangle bounds;

    // Used for calculating rectangle position
    private final GlyphLayout glyphLayout;

    private final BitmapFont font;

    // Indicates if the cursor is hovered over this button
    private boolean hover;

    public Button(String text, Vector2 position, BitmapFont font) {
        this.text = text;
        this.glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, text);
        bounds = new Rectangle(position.x - (glyphLayout.width / 2), position.y, glyphLayout.width, glyphLayout.height);
        this.font = font;
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        float x = bounds.x - Constants.Graphics.BUTTON_PADDING;
        float y = bounds.y - Constants.Graphics.BUTTON_PADDING;
        float width = glyphLayout.width + (Constants.Graphics.BUTTON_PADDING * 2);
        float height = glyphLayout.height + (Constants.Graphics.BUTTON_PADDING * 2);
        renderer.rect(x, y, width, height);
        // Rounded edges
        renderer.circle(x + 1, y + 1, 3);
        renderer.circle(x + width - 1, y + 1, 3);
        renderer.circle(x + 1, y + height - 1, 3);
        renderer.circle(x + width - 1, y + height - 1, 3);
        renderer.end();

        // Edge padding
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0; i < 2; i++) {
            renderer.line(x - i, y, x - i, y + height);
            renderer.line(x, y - i, x + width, y - i);
            renderer.line(x + width + i + 1, y, x + width + i + 1, y + height);
            renderer.line(x, y + height + i, x + width, y + height + i);
            renderer.line(x, y + height + i + 1, x + width, y + height + i + 1);
        }
        renderer.end();

        batch.begin();
        font.setColor(Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY));
        font.draw(batch, text, bounds.x, bounds.y + glyphLayout.height);
        font.setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        batch.end();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isHovered() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
}
