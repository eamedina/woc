package com.woc.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.woc.game.Assets;
import com.woc.game.Sounds;
import com.woc.game.WocCamera;
import com.woc.game.WocGame;
import com.woc.game.etities.Bulb;
import com.woc.game.etities.ColorBall;
import com.woc.game.etities.PowerStation;

public abstract class SimpleAbstractLevel implements Screen, Level {

	private SpriteBatch batch;
	public WocCamera camera;
	public World world;
	private Box2DDebugRenderer renderer;

	WocBackground1 background;
	
	public ArrayList<ColorBall> freeBalls = new ArrayList<ColorBall>();
	private ArrayList<ColorBall> fixedBals = new ArrayList<ColorBall>();
	ColorBall draggedBall;
	
	protected PowerStation powerStation;
	protected Bulb bulb;
	protected long selectedSoundId;
	private TextButton button;
	private Stage stage;

	private InputAdapter dragProcessor = (new DragProcessor(this));
	private WocGame wocGame;

	public SimpleAbstractLevel(WocGame wocGame) {
		this.wocGame = wocGame;
	}

	public void fixColorBall(ColorBall colorBall) {
		freeBalls.remove(colorBall);
		fixedBals.add(colorBall);
	}

	@Override
	public void show() {
		Assets.load();
		world = new World(new Vector2(0, -9.8f), true);
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		camera = new WocCamera(this, 0, -6);
		background = new WocBackground1(this);

		setEntities();
		setupBalls();

		createUI();

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new GestureDetector(camera), camera, dragProcessor));
	}

	private void createUI() {
		stage = new Stage(new ScreenViewport(), batch);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Table table = new Table();
		// table.debug();
		stage.addActor(table);
		table.bottom().right();
		table.setFillParent(true);
		button = new TextButton("Restart", skin);
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				resetLevel();
			}
		});
		table.add(button).size(100, 100).pad(5, 5, 5, 5);
		button = new TextButton("menu", skin);
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				wocGame.setScreen(new Menu(wocGame));
				// dispose();
			}
		});
		table.add(button).size(100, 100).pad(5, 5, 5, 5);
	}

	abstract void setEntities();

	private void setupBalls() {
		createColorBalls();
		bulb.addBalls(fixedBals);
		powerStation.addBalls(fixedBals);
	}

	abstract void createColorBalls();

	@Override
	public void resize(int width, int height) {
		camera.resize(width, height);
	}

	@Override
	public void render(float delta) {
		world.step(delta, 8, 3);
		for (int i = freeBalls.size() - 1; i >= 0; i--) {
			if (freeBalls.get(i).toDie) {
				freeBalls.get(i).dispose();
				freeBalls.remove(i);
			}
		}

		camera.step(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		background.draw(batch);
		powerStation.draw(batch);
		bulb.draw(batch);

		for (int i = 0; i < freeBalls.size(); i++) {
			freeBalls.get(i).draw(batch);
		}
		for (int i = 0; i < fixedBals.size(); i++) {
			fixedBals.get(i).draw(batch);
		}
		batch.end();
		stage.draw();
		// Table.drawDebug(stage);
		// renderer.render(world, camera.combined);
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
		stage.dispose();
		world.dispose();
		batch.dispose();
		Assets.dispose();
	}

	private void win() {
		Gdx.input.setInputProcessor(stage);
		bulb.win();
		camera.win();
		for (ColorBall ball : freeBalls) {
			ball.stopAnimationAndGoTo(bulb.b2Body);
		}
		world.setContactListener(new ContactAdapter() {
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Object a = contact.getFixtureA().getBody().getUserData();
				Object b = contact.getFixtureB().getBody().getUserData();
				if (a != null && a instanceof Bulb) {
					((ColorBall) b).toDie = true;
				} else if (b != null && b instanceof Bulb) {
					((ColorBall) a).toDie = true;
				}
			}
		});
	}

	public boolean try2Join(ColorBall sprite) {
		ArrayList<ColorBall> valids = getPotencialsJoints(sprite);
		if (valids.size() > 0) {
			for (ColorBall valid : valids) {
				sprite.join(valid);
			}
			fixColorBall(sprite);
			if (bulb.isConnected())
				win();
			return true;
		}
		return false;
	}

	public ArrayList<ColorBall> getPotencialsJoints(ColorBall sprite) {
		ArrayList<ColorBall> r = new ArrayList<ColorBall>();
		for (ColorBall ball : fixedBals) {
			float dist = ball.b2Body.getPosition().dst(sprite.b2Body.getPosition());
			// System.out.println(dist);
			if (dist < WocGame.MAX_NODE_DIST) {
				r.add(ball);
			}
			if (dist < WocGame.MIN_NODE_DIST) {
				r.clear();
				return r;
			}
		}
		int bulbBalls = countBulbBalls(r);
		if (r.size() < 2 || bulbBalls == 1 || bulbBalls == 2 && bulbBalls == r.size()) {
			r.clear();
		}
		return r;
	}

	private int countBulbBalls(ArrayList<ColorBall> balls) {
		int r = 0;
		for (ColorBall colorBall : balls) {
			r += bulb.isABulbBall(colorBall) ? 1 : 0;
		}
		return r;
	}

	private void resetLevel() {
		while (freeBalls.size() > 0) {
			freeBalls.get(0).dispose();
			freeBalls.remove(0);
		}
		for (ColorBall colorBall : fixedBals) {
			if (!bulb.isABulbBall(colorBall) && !powerStation.isABulbBall(colorBall)) {
				colorBall.dispose();
			}
		}
		fixedBals.clear();
		setupBalls();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new GestureDetector(camera), camera, dragProcessor));
	}

	@Override
	abstract public float getWidth();

	@Override
	abstract public float getHeight();

	@Override
	public World getWorld() {
		return world;
	}

}
