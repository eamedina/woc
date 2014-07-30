package com.woe.game.tanks;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.woe.game.Assets;
import com.woe.game.NodeStructure;
import com.woe.game.WoeGame;
import com.woe.game.entities.Box2Sprite;
import com.woe.game.entities.FreeBall;
import com.woe.game.entities.WoeBackground;

public class BigBall extends InputAdapter{

	private Body body;
	private Sprite sprite;
	private float width=2f;
	private OrthographicCamera camera;
	private World world;

	public BigBall(World world, float x, float y, OrthographicCamera camera, WoeBackground background) {
		this.camera = camera;
		this.background = background;
		this.world = world;
		initBox2D(world,  x, y);
		sprite = new Sprite(Assets.woe3);
		sprite.setSize(width, width);
		sprite.setOrigin(width / 2, width / 2);
	}
	
	public void initBox2D(World world,  float x, float y) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density= 10;
		fixtureDef.friction= .4f;
		fixtureDef.restitution= .3f;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// chassis
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(width / 2);

		fixtureDef.shape = ballShape;

		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		
	}


	public void draw(SpriteBatch batch) {
		updatePhicycs();
		sprite.draw(batch);
	}
	private void updatePhicycs() {
		Vector2 pos = body.getPosition();
		sprite.setPosition(pos.x - (width / 2), pos.y - (width / 2f));
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}
	
	
	private Vector3 v = new Vector3();
	private Vector2 v2 = new Vector2();
	private Rectangle fingerRect= WoeGame.FINGER_RECT;
	private MouseJoint mouseJoint;
	private NodeStructure nodeStructure;
	private WoeBackground background;
	private Object draggedBall;


	private Vector3 unproject(int x, int y) {
		v.x = x; v.y = y;
		camera.unproject(v);
		System.out.print(v.x + "f," + v.y+ "f,");
		return v;
	}

	private BigBall createDraggedBall(BigBall box2Sprite) {
		MouseJointDef jointDef = new MouseJointDef();
		jointDef.bodyA = background.b2Body;
		jointDef.collideConnected = true;
		jointDef.maxForce = 500;
		jointDef.bodyB = body;
		jointDef.target.set(v.x,v.y);
		mouseJoint = (MouseJoint) world.createJoint(jointDef);
		return box2Sprite;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		v = unproject(x, y);
		if (sprite.getBoundingRectangle().overlaps(fingerRect)) {
			draggedBall = createDraggedBall(this);
			return true;
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
			draggedBall = null;
			return true;
		}
		return false;
	}


	
}