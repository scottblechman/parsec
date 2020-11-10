package dev.scottblechman.parsec.common;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    public static class camera {

        private camera() { }

        public static final int VIEWPORT_WIDTH = 640;
        public static final int VIEWPORT_HEIGHT = 480;
    }

    public static class physics {

        private physics() { }

        public static final int FORCE_SCALAR = 20;
    }

    public static class entities {

        private entities() { }

        public static final int PROJECTILE_RADIUS = 4;
        public static final Vector2 PROJECTILE_INIT_POS = new Vector2(320, 240);
    }
}
