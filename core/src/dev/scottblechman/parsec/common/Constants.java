package dev.scottblechman.parsec.common;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    public static class camera {

        private camera() { }

        public static final int VIEWPORT_WIDTH = 720;
        public static final int VIEWPORT_HEIGHT = 640;
    }

    public static class physics {

        private physics() { }

        public static final float FORCE_SCALAR = 15f;
        public static final float GRAVITY_SCALAR = 10000f;
    }

    public static class entities {

        private entities() { }

        public static final int PROJECTILE_RADIUS = 4;
        public static final Vector2 PROJECTILE_INIT_POS = new Vector2((float) camera.VIEWPORT_WIDTH / 2f,
                (float) camera.VIEWPORT_HEIGHT / 4f);
        public static final int SUN_RADIUS = 32;
        public static final Vector2 SUN_INIT_POS = new Vector2((float) camera.VIEWPORT_WIDTH / 2f,
                (float) camera.VIEWPORT_HEIGHT / 2f);
    }
}
