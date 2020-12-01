package dev.scottblechman.parsec.screens.menu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.Button;
import dev.scottblechman.parsec.common.components.StarField;

import java.util.List;

public class MenuViewModel {

    private final StarField starField;
    private final Button newGameButton;
    private final Button quitButton;


    public MenuViewModel(Parsec game) {
        starField = new StarField();
        newGameButton = new Button("NEW GAME", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 2f, Constants.Camera.VIEWPORT_HEIGHT / 2f), game.getFont());
        quitButton = new Button("QUIT", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 2f, Constants.Camera.VIEWPORT_HEIGHT / 3f), game.getFont());
    }

    public void update() {
        starField.update();
        newGameButton.update();
        quitButton.update();
    }

    public List<Vector3> getStarField() {
        return starField.getPool();
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }
}
