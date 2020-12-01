package dev.scottblechman.parsec.screens.score;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.Button;
import dev.scottblechman.parsec.common.components.StarField;

import java.util.List;

public class ScoreViewModel {

    private final StarField starField;
    private final Button newGameButton;
    private final Button quitButton;


    public ScoreViewModel(Parsec game) {
        starField = new StarField();
        newGameButton = new Button("NEW GAME", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 3f, Constants.Camera.VIEWPORT_HEIGHT / 11f), game.getFont());
        quitButton = new Button("QUIT", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 1.5f, Constants.Camera.VIEWPORT_HEIGHT / 11f), game.getFont());
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

