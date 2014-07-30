package com.woc.game.etities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.woc.game.levels.SimpleAbstractLevel;
import com.woc.game.Assets;

public class Bulb {
	private SimpleAbstractLevel level;
	public Body b2Body;
	private ColorBall leftBall;
	private ColorBall rightBall;
	private float w;
	private float h;
	private boolean win;
	
	public Bulb(SimpleAbstractLevel level, float x, float y) {
		this.level =level;
		w = 2f;
		h= 3f;
		createB2Body(x,y,w,h);
		leftBall = new ColorBall(level, b2Body.getPosition().x-.5f, b2Body.getPosition().y-1.25f);
		rightBall = new ColorBall(level, b2Body.getPosition().x+.5f, b2Body.getPosition().y-1.25f);
	}

	
	private void createB2Body(float x,float y , float w, float h) {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(x,y);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w/2, h/2);
				
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		b2Body = level.world.createBody(bd);
		b2Body.createFixture(fd);
		shape.dispose();
		b2Body.setUserData(this);
	}

	public void draw(SpriteBatch batch) {
		if (win) {
			batch.draw(Assets.blurball, b2Body.getPosition().x-w, b2Body.getPosition().y-h,w*2,h*2);
		}
		leftBall.draw(batch);
		rightBall.draw(batch);
		batch.draw(Assets.bulb, b2Body.getPosition().x-w/2, b2Body.getPosition().y-h/2,w,h );
	}


	public boolean isABulbBall(ColorBall colorBall) {
		return leftBall==colorBall || rightBall==colorBall;
	}


	public boolean isConnected() {
		return leftBall.b2Body.getJointList().size>0;
	}


	public void win() {
		win = true;
	}

	public void addBalls(ArrayList<ColorBall> fixedBals) {
		fixedBals.add(leftBall);
		fixedBals.add(rightBall);
	}
}
