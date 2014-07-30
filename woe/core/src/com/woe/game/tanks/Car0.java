package com.woe.game.tanks;

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

public class Car0 {

	private Body m_car;
	private Body m_wheel1;
	private Body m_wheel2;
	private float m_hz;
	private float m_zeta;
	private WheelJoint m_spring1;
	private WheelJoint m_spring2;
	private float y;
	private float x;
	
	public Car0(World m_world, float x, float y) {
		this.x = x;
		this.y = y;
		initBox2d(m_world);
	}

	private void initBox2d(World m_world) {

	      PolygonShape chassis = new PolygonShape();
	      Vector2 vertices[] = new Vector2[6];
	      vertices[0] = new Vector2(-1.5f, -0.5f);
	      vertices[1] = new Vector2(1.5f, -0.5f);
	      vertices[2] = new Vector2(1.5f, 0.0f);
	      vertices[3] = new Vector2(0.0f, 0.9f);
	      vertices[4] = new Vector2(-1.15f, 0.9f);
	      vertices[5] = new Vector2(-1.5f, 0.2f);
	      chassis.set(vertices);

	      CircleShape circle = new CircleShape();
	      circle.setRadius(0.4f);//0.4f);

	      BodyDef bd = new BodyDef();
	      bd.type = BodyType.DynamicBody;
	      bd.position.set(0.0f+x, 1.0f+y);
	      m_car = m_world.createBody(bd);
	      m_car.createFixture(chassis, .1f);//1.0f);

	      FixtureDef fd = new FixtureDef();
	      fd.shape = circle;
	      fd.density = 1.0f;
	      fd.friction = 0.9f;

	      bd.position.set(-1.0f+x, 0.35f+y);
	      m_wheel1 = m_world.createBody(bd);
	      m_wheel1.createFixture(fd);

	      bd.position.set(1.0f+x, 0.4f+y);
	      m_wheel2 = m_world.createBody(bd);
	      m_wheel2.createFixture(fd);

	      WheelJointDef jd = new WheelJointDef();
	      Vector2 axis = new Vector2(0.0f, 1.0f);

	      jd.initialize(m_car, m_wheel1, m_wheel1.getPosition(), axis);
	      jd.motorSpeed = 0.0f;
	      jd.maxMotorTorque = 20.0f;
	      jd.enableMotor = true;
	      jd.frequencyHz = m_hz;
	      jd.dampingRatio = m_zeta;
	      m_spring1 = (WheelJoint) m_world.createJoint(jd);

	      jd.initialize(m_car, m_wheel2, m_wheel2.getPosition(), axis);
	      jd.motorSpeed = 0.0f;
	      jd.maxMotorTorque = 10.0f;
	      jd.enableMotor = false;
	      jd.frequencyHz = m_hz;
	      jd.dampingRatio = m_zeta;
	      m_spring2 = (WheelJoint) m_world.createJoint(jd);
	}
}
