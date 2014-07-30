package com.woe.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.woe.game.Assets;
import com.woe.game.NodeStructure;
import com.woe.game.WoeGame;
import com.woe.game.entities.Box2Sprite;
import com.woe.game.entities.FreeBall;
import com.woe.game.entities.WoeBackground;
import com.woe.game.tanks.BigBall;
import com.woe.game.tanks.Tank;

public class Level1 implements Screen {
	public static final float WORLD_WIDTH = 20;
	public static final float VIEWPORT_WIDTH = 10;
	public static final float WORLD_HEIGHT = 7.5f;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private World world;
	private Box2DDebugRenderer renderer;
	
	private WoeBackground background;
	NodeStructure nodeStructure;
	ArrayList<Box2Sprite> dropedSprites = new ArrayList<Box2Sprite>();
	private Box2Sprite draggedBall;
	
	private BigBall tank;

	@Override
	public void show() {
		Assets.load();
		world = new World(new Vector2(0, -9.8f), true);
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		background = new WoeBackground(world);
		nodeStructure = new NodeStructure(world);
		
//		tank = new Tank(world, -7f, 2f);
		tank = new BigBall(world,-7f, 2f,camera,background);

		InputAdapter inputProcessor = (new InputAdapter() {

			private Vector2 v2 = new Vector2();
			private Rectangle fingerRect= WoeGame.FINGER_RECT;
			private MouseJoint mouseJoint;
			private Vector3 v = new Vector3();
			private Vector3 unproject(int x, int y) {
				v.x = x; v.y = y;
				camera.unproject(v);
				System.out.print(v.x + "f," + v.y+ "f,");
				return v;
			}

			private Box2Sprite createDraggedBall(Box2Sprite box2Sprite) {
				MouseJointDef jointDef = new MouseJointDef();
				jointDef.bodyA = background.b2Body;
				jointDef.collideConnected = true;
				jointDef.maxForce = 500;
				jointDef.bodyB = box2Sprite.b2Body;
				jointDef.target.set(v.x,v.y);
				mouseJoint = (MouseJoint) world.createJoint(jointDef);
				return box2Sprite;
			}

			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				v = unproject(x, y);
				for (FreeBall ball : nodeStructure.freeSprites) {
					fingerRect.setCenter(v.x, v.y);
					if (ball.overlaps(fingerRect)) {
						nodeStructure.freeSprites.remove(ball);
						draggedBall = createDraggedBall(new Box2Sprite(world,nodeStructure, v.x, v.y, true));
						return true;
					}
				}
				for (Box2Sprite ball : dropedSprites) {
					if (ball.contains(v.x, v.y)) {
						draggedBall = createDraggedBall(ball);
						dropedSprites.remove(ball);
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean touchDragged(int x, int y, int pointer) {
				v = unproject(x, y);
				if (mouseJoint != null)	{
					mouseJoint.setTarget(v2.set(v.x, v.y));
					return true;
				}
				return false;
			}

			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				if (mouseJoint != null) {
					world.destroyJoint(mouseJoint);
					mouseJoint = null;
					v = unproject(x, y);
					if (!nodeStructure.try2Join(draggedBall)) {
						dropedSprites.add(draggedBall);
					}
					draggedBall = null;
					return true;
				}
				return false;
			}
		});
		
		GestureListener gestureListener = new GestureAdapter() {
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				float cameraToWorld = WORLD_WIDTH/ Gdx.graphics.getWidth();
				float camX = camera.position.x-deltaX*cameraToWorld;
				float camY = camera.position.y+deltaY*cameraToWorld;
				float maxY = (WORLD_HEIGHT-camera.viewportHeight)/2;
				float maxX = (WORLD_WIDTH-camera.viewportWidth)/2;
				camY = Math.max(Math.min(camY, maxY),-maxY);
				camX = Math.max(Math.min(camX, maxX),-maxX);
				camera.position.set(camX,camY,0);
				camera.update();
				return true;
			}
		};
		Gdx.input.setInputProcessor(new InputMultiplexer(tank,inputProcessor,new GestureDetector(gestureListener)));
	}


	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = VIEWPORT_WIDTH;
		camera.viewportHeight = VIEWPORT_WIDTH*height/width;
		camera.update();
	}

	@Override
	public void render(float delta) {
		world.step(delta, 8, 3);
		nodeStructure.update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		nodeStructure.draw(batch);
		for (Box2Sprite sprite : dropedSprites) {
			sprite.draw(batch);
		}

		if (draggedBall != null) {
			draggedBall.draw(batch);
		}
		tank.draw(batch);
		batch.end();
		renderer.render(world, camera.combined);
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
