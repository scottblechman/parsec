package dev.scottblechman.parsec.common.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.interfaces.IComponent;

public class Button implements IComponent {
    private final String text;
    private final Rectangle bounds;

    // Used for calculating rectangle position
    private final GlyphLayout glyphLayout;

    private final BitmapFont font;

    // Indicates if the cursor is hovered over this button
    private boolean hover;

    // Tracks the progress of visual transitions
    private int transitionTime;

    public Button(String text, Vector2 position, BitmapFont font) {
        this.text = text;
        this.glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, text);
        bounds = new Rectangle(position.x - (glyphLayout.width / 2), position.y, glyphLayout.width, glyphLayout.height);
        this.font = font;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer renderer) {
        Color background = Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY);
        Color foreground = Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY);
        // Background and foreground, respectively
        float[] bgHSV = new float[3];
        float[] fgHSV = new float[3];
        background.toHsv(bgHSV);
        foreground.toHsv(fgHSV);
        if(bgHSV[2] > 0) {
            bgHSV[2] -= (transitionTime / 100f);
        }
        if(fgHSV[2] > 0) {
            fgHSV[2] -= (transitionTime / 100f);
        }


        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(background.fromHsv(bgHSV));
        float x = bounds.x - Constants.Graphics.BUTTON_PADDING;
        float y = bounds.y - Constants.Graphics.BUTTON_PADDING;
        float width = glyphLayout.width + (Constants.Graphics.BUTTON_PADDING * 2);
        float height = glyphLayout.height + (Constants.Graphics.BUTTON_PADDING * 2);
        renderer.rect(x, y, width, height);
        // Rounded edges
        renderer.circle(x + 1, y + 1, Constants.Graphics.BUTTON_RADIUS);
        renderer.circle(x + width - 1, y + 1, Constants.Graphics.BUTTON_RADIUS);
        renderer.circle(x + 1, y + height - 1, Constants.Graphics.BUTTON_RADIUS);
        renderer.circle(x + width - 1, y + height - 1, Constants.Graphics.BUTTON_RADIUS);
        renderer.end();

        // Edge padding
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0; i < Constants.Graphics.BUTTON_RADIUS; i++) {
            renderer.line(x - i, y, x - i, y + height);
            renderer.line(x, y - i, x + width, y - i);
            renderer.line(x + width + i + 1, y, x + width + i + 1, y + height);
            renderer.line(x, y + height + i, x + width, y + height + i);
            renderer.line(x, y + height + i + 1, x + width, y + height + i + 1);
        }
        if(Constants.Game.DEBUG_MODE) {
            renderer.setColor(Color.BLUE);
            renderer.rect(getHoverBounds().x, getHoverBounds().y, getHoverBounds().width, getHoverBounds().height);
        }
        renderer.end();

        batch.begin();
        font.setColor(foreground.fromHsv(fgHSV));
        font.draw(batch, text, bounds.x, bounds.y + glyphLayout.height);
        font.setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        batch.end();
    }

    @Override
    public void update() {
        if(hover && transitionTime < Constants.Graphics.BUTTON_COOLDOWN_FRAMES) {
            transitionTime ++;
        } else if (!hover && transitionTime > 0) {
            transitionTime--;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Returns bounds, accommodating for padding and radius
     * @return bounds for cursor comparison
     */
    public Rectangle getHoverBounds() {
        Rectangle hoverBounds = new Rectangle();
        return hoverBounds.set(bounds.x - Constants.Graphics.BUTTON_RADIUS * 2,
                bounds.y - Constants.Graphics.BUTTON_RADIUS * 2,
                bounds.width + Constants.Graphics.BUTTON_PADDING * 2 + Constants.Graphics.BUTTON_RADIUS * 2,
                bounds.height + Constants.Graphics.BUTTON_PADDING * 2 + Constants.Graphics.BUTTON_RADIUS * 2);
    }

    public boolean isHovered() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
}
