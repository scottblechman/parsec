package dev.scottblechman.parsec.state;

import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.screens.level.LevelScreen;
import dev.scottblechman.parsec.screens.menu.MenuScreen;
import dev.scottblechman.parsec.screens.score.ScoreScreen;
import dev.scottblechman.parsec.state.enums.ScreenState;

public class ScreenNavigation {

    private final Parsec game;
    private ScreenState state;

    public ScreenNavigation(Parsec game) {
        this.game = game;
        this.state = ScreenState.MENU;
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
            case MENU:
                game.setScreen(new MenuScreen(game));
                break;
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
