package dev.scottblechman.parsec.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.scottblechman.parsec.Parsec;

public class LevelScreen implements Screen {

    final Parsec game;

    OrthographicCamera camera;

    // TODO: 11/9/2020 Move this to a Pellet class inside a ViewModel.
    final Vector2 pelletPosition;
    final float PELLET_RADIUS = 4f;
    World world;
    Body pellet;

    final float STEP_TIME = 1f/60f;
    float accumulator = 0;

    public LevelScreen(Parsec game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        pelletPosition = new Vector2((float) (((double) Gdx.graphics.getWidth()) / 2),
                (float) (((double) Gdx.graphics.getHeight()) / 2));

        world = new World(new Vector2(0, 0), true);
        pellet = createBody(pelletPosition, PELLET_RADIUS);
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

        game.batch.begin();

        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw pellet
        game.shapeRenderer.circle(pelletPosition.x, pelletPosition.y, PELLET_RADIUS);
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
}
