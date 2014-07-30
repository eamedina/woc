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

public class Tank3 extends InputAdapter {
	private static final float[] WHEELS_POS = { -1.2187499f, 0.3468751f, -0.74375004f, 0.29062515f, -0.18124998f,
		0.27812514f, 0.4000002f, 0.30937505f, 0.78125f, 0.45312515f };
	private static final float[] WHEELS_RADIUS = {.24f,.28f,.28f,.28f,.20f };
	private static final int WHEELS = 5;
	private Body chassisBody;
	private Body cannon;
	Body[] wheelBodys;
	private WheelJoint[] axisses;
	private float motorSpeed = 25;
	Sprite chassisSprite;
	Sprite tankSprite;
	float height;
	float width;

	public Tank3(World world, float x, float y) {
		 height=1f;
		 width=2f;
		initBox2D(world,  x, y, width, height);
		width = width*1.1f;
		height = height*1.1f;
		chassisSprite = new Sprite(Assets.tankchassis);
		chassisSprite.setSize(width, height);
		chassisSprite.setOrigin(width / 2, height / 2);
	}
	
	public void initBox2D(World world,  float x, float y, float width, float height) {
		FixtureDef chassisFixtureDef = new FixtureDef();
		chassisFixtureDef.density= 20;
		chassisFixtureDef.friction= .4f;
		chassisFixtureDef.restitution= .3f;


		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
//		bodyDef.active=false;

		// chassis
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(new float[] { -1.3249998f, -0.40000004f, -0.31249997f, -0.76249987f, 0.91249996f,
				-0.37500006f, 0.1999998f, -0.012499987f, 0.0999999f, 0.30000013f, -0.29999998f, 0.49999994f,
				-0.7499998f, 0.46249998f, -0.98749983f, 0.112499885f }); 
		chassisFixtureDef.shape = chassisShape;
		chassisBody = world.createBody(bodyDef);
		chassisBody.createFixture(chassisFixtureDef);

		// wheels
		bodyDef.angularDamping = .9f;
		bodyDef.linearDamping = 5;
		FixtureDef wheelFixtureDef = new FixtureDef();
		wheelFixtureDef.density = 50;
		wheelFixtureDef.friction=1;
		wheelFixtureDef.restitution=.4f;
		wheelFixtureDef.shape = new CircleShape();
		wheelFixtureDef.filter.groupIndex = -8;
		wheelBodys = new Body[WHEELS];
		for (int i = 0; i < wheelBodys.length; i++) {
			wheelFixtureDef.shape.setRadius(WHEELS_RADIUS[i]);
			bodyDef.position.set(WHEELS_POS[i * 2], WHEELS_POS[i * 2 + 1]);
			wheelBodys[i] = world.createBody(bodyDef);
			wheelBodys[i].createFixture(wheelFixtureDef);
		}
		// left axis
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = chassisBody;
		axisDef.frequencyHz = 500;//chassisFixtureDef.density;
		axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
		axisDef.localAxisA.set(Vector2.Y);
		axisses = new WheelJoint[wheelBodys.length];
		for (int i = 0; i < wheelBodys.length; i++) {
			axisDef.bodyB = wheelBodys[i];
			axisDef.localAnchorA.set(WHEELS_POS[i * 2],  WHEELS_POS[i * 2 + 1]-1);
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
		chassisSprite.setPosition(pos.x - (width / 2), pos.y - (height / 1.5f));
		chassisSprite.setRotation(chassisBody.getAngle() * MathUtils.radiansToDegrees);
	}

	public float getX() {
		return chassisBody.getPosition().x;
	}

	public float getY() {
		return chassisBody.getPosition().y;
	}
}