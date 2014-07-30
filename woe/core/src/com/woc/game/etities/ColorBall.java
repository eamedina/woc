package com.woc.game.etities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Array;
import com.woc.game.Assets;
import com.woc.game.Sounds;
import com.woc.game.WocGame;
import com.woc.game.levels.Level;

public class ColorBall {

	static final Color[] COLORS = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
	static final Color[] FIXED_COLORS = { new Color(1, 0, 0, .3f), new Color(0, 1, 0, .3f), new Color(0, 0, 1, .3f) };
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int YELLOW =3;

	public float width = WocGame.COLOR_WITH;
	public Body b2Body;
	Sprite nodoSprite;
	Sprite branchSprite;
	Sprite blurSprite;
	public int keyColor = (int) (Math.random() * 3);

	private Level level;
	public boolean fixed;
	private Vector2 gravityCenter;
	public boolean toDie;

	public ColorBall(Level level,int keyColor) {
		this.keyColor=keyColor;
		this.level = level;
		float x = (float) (Math.random() * level.getWidth() * .8f) - level.getWidth() * .4f;
		float y = (float) (Math.random() * level.getHeight() * .8f) - level.getHeight()* .4f;
		createB2Body(x, y, true);
		createSprites();
	}

	public ColorBall(Level level, float x, float y) {
		keyColor = 3;
		this.level = level;
		createB2Body(x, y, false);
		createSprites();
		fixed = true;
	}

	private void createSprites() {
		nodoSprite = new Sprite(Assets.white);
		nodoSprite.setColor(COLORS[keyColor]);
		nodoSprite.setSize(width, width);
		nodoSprite.setOrigin(width / 2, width / 2);
		blurSprite = new Sprite(Assets.blurball);
		blurSprite.setColor(COLORS[keyColor]);
		blurSprite.setSize(width * 4, width * 4);
		blurSprite.setOrigin(width / 2, width / 2);
		branchSprite = new Sprite(Assets.beam);
		branchSprite.setColor(Color.WHITE);
		branchSprite.setSize(width * 2, width / 2);
		branchSprite.setOrigin(0, branchSprite.getHeight() / 2);
	}

	private void createB2Body(float x, float y, boolean dynamic) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		bodyDef.gravityScale = 0;
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(width / 2);
		b2Body = level.getWorld().createBody(bodyDef);

		FixtureDef fixt = new FixtureDef();
		// fixt.filter.groupIndex = -10;
		fixt.density = 1;
		fixt.shape = ballShape;
		fixt.restitution = 1F;
		fixt.friction = .1f;
		b2Body.createFixture(fixt);
		ballShape.dispose();
		b2Body.setUserData(this);
	}

	public void draw(Batch batch) {
		updatePhicycs();
		drawJoints(batch);
		if (amIDragged())
			blurSprite.draw(batch);
		nodoSprite.draw(batch);
	}

	private void drawJoints(Batch batch) {
		Array<JointEdge> joints = b2Body.getJointList();
		for (JointEdge jointEdge : joints) {
			if ((jointEdge.joint instanceof MouseJoint)) {
				ArrayList<ColorBall> nodes = level.getPotencialsJoints(this);
				for (ColorBall colorBall : nodes) {
					drawBeam(batch, colorBall.b2Body);
				}
				if (nodes.size() > 0)
					Sounds.startBeam();
				else
					Sounds.stopBeam();
			} else {
				Body targetBody = jointEdge.joint.getBodyA();
				if (targetBody == b2Body) {
					targetBody = jointEdge.joint.getBodyB();
				}
				drawBeam(batch, targetBody);
			}
		}
	}

	private void drawBeam(Batch batch, Body targetBody) {
		branchSprite.setPosition(b2Body.getPosition().x, b2Body.getPosition().y - (width / 4));
		Vector2 v = targetBody.getPosition().sub(b2Body.getPosition());
		branchSprite.setRotation((float) v.angle());
		branchSprite.setSize(v.len(), MathUtils.random(.1f, .2f));
		branchSprite.draw(batch);
	}

	private void updatePhicycs() {
		if (gravityCenter != null) {
			Vector2 force = new Vector2(gravityCenter).sub(b2Body.getPosition());
			b2Body.applyForceToCenter(force.scl(.15f), true);
		} else {
			if (!fixed && !amIDragged()) {
				if (b2Body.getLinearVelocity().len() < WocGame.MIN_COLOR_SPEED) {
					float yImp = (float) (Math.random() * 2 * WocGame.MAX_COLOR_SPEED - WocGame.MAX_COLOR_SPEED);
					float xImp = (float) (Math.random() * 2 * WocGame.MAX_COLOR_SPEED - WocGame.MAX_COLOR_SPEED);
					b2Body.applyLinearImpulse(new Vector2(xImp, yImp), b2Body.getPosition(), true);
				}
			}
		}
		Vector2 pos = b2Body.getPosition();
		nodoSprite.setPosition(pos.x - (width / 2), pos.y - (width / 2));
		nodoSprite.setRotation(b2Body.getAngle() * MathUtils.radiansToDegrees);
		if (blurSprite != null)
			blurSprite.setPosition(pos.x - (width * 2), pos.y - (width * 2));
	}

	private boolean amIDragged() {
		Array<JointEdge> joints = b2Body.getJointList();
		for (JointEdge jointEdge : joints)
			if (jointEdge.joint instanceof MouseJoint)
				return true;
		return false;
	}

	public boolean overlaps(Rectangle rect) {
		return nodoSprite.getBoundingRectangle().overlaps(rect);
	}

	public float getX() {
		return b2Body.getPosition().x;
	}

	public float getY() {
		return b2Body.getPosition().y;
	}

	public void join(ColorBall s2) {
		fixed = true;
		b2Body.setGravityScale((keyColor-1)*WocGame.BALL_WEIGHT);
		DistanceJointDef jd = new DistanceJointDef();
		jd.frequencyHz = WocGame.JOINT_FREQUENCY;
		jd.bodyA = b2Body;
		jd.bodyB = s2.b2Body;
		jd.length = b2Body.getPosition().dst(s2.b2Body.getPosition());
		level.getWorld().createJoint(jd);
	}

	public void stopAnimationAndGoTo(Body body) {
		gravityCenter = body.getPosition();
		b2Body.setLinearVelocity(b2Body.getLinearVelocity().scl(0.5f));
		b2Body.getFixtureList().get(0).setRestitution(0);
	}

	public void dispose() {
		Array<JointEdge> list = b2Body.getJointList();
		while (list.size > 0) {
			level.getWorld().destroyJoint(list.get(0).joint);
		}
		if (b2Body.getType() == BodyType.StaticBody) {
			System.out.println();
		}
		level.getWorld().destroyBody(b2Body);
	}

	public void setColor(Color color) {
		nodoSprite.setColor(color);
	}
}
