package com.woe.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.woe.game.Assets;
import com.woe.game.WoeGame;

public class FreeBall {

	public float with = WoeGame.EDUS_WITH;
	private Sprite sprite;
	private Box2Sprite nodeFrom;
	private Box2Sprite nodeTarget;
	private float traveled;
	private float speed;
	protected Body b2Body;

	public FreeBall(Box2Sprite fixBallSprite) {
		this(fixBallSprite.getX(), fixBallSprite.getY());
		nodeFrom = fixBallSprite;
	}

	public FreeBall(float x, float y) {
		sprite = new Sprite(Assets.white);
		sprite.setColor(Color.BLUE);
		sprite.setPosition(x, y);
		sprite.setSize(with, with);
		sprite.setOrigin(with / 2, with / 2);
	}

	public void draw(Batch batch) {
		sprite.draw(batch);
	}

	public void update(float delta) {
		if (nodeTarget==null) {
			ArrayList<Box2Sprite> targets = nodeFrom.getNodeTargets();
			if (targets.size()==0) {
				throw new RuntimeException("No hay camino para el free node");
			}
			nodeTarget = targets.get((int) Math.floor((Math.random()*targets.size())));
			traveled = .0f;
			speed =  (float) (Math.random()*.5+.5);
		}
		traveled = (float) (traveled + delta*speed);
		if (traveled>=1) {
			sprite.setPosition(nodeTarget.getX(),nodeTarget.getY());
			nodeFrom = nodeTarget;
			nodeTarget=null;
		} else {
			Vector2 pos = nodeTarget.getPosition().sub(nodeFrom.getPosition());
			pos = nodeFrom.getPosition().add(pos.scl(traveled));  
			sprite.setPosition(pos.x, pos.y);
		}

	}


	public boolean contains(float x, float y) {
		return sprite.getBoundingRectangle().contains(x, y);
	}
	public boolean overlaps(Rectangle rect) {
		return sprite.getBoundingRectangle().overlaps(rect);
	}


	public void setPosition(float x, float y) {
		sprite.setPosition(x- (with / 2), y- (with / 2));
	}



	
	
}
