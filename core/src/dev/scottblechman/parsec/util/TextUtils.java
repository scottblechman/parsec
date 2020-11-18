package dev.scottblechman.parsec.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
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
    public void writeGrid(String text, int squares, int offsetHorizontal, int offsetVertical) {
        if((offsetHorizontal < 0 || offsetHorizontal > squares - 1) || (offsetVertical < 0 || offsetVertical > squares - 1)) {
            Gdx.app.log(TextUtils.class.toString(), "Text at (" + offsetHorizontal + ", " + offsetVertical +
                    ") is out of bounds horizontally. The text was not written.");
            return;
        }
        float left = (Constants.Camera.VIEWPORT_WIDTH / (float) squares) * (float) offsetHorizontal;
        float top = (Constants.Camera.VIEWPORT_HEIGHT / (float) squares) * (float) offsetVertical;
        layout.setText(font, text);
        // Y needs to offset by height to start at quadrant's (0, 0)
        top += layout.height;
        font.draw(batch, text, left + (Constants.Camera.VIEWPORT_WIDTH / (float) (squares * 2)) - (layout.width / 2),
                top + (Constants.Camera.VIEWPORT_HEIGHT / (float) (squares * 2)));
    }

    /**
     * Draws a list of strings to the screen. Centers the block of text in the middle of the screen by default.
     * @param lines the text to be written
     */
    public void writeList(String[] lines) {
        // Use the longest string in the list to set the horizontal offset
        String longest = "";
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if(line.length() > longest.length()) {
                longest = line;
            }
            builder.append(line);
            builder.append("\n");
        }
        float x = Constants.Camera.VIEWPORT_WIDTH / 2f;
        float y = Constants.Camera.VIEWPORT_HEIGHT / 2f;
        layout.setText(font, longest);
        x -= layout.width / 2f;
        layout.setText(font, builder.toString());
        y += layout.height / 2f;

        font.draw(batch, builder.toString(), x, y, Align.center, (int) (Constants.Camera.VIEWPORT_WIDTH / 2f), true);
    }

}
