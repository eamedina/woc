package com.woe.game.tanks;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.woe.game.Assets;

public class Tank2 extends InputAdapter {
	// WHEELS_POS, WHEELS_RADIUS, CHASSIS_VERTICES depends from with for scale
	// (originally calculated for width 9). In addition WHEELS_POS need to be
	// relocated from (0,0) to (x,y)
	private static final float[] WHEELS_POS = { -3.6499999f, -1.3625f, -2.2499998f, -1.6f, -0.51250005f, -1.6f,
			1.2249999f, -1.6f, 2.3374996f, -0.9874999f };// width=9
	private static final float[] WHEELS_RADIUS = { .7f, 1f, 1f, 1f, .55f };// width=9
	private static final float[] CHASSIS_VERTICES = { -4.0125f, -0.5249999f, -3.5749998f, -1.7875f, 1.4125f, -1.8125f,
			2.6874998f, -0.38750005f, -0.29999998f, 2.35f, -1.3874997f, 2.5875f, -2.2999997f, 2.3f, -3.4749997f,
			0.5499999f };// width=9
	private static final int WHEELS = 5;
	
	private Body chassisBody;
	private Body cannon;
	Body[] wheelBodys;
	private WheelJoint[] axisses;
	private float motorSpeed = 25;
	Sprite chassisSprite;
	float height;
	final float  width;
	
	private final float[] wheelsPos;
	private final float[] wheelsRadius;
	private final float[] chassisVertices;

	public Tank2(World world, float x, float y) {
		width=3f;
		wheelsPos= adaptToWidth(WHEELS_POS);
		wheelsRadius=adaptToWidth(WHEELS_RADIUS);
		chassisVertices=adaptToWidth(CHASSIS_VERTICES);
		height=width*Assets.tank.getRegionHeight()/Assets.tank.getRegionWidth();
		initBox2D(world,  x, y, width, height);
		chassisSprite = new Sprite(Assets.tank);
		chassisSprite.setSize(width, height);
		chassisSprite.setOrigin(width / 2, height / 2);
	}


	private float[] adaptToWidth(float[] original) {
		float[] r= new float[original.length];
		for (int i = 0; i < original.length; i++) {
			r[i]=original[i]/9*width;
		}
		return r;
	}

	public void initBox2D(World world,  float x, float y, float width, float height) {
		FixtureDef chassisFixtureDef = new FixtureDef();
		chassisFixtureDef.density= .1f;
		chassisFixtureDef.friction= 1f;
		chassisFixtureDef.restitution= .3f;


		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
//		bodyDef.active=false;

		// chassis
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(chassisVertices);
		chassisFixtureDef.shape = chassisShape;
		chassisBody = world.createBody(bodyDef);
		chassisBody.createFixture(chassisFixtureDef);

		// wheels
		bodyDef.angularDamping = .9f;
		bodyDef.linearDamping = 5;
		FixtureDef wheelFixtureDef = new FixtureDef();
		wheelFixtureDef.density = .2f;
		wheelFixtureDef.friction=1;
		wheelFixtureDef.restitution=.4f;
		wheelFixtureDef.shape = new CircleShape();
		wheelFixtureDef.filter.groupIndex = -8;
		wheelBodys = new Body[WHEELS];
		for (int i = 0; i < wheelBodys.length; i++) {
			wheelFixtureDef.shape.setRadius(wheelsRadius[i]);
			bodyDef.position.set(wheelsPos[i * 2]+x, wheelsPos[i * 2 + 1]+y);
			wheelBodys[i] = world.createBody(bodyDef);
			wheelBodys[i].createFixture(wheelFixtureDef);
		}
		// left axis
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = chassisBody;
		axisDef.frequencyHz = 500;//chassisFixtureDef.density;
		axisDef.maxMotorTorque = 10;//chassisFixtureDef.density * 10;
		axisDef.localAxisA.set(Vector2.Y);
		axisses = new WheelJoint[wheelBodys.length];
		for (int i = 0; i < wheelBodys.length; i++) {
			axisDef.bodyB = wheelBodys[i];
			axisDef.localAnchorA.set(wheelsPos[i * 2],  wheelsPos[i * 2 + 1]);
			axisses[i] = (WheelJoint) world.createJoint(axisDef);
		}
	}

	
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.A:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(true);
				axisses[i].setMotorSpeed(motorSpeed);
			}
			break;
		case Keys.D:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(true);
				axisses[i].setMotorSpeed(-motorSpeed);
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.A:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(false);
			}
		case Keys.D:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(false);
			}
		}
		return true;
	}

	public Body getChassis() {
		return chassisBody;
	}

	public void draw(SpriteBatch batch) {
		updatePhicycs();
		chassisSprite.draw(batch);
	}
	private void updatePhicycs() {
		Vector2 pos = chassisBody.getPosition();
		chassisSprite.setPosition(pos.x - (width / 2), pos.y - (height / 2));
		chassisSprite.setRotation(chassisBody.getAngle() * MathUtils.radiansToDegrees);
	}

	public float getX() {
		return chassisBody.getPosition().x;
	}

	public float getY() {
		return chassisBody.getPosition().y;
	}
}