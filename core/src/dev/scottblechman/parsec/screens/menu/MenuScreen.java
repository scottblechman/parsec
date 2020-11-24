package dev.scottblechman.parsec.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;
import dev.scottblechman.parsec.common.components.Button;
import dev.scottblechman.parsec.state.enums.ScreenState;
import dev.scottblechman.parsec.util.TextUtils;

public class MenuScreen implements Screen, InputProcessor {

    private final Parsec game;
    private final OrthographicCamera camera;

    private TextUtils textUtils;

    private final Button newGameButton;
    private final Button quitButton;

    Vector3 tp = new Vector3();

    public MenuScreen(Parsec game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Camera.VIEWPORT_WIDTH, Constants.Camera.VIEWPORT_HEIGHT);
        newGameButton = new Button("NEW GAME", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 2f, Constants.Camera.VIEWPORT_HEIGHT / 2f), game.getFont());
        quitButton = new Button("QUIT", new Vector2(Constants.Camera.VIEWPORT_WIDTH / 2f, Constants.Camera.VIEWPORT_HEIGHT / 3f), game.getFont());

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        textUtils = new TextUtils(game.getFont(), game.getSpriteBatch());
    }

    @Override
    public void render(float delta) {
        Color color = Color.valueOf(Constants.Colors.BACKGROUND_PRIMARY);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.getSpriteBatch().setColor(Color.valueOf(Constants.Colors.FOREGROUND_PRIMARY));
        game.getSpriteBatch().begin();
        game.getFont().getData().setScale(3f);
        textUtils.writeGrid("(PAR) SEC", 5, 2, 3);
        game.getFont().getData().setScale(1f);
        newGameButton.draw(game.getSpriteBatch());
        quitButton.draw(game.getSpriteBatch());
        game.getSpriteBatch().end();

        if(Constants.Game.DEBUG_MODE) {
            game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
            game.getShapeRenderer().rect(newGameButton.getBounds().x, newGameButton.getBounds().y, newGameButton.getBounds().width, newGameButton.getBounds().height);
            game.getShapeRenderer().rect(quitButton.getBounds().x, quitButton.getBounds().y, quitButton.getBounds().width, quitButton.getBounds().height);
            game.getShapeRenderer().end();
        }
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
        camera.unproject(tp.set(screenX, screenY, 0));
        if(newGameButton.getBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            game.navigateTo(ScreenState.GAME);
        } else if(quitButton.getBounds().contains(screenX, Constants.Camera.VIEWPORT_HEIGHT - (float) screenY)) {
            Gdx.app.exit();
        }
        return true;
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
