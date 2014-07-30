package com.woc.game.etities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.woc.game.Assets;
import com.woc.game.levels.SimpleAbstractLevel;

public class PowerStation {
	private SimpleAbstractLevel level;
	public Body b2Body;
	private ColorBall leftBall;
	private ColorBall rightBall;
	private float w;
	private float h;

	public PowerStation(SimpleAbstractLevel level, float x, float y) {
		this.level = level;
		w = 3f;
		h = 2f;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(x, y);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w / 2, h / 2);

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		b2Body = level.world.createBody(bd);
		b2Body.createFixture(fd);
		shape.dispose();
		leftBall = new ColorBall(level, b2Body.getPosition().x - .5f, b2Body.getPosition().y + 1);
		rightBall = new ColorBall(level, b2Body.getPosition().x + .5f, b2Body.getPosition().y + 1);
	}

	public void draw(SpriteBatch batch) {
		leftBall.draw(batch);
		rightBall.draw(batch);
		batch.draw(Assets.powerstation, b2Body.getPosition().x - w / 2, b2Body.getPosition().y - h / 2, w, h);
	}

	public boolean isABulbBall(ColorBall colorBall) {
		return leftBall == colorBall || rightBall == colorBall;
	}

	public void addBalls(ArrayList<ColorBall> fixedBals) {
		fixedBals.add(leftBall);
		fixedBals.add(rightBall);
	}
}
