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

public class Tank extends InputAdapter {

	private Body chassisBody;
	Body[] wheelBodys;
	private WheelJoint[] axisses;
	private float motorSpeed = 25;
	Sprite chassisSprite;
	float height;
	float width;

	public Tank(World world, float x, float y) {
		 height=1f;
		 width=2f;
		initBox2D(world,  x, y, width, height);
		width = width*1.5f;
		height = height*1.5f;
		chassisSprite = new Sprite(Assets.tank);
		chassisSprite.setSize(width, height);
		chassisSprite.setOrigin(width / 2, height / 2);
	}
	
	public void initBox2D(World world,  float x, float y, float width, float height) {
		FixtureDef chassisFixtureDef = new FixtureDef();
		chassisFixtureDef.density= 1;
		chassisFixtureDef.friction= .4f;
		chassisFixtureDef.restitution= .3f;
		FixtureDef wheelFixtureDef = new FixtureDef();
		wheelFixtureDef.density = 2;
		wheelFixtureDef.friction=1;
		wheelFixtureDef.restitution=.4f;
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// chassis
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(new float[] {
				-width / 2, -height / 2,
				width / 2, -height / 2,
				width / 2 * 1.4f, height / 2,
				-width / 2 , height / 2 * .8f}); // counterclockwise order

		chassisFixtureDef.shape = chassisShape;

		chassisBody = world.createBody(bodyDef);
		chassisBody.createFixture(chassisFixtureDef);

		// wheels
		CircleShape wheelShape = new CircleShape();
		
		wheelFixtureDef.shape = wheelShape;
		wheelBodys = new Body[6];
		for (int i = 0; i < wheelBodys.length; i++) {
			if (i==0 ||i==3) {
				wheelFixtureDef.shape.setRadius(.18f);
			} else {
				wheelFixtureDef.shape.setRadius(.18f);
			}
			wheelBodys[i] = world.createBody(bodyDef);
			wheelBodys[i].createFixture(wheelFixtureDef);
		}
		wheelShape.setRadius(.18f);
		// left axis
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = chassisBody;
		axisDef.frequencyHz = 10;//chassisFixtureDef.density;
		axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
		axisDef.localAxisA.set(Vector2.Y);
		axisses = new WheelJoint[wheelBodys.length];
		float[] ys={-0.625f,-0.625f,-0.625f,-0.625f,-0.625f,-0.45f};
		for (int i = 0; i < wheelBodys.length; i++) {
			axisDef.bodyB = wheelBodys[i];
//			axisDef.localAnchorA.set(-width / 2 * .75f + wheelShape.getRadius()*(i*2-1), -height / 2 * 1.25f);
			axisDef.localAnchorA.set(-width / 2 * .75f + wheelShape.getRadius()*(i*2-1),ys[i]);
			axisses[i] = (WheelJoint) world.createJoint(axisDef);
		}
	}

	
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.D:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(true);
				axisses[i].setMotorSpeed(-motorSpeed);
			}
			break;
		case Keys.A:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(true);
				axisses[i].setMotorSpeed(motorSpeed);
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.D:
			for (int i = 0; i < axisses.length; i++) {
				axisses[i].enableMotor(false);
			}
		case Keys.A:
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
}