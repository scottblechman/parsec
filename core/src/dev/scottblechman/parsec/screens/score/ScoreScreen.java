package dev.scottblechman.parsec.screens.score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.util.TextUtils;

public class ScoreScreen implements Screen, InputProcessor {

    private final Parsec game;
    private final OrthographicCamera camera;

    private TextUtils textUtils;

    public ScoreScreen(Parsec game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Camera.VIEWPORT_WIDTH, Constants.Camera.VIEWPORT_HEIGHT);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        this.textUtils = new TextUtils(game.getFont(), game.getSpriteBatch());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().begin();
        textUtils.writeGrid("GAME OVER", 5, 2, 4);
        textUtils.writeGrid("TOTAL SCORE: " + game.getScoreService().getTotal(), 11, 5, 1);
        textUtils.writeList(makeScoreList());
        textUtils.writeGrid("PLAY AGAIN", 7, 2, 0);
        textUtils.writeGrid("QUIT", 7, 4, 0);
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        // Intentionally left empty
    }

    @Override
    public void pause() {
        // Intentionally left empty
    }

    @Override
    public void resume() {
        // Intentionally left empty
    }

    @Override
    public void hide() {
        // Intentionally left empty
    }

    @Override
    public void dispose() {
        // Intentionally left empty
    }

    private String[] makeScoreList() {
        String[] scoreLines = new String[game.getScoreService().getScores().size()];
        for(int i = 0; i < game.getScoreService().getScores().size(); i++) {
            scoreLines[i] = "SYSTEM " + (i + 1) + ": " + game.getScoreService().getScores().get(i);
        }
        return scoreLines;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
