package dev.scottblechman.parsec.screens.menu;

import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.common.components.StarField;

import java.util.List;

public class MenuViewModel {

    private final StarField starField;


    public MenuViewModel() {
        starField = new StarField();
    }

    public void update() {
        starField.update();
    }

    public List<Vector3> getStarField() {
        return starField.getPool();
    }
}
