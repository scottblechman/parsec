package dev.scottblechman.parsec.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.scottblechman.parsec.Parsec;
import dev.scottblechman.parsec.common.Constants;

public class LevelScreen implements Screen, InputProcessor {

    final Parsec game;
    OrthographicCamera camera;
    LevelViewModel viewModel;

    Vector3 tp = new Vector3();
    boolean dragging;
    // Starting and ending points of the drag motion
    Vector2 dragStart = new Vector2();
    Vector2 dragEnd = new Vector2();

    public LevelScreen(Parsec game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.camera.VIEWPORT_WIDTH, Constants.camera.VIEWPORT_HEIGHT);

        viewModel = new LevelViewModel();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        // Method intentionally left empty.
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw projectile
        game.shapeRenderer.circle(viewModel.getProjectilePosition().x, viewModel.getProjectilePosition().y,
                Constants.entities.PROJECTILE_RADIUS);
        // Draw drag (if occurring)
        if(dragging) {
            game.shapeRenderer.line(dragStart, dragEnd);
        }
        game.shapeRenderer.end();

        viewModel.stepWorld();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // Method intentionally left empty.
    }

    @Override
    public void resume() {
        // Method intentionally left empty.
    }

    @Override
    public void hide() {
        // Method intentionally left empty.
    }

    @Override
    public void dispose() {
        // Method intentionally left empty.
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
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = true;
        dragStart.x = screenX;
        dragEnd.x = screenX;
        dragStart.y = camera.viewportHeight - screenY;
        dragEnd.y = camera.viewportHeight - screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = false;
        viewModel.shootProjectile(dragStart, dragEnd);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragEnd.x = screenX;
        // Ensure end point is never higher than start point (pellet never shot negative)
        if(camera.viewportHeight - screenY < dragStart.y)
            dragEnd.y = camera.viewportHeight - screenY;
        return true;
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
