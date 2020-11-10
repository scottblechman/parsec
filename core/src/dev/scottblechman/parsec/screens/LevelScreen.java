package dev.scottblechman.parsec.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.Parsec;

public class LevelScreen implements Screen, InputProcessor {

    final Parsec game;

    OrthographicCamera camera;

    // TODO: 11/9/2020 Move this to a Pellet class inside a ViewModel.
    final Vector2 pelletPosition;
    final float PELLET_RADIUS = 4f;
    World world;
    Body pellet;

    final float STEP_TIME = 1f/60f;
    float accumulator = 0;

    Vector3 tp = new Vector3();
    boolean dragging;

    // The starting and ending point of the drag motion, for rendering
    Vector2 dragPosStart = new Vector2();
    Vector2 dragPosEnd = new Vector2();

    final int FORCE_SCALAR = 20;

    public LevelScreen(Parsec game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        pelletPosition = new Vector2((float) (((double) Gdx.graphics.getWidth()) / 2),
                (float) (((double) Gdx.graphics.getHeight()) / 2));

        world = new World(new Vector2(0, 0), true);
        pellet = createBody(pelletPosition, PELLET_RADIUS);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

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
        // Draw pellet
        game.shapeRenderer.circle(pellet.getPosition().x, pellet.getPosition().y, PELLET_RADIUS);
        // Draw drag (if occurring)
        if(dragging) {
            game.shapeRenderer.line(dragPosStart, dragPosEnd);
        }
        game.shapeRenderer.end();

        stepWorld();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        while (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, 6, 2);
        }
    }

    /**
     * Defines a circular body capable of moving in the world.
     * @param position initial x and y coordinates, in meters
     * @param radius circle radius, in meters
     * @return created body
     */
    private Body createBody(Vector2 position, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);

        circle.dispose();

        return body;
    }

    /**
     * Uses the existing mouse drag information to apply an impulse to the pellet body.
     */
    private void shootPellet() {
        float impulseX = dragPosStart.x - dragPosEnd.x;
        // Assume that start pos y is always >= end pos y
        float impulseY = dragPosStart.y - dragPosEnd.y;
        pellet.applyLinearImpulse(FORCE_SCALAR * impulseX, FORCE_SCALAR * impulseY,
                pellet.getPosition().x, pellet.getPosition().y, true);
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
        dragPosStart.x = screenX;
        dragPosStart.y = camera.viewportHeight - screenY;
        dragPosEnd.x = screenX;
        dragPosEnd.y = camera.viewportHeight - screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = false;
        shootPellet();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragPosEnd.x = screenX;
        // Ensure end point is never higher than start point (pellet never shot negative)
        if(camera.viewportHeight - screenY < dragPosStart.y)
            dragPosEnd.y = camera.viewportHeight - screenY;
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
