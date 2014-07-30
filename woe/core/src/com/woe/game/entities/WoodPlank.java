package com.woe.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.woe.game.Assets;
import com.woe.game.WoeGame;

public class WoodPlank {
	public Body b2Body;
	Sprite sprite;
	private float width;
	private float height;

	public WoodPlank(World world, Box2Sprite sprite1, Box2Sprite sprite2) {
		width =sprite1.getPosition().dst(sprite2.getPosition()) - WoeGame.EDUS_WITH * 1.2f;
		height = width * Assets.woodplank.getRegionHeight() / Assets.woodplank.getRegionWidth();
		sprite = new Sprite(Assets.woodplank);
		sprite.setSize(width, height);
		sprite.setOrigin(width / 2, height / 2);
		createB2Body(world, sprite1, sprite2);
	}

	private void createB2Body(World world, Box2Sprite sprite1, Box2Sprite sprite2) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(sprite1.getX(), sprite1.getY());
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.angularDamping = .1f;
		bodyDef.linearDamping = .1f;
//		bodyDef.angle = new Vector2(1,1).getAngleRad();
		// ball
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2, height/2);

		b2Body = world.createBody(bodyDef);

		FixtureDef fixt = new FixtureDef();
		// fixt.filter.groupIndex = -8;
		fixt.density = 1;
		fixt.shape = shape;
		fixt.restitution = 0F;
		fixt.friction = .1f;
		b2Body.createFixture(fixt);
		shape.dispose();
		b2Body.setUserData(this);
		// TODO crear joints para tabla
		Vector2 v1 = new Vector2( b2Body.getPosition().x-width/2,b2Body.getPosition().y);
		Vector2 v2 = new Vector2( b2Body.getPosition().x+width/2,b2Body.getPosition().y);
		if (sprite1.getPosition().dst(v1)<sprite1.getPosition().dst(v2)) {
			createJoint(world, sprite1,-width/2);
			createJoint(world, sprite2,width/2);
		}else {
			createJoint(world, sprite1,width/2);
			createJoint(world, sprite2,-width/2);
		}
		

	}

	private void createJoint(World world, Box2Sprite sprite, float xAnchor) {
		RevoluteJointDef jd = new RevoluteJointDef();
		jd.bodyA = b2Body;
		jd.localAnchorA.set(xAnchor, 0);
		jd.localAnchorB.set(0, 0);
		jd.bodyB = sprite.b2Body;
//		jd.maxLength = .15f;// b2Body.getPosition().dst(sprite.b2Body.getPosition());
		world.createJoint(jd);
	}

	public void draw(Batch batch) {
		updatePhicycs();
		sprite.draw(batch);
	}

	private void updatePhicycs() {
		Vector2 pos = b2Body.getPosition();
		sprite.setPosition(pos.x - (width / 2), pos.y - (height / 2));
		sprite.setRotation(b2Body.getAngle() * MathUtils.radiansToDegrees);
	}
}
