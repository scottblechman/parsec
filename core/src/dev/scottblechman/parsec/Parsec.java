package dev.scottblechman.parsec;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.scottblechman.parsec.screens.LevelScreen;

public class Parsec extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new LevelScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
