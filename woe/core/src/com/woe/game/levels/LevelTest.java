package com.woe.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.woe.game.Assets;
import com.woe.game.WoeCamera;
import com.woe.game.tanks.Tank2;

public class LevelTest implements Screen {

	private static final float VIEWPORT_WIDTH = 10;

	private SpriteBatch batch;
	private WoeCamera camera;
	private World world;
	private Box2DDebugRenderer renderer;
	Tank2 tank;

	@Override
	public void show() {
		Assets.load();
		world = new World(new Vector2(0, -9.8f), true);
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		camera = new WoeCamera();

		createGround();

		tank = new Tank2(world, 0, 0);
		camera.setTarget(tank);
		InputProcessor inputProcessor = new InputAdapter() {

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0));
				System.out.print(v.x + "f," + v.y + "f,");
				return false;
			}

		};
		Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor, tank));

	}

	float[] ground = {-14f,-1f, -4f,-1f,-2.6920125f,-0.0119102f,3.0945957f,0.038318038f,3.9691818f,-0.98595095f};
	
	private void createGround() {
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.StaticBody;

		ChainShape shape = new ChainShape();
		for (int i = 0; i < ground.length/2; i++) {
			ground[i*2+1]-=3;
		}
		shape.createChain(ground);

		

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 1f;
		fd.restitution = 0.5f;
		fd.shape = shape;

		world.createBody(bd).createFixture(fd);

		shape.dispose();
	}

	@Override
	public void render(float delta) {
		world.step(delta, 8, 3);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.target();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		tank.draw(batch);
		batch.end();
		renderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		camera.resize(width, height);
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
