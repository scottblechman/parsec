package dev.scottblechman.parsec.state;

import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.screens.level.LevelScreen;
import dev.scottblechman.parsec.screens.score.ScoreScreen;
import dev.scottblechman.parsec.state.enums.ScreenState;

public class ScreenNavigation {

    private final Parsec game;
    private ScreenState state;

    public ScreenNavigation(Parsec game) {
        this.game = game;
        this.state = ScreenState.GAME;
        changeScreen();
    }

    public ScreenState getState() {
        return state;
    }

    public void setState(ScreenState state) {
        this.state = state;

    }

    public void changeScreen() {
        switch (state) {
            case GAME:
                game.setScreen(new LevelScreen(game));
                break;
            case SCORE:
                game.setScreen(new ScoreScreen(game));
                break;
            default:
                break;
        }
    }
}
