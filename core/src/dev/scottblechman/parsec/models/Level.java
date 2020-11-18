package dev.scottblechman.parsec.models;

public class Level {
    private int targetRadius;
    private int[] moons;

    public Level() {
        // Intentionally left empty for JSON de-serialization
    }

    public Level(int radius, int[] moons) {
        this.targetRadius = radius;
        this.moons = moons;
    }

    public int getTargetRadius() {
        return targetRadius;
    }

    public int[] getMoons() {
        return moons;
    }
}
