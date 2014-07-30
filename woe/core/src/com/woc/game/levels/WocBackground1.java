package com.woc.game.levels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class WocBackground1 {
//	float[] relieve = { -4.5f,-3.5f, 4.5f,-3.5f, 4.5f,3.5f, -4.5f,3.5f };
	

	public Body b2Body;

	public WocBackground1(Level level) {
		float w = level.getWidth()/2-.1f;
		float h= level.getHeight()/2-.1f;
		float[] relieve = {  -w ,-h, w,-h, w,h, -w,h, -w,-h };
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		ChainShape shape = new ChainShape();
		shape.createChain(relieve);
				
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		b2Body = level.getWorld().createBody(bd);
		b2Body.createFixture(fd);
		shape.dispose();
	}

	public void draw(Batch batch) {
//		batch.disableBlending();
//		float w = Level1.WORLD_WIDTH;
//		float h = Level1.WORLD_WIDTH * Assets.stage1.getRegionHeight() / Assets.stage1.getRegionWidth();
//		batch.draw(Assets.stage1, -w/2 , -h/2 , w, h);
//		batch.enableBlending();
	}
}
