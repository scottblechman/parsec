package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import dev.scottblechman.parsec.models.Level;

import java.util.ArrayList;

public class LevelService {

    private int currentLevel = 0;
    private int numTutorialLevels = 0;
    private ArrayList<Level> levels;

    public LevelService(boolean tutorial) {
        Json json = new Json();
        //noinspection unchecked
        this.levels = json.fromJson(ArrayList.class, Level.class, Gdx.files.internal("data/levels.json"));

        // If we are doing the tutorial, prepend the tutorial levels
        if(tutorial) {
            //noinspection unchecked
            ArrayList<Level> tutorialLevels = json.fromJson(ArrayList.class, Level.class, Gdx.files.internal("data/tutorial.json"));
            numTutorialLevels = tutorialLevels.size();
            tutorialLevels.addAll(this.levels);
            this.levels = tutorialLevels;
        }
    }

    public int getLevelRadius() {
        return levels.get(currentLevel).getTargetRadius();
    }

    public int getLevelNumber() {
        return currentLevel + 1;
    }

    public int[] getMoonRadii() {
        if(levels.get(currentLevel).getMoons() == null) {
            return new int[0];
        }
        return levels.get(currentLevel).getMoons();
    }

    public int getMoonRadius(int index) {
        return levels.get(currentLevel).getMoons()[index];
    }

    public String getMessage() {
        return levels.get(currentLevel).getMessage();
    }

    public boolean createBarrier() {
        return levels.get(currentLevel).hasBarrier();
    }

    public boolean shouldAlwaysAdvance() {
        return levels.get(currentLevel).shouldAlwaysAdvance();
    }

    /**
     * Checks if the current level is a tutorial level.
     * @return true when the current level is higher than the number of tutorial levels
     */
    public boolean onTutorialLevel() {
        return currentLevel >= numTutorialLevels;
    }

    public boolean lastLevel() {
        return currentLevel == levels.size() - 1;
    }

    public void nextLevel() {
        if(currentLevel < levels.size()) {
            currentLevel++;
        }
    }
}
