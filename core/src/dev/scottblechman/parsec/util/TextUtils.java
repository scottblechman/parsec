package dev.scottblechman.parsec.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import dev.scottblechman.parsec.common.Constants;

public class TextUtils {

    private TextUtils() { }

    private static final GlyphLayout layout = new GlyphLayout();

    public static float centerHorizontal(BitmapFont font, String text) {
        float initialCenter = (float) Constants.Camera.VIEWPORT_WIDTH / 2f;
        layout.setText(font, text);
        return initialCenter - (layout.width / 2);
    }

    public static float centerVertical(BitmapFont font, String text) {
        float initialCenter = (float) Constants.Camera.VIEWPORT_HEIGHT / 2f;
        layout.setText(font, text);
        return initialCenter + (layout.height / 2);
    }
}
