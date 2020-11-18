package dev.scottblechman.parsec.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import dev.scottblechman.parsec.models.Level;

import java.util.ArrayList;

public class LevelService {

    private int currentLevel = 0;
    private final ArrayList<Level> levels;

    public LevelService() {
        Json json = new Json();
        this.levels = json.fromJson(ArrayList.class, Level.class, Gdx.files.internal("data/levels.json"));
    }

    public int getLevelRadius() {
        return levels.get(currentLevel).targetRadius;
    }

    public int getLevelNumber() {
        return currentLevel + 1;
    }

    public void nextLevel() {
        if(currentLevel < levels.size()) {
            currentLevel++;
        }
    }
}
