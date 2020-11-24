package dev.scottblechman.parsec.models;

public class Level {
    private int targetRadius;
    private int[] moons;
    private String message;
    private boolean barrier;
    private boolean advanceAlways;

    public Level() {
        // Intentionally left empty for JSON de-serialization
    }

    public Level(int radius, int[] moons, String message, boolean barrier, boolean advanceAlways) {
        this.targetRadius = radius;
        this.moons = moons;
        this.message = message;
        this.barrier = barrier;
        this.advanceAlways = advanceAlways;
    }

    public int getTargetRadius() {
        return targetRadius;
    }

    public int[] getMoons() {
        return moons;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasBarrier() {
        return barrier;
    }

    public boolean shouldAlwaysAdvance() {
        return advanceAlways;
    }
}
