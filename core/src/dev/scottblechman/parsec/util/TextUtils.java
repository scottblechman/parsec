package dev.scottblechman.parsec.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.scottblechman.parsec.common.Constants;

public class TextUtils {

    private final BitmapFont font;
    private final SpriteBatch batch;
    private final GlyphLayout layout;

    public TextUtils(BitmapFont font, SpriteBatch batch) {
        this.font = font;
        this.batch = batch;
        this.layout = new GlyphLayout();
    }

    /**
     * Draws text to the screen, assuming a 4x4 grid. Draws to the center of the quadrant by default.
     * @param text String to be written
     * @param offsetHorizontal the horizontal quadrant to write in, from left to right
     * @param offsetVertical the vertical quadrant to write in, from bottom to top
     */
    public void write4x4(String text, int offsetHorizontal, int offsetVertical) {
        if((offsetHorizontal < 0 || offsetHorizontal > 3) || (offsetVertical < 0 || offsetVertical > 3)) {
            Gdx.app.log(TextUtils.class.toString(), "Text at (" + offsetHorizontal + ", " + offsetVertical +
                    ") is out of bounds horizontally. The text was not written.");
            return;
        }
        float left = (Constants.Camera.VIEWPORT_WIDTH / 4f) * (float) offsetHorizontal;
        float top = (Constants.Camera.VIEWPORT_HEIGHT / 4f) * (float) offsetVertical;
        layout.setText(font, text);
        // Y needs to offset by height to start at quadrant's (0, 0)
        top += layout.height;
        font.draw(batch, text, left + (Constants.Camera.VIEWPORT_WIDTH / 8f) - (layout.width / 2),
                top + (Constants.Camera.VIEWPORT_HEIGHT / 8f));
    }

}
