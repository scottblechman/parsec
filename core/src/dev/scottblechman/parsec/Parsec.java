package dev.scottblechman.parsec;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.scottblechman.parsec.screens.level.LevelScreen;

public class Parsec extends Game {
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font/kenney_future.fnt"));
		shapeRenderer = new ShapeRenderer();
		this.setScreen(new LevelScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}
}
