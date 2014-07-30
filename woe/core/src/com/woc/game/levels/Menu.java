package com.woc.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.woc.game.WocGame;

public class Menu implements Screen {

	public class LvlListener extends ChangeListener {
		private int lvl;
	
		public LvlListener(int i) {
			lvl = i-1;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			switch (lvl) {
			case 0:
				wocGame.setScreen(new Level1(wocGame));
				break;
			case 1:
				wocGame.setScreen(new Level2(wocGame));
				break;
			case 2:
				wocGame.setScreen(new Level3(wocGame));
				break;
			}
			
		}

	}

	private Stage stage;
	private Table table;
	private WocGame wocGame;

	public Menu(WocGame wocGame) {
		this.wocGame = wocGame;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		int lvl = 0;
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				TextButton button = new TextButton(++lvl + "", skin);
				button.addListener(new LvlListener(lvl));
				table.add(button).size(100, 100).pad(5, 5, 5, 5).expand();
			}
			table.row();
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
