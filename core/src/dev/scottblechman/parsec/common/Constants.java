package dev.scottblechman.parsec.common;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    private Constants() { }

    public static class Camera {

        private Camera() { }

        public static final int VIEWPORT_WIDTH = 720;
        public static final int VIEWPORT_HEIGHT = 640;
        public static final int MARGIN = 16;
    }

    public static class Physics {

        private Physics() { }

        public static final float FORCE_SCALAR = 15f;
        public static final float MOON_FORCE_SCALAR = 1000f;
        public static final float GRAVITY_SCALAR = 10000f;
    }

    public static class Entities {

        private Entities() { }

        public static final int PROJECTILE_RADIUS = 4;
        public static final Vector2 PROJECTILE_INIT_POS = new Vector2((float) Camera.VIEWPORT_WIDTH / 2f,
                (float) Camera.VIEWPORT_HEIGHT / 4f);
        public static final int SUN_RADIUS = 32;
        public static final Vector2 SUN_INIT_POS = new Vector2((float) Camera.VIEWPORT_WIDTH / 2f,
                (float) Camera.VIEWPORT_HEIGHT / 2f);
        public static final int MOON_RADIUS = 4;
    }

    public static class Game {

        private Game() { }

        public static final float PROJECTILE_TIMEOUT = 30f;
    }
}
