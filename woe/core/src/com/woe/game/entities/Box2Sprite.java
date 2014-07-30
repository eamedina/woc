package com.woe.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Array;
import com.woe.game.Assets;
import com.woe.game.NodeStructure;
import com.woe.game.WoeGame;

public class Box2Sprite {
	
	public float width = WoeGame.EDUS_WITH;
	public Body b2Body;
	Sprite nodoSprite;
	Sprite branchSprite;
	private World world;
	private NodeStructure nodeStructure;

	// private Vector2 b2Origin;

	public Box2Sprite(World world, NodeStructure nodeStructure, float x, float y, boolean dynamic) {
		createB2Body(world,nodeStructure, x, y, dynamic);
		nodoSprite = new Sprite(Assets.woe1);
		nodoSprite.setSize(width, width);
		nodoSprite.setOrigin(width / 2, width / 2);
		branchSprite = new Sprite(Assets.branch);
		branchSprite.setSize(width * 2, width / 2);
		branchSprite.setOrigin(0, branchSprite.getHeight() / 2);
	}

	private void createB2Body(World world, NodeStructure nodeStructure, float x, float y, boolean dynamic) {
		this.world = world;
		this.nodeStructure = nodeStructure;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;

		bodyDef.angularDamping = .1f;
		bodyDef.linearDamping = .1f;
		 bodyDef.fixedRotation =true;
		// ball
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(width / 2);

		b2Body = world.createBody(bodyDef);

		FixtureDef fixt = new FixtureDef();
		 fixt.filter.groupIndex = -10;
		fixt.density = 1;
		fixt.shape = ballShape;
		fixt.restitution = 0F;
		fixt.friction = .1f;
		b2Body.createFixture(fixt);
		ballShape.dispose();
		b2Body.setUserData(this);
	}

	public void draw(Batch batch) {
		updatePhicycs();
		drawJoints(batch);
		nodoSprite.draw(batch);
	}

	private void drawJoints(Batch batch) {
		branchSprite.setColor(Color.YELLOW);
		Array<JointEdge> joints = b2Body.getJointList();
		for (JointEdge jointEdge : joints) {
			if ((jointEdge.joint instanceof MouseJoint)) {
				branchSprite.setColor(Color.WHITE);
				ArrayList<Box2Sprite> nodes = nodeStructure.getPotencialsJoints(this);
				for (Box2Sprite box2Sprite : nodes) {
					Vector2 v =box2Sprite.getPosition().sub(getPosition());
					branchSprite.setRotation((float) v.angle());
					branchSprite.setSize(v.len() , branchSprite.getHeight());
					branchSprite.draw(batch);
				}
			}else {
				Body targetBody = jointEdge.joint.getBodyA();
				if (targetBody == b2Body) {
					targetBody = jointEdge.joint.getBodyB();
				}
				Vector2 v = targetBody.getPosition().sub(b2Body.getPosition());
				branchSprite.setRotation((float) v.angle());
				branchSprite.setSize(v.len() / 2, branchSprite.getHeight());
				branchSprite.draw(batch);				
			}
		}
	}

	private void updatePhicycs() {
		Vector2 pos = b2Body.getPosition();
		nodoSprite.setPosition(pos.x - (width / 2), pos.y - (width / 2));
		nodoSprite.setRotation(b2Body.getAngle() * MathUtils.radiansToDegrees);
		branchSprite.setPosition(pos.x, pos.y - (width / 4));
	}

	public void setPosition(float x, float y) {
		b2Body.setTransform(x, y, b2Body.getAngle());
		b2Body.setAwake(true);
	}

	public void join(Box2Sprite s2) {
		DistanceJointDef jd = new DistanceJointDef();
		jd.frequencyHz = WoeGame.JOINT_FREQUENCY;
		jd.bodyA = b2Body;
		jd.bodyB = s2.b2Body;
		jd.length = b2Body.getPosition().dst(s2.b2Body.getPosition());
		world.createJoint(jd);
	}

	public float getX() {
		return b2Body.getPosition().x - (width / 2);
	}

	public float getY() {
		return b2Body.getPosition().y - (width / 2);
	}

	public Vector2 getPosition() {
		return b2Body.getPosition().sub((width / 2), (width / 2));
	}

	public ArrayList<Box2Sprite> getNodeTargets() {
		ArrayList<Box2Sprite> r = new ArrayList<Box2Sprite>();
		Array<JointEdge> joints = b2Body.getJointList();
		for (JointEdge jointEdge : joints) {
			Body targetBody = jointEdge.joint.getBodyA();
			if (targetBody == b2Body) {
				targetBody = jointEdge.joint.getBodyB();
			}
			if (targetBody.getUserData() instanceof  Box2Sprite) {
				r.add((Box2Sprite) targetBody.getUserData());
			}
		}
		return r;
	}

	public boolean contains(float x, float y) {
		return nodoSprite.getBoundingRectangle().contains(x, y);
	}

	public void dispose() {
		world.destroyBody(b2Body);
	}
}
