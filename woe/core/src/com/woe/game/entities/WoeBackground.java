package com.woe.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.woe.game.Assets;
import com.woe.game.levels.Level1;

public class WoeBackground {
	float[] relieve = { -15f,-3.5f,-15f, -0.14062503f, -3.0812502f, -0.12812501f, -2.4125001f, -0.20312499f, -2.14375f,
			-0.41562507f, -2.1062498f, -0.57187504f, -2.08125f, -0.6593751f, -1.8625f, -0.7593751f, -1.76875f,
			-0.90312505f, -1.74375f, -1.2781252f, -1.70625f, -1.378125f, -1.74375f, -1.6906251f, -1.70625f,
			-1.7531251f, -1.51875f, -1.8843752f, -1.0562501f, -2.5531251f, -0.9625f, -2.5968752f, -0.78750014f,
			-2.578125f, -0.61874986f, -2.246875f, -0.6125f, -2.246875f, -0.43125004f, -2.1093752f, -0.4249999f,
			-2.1093752f, -0.4249999f, -2.1156251f, -0.38749993f, -2.328125f, -0.20624995f, -2.5968752f, 0.012500286f,
			-2.5968752f, 0.012500286f, -2.590625f, 0.2125001f, -2.121875f, 0.2125001f, -2.1281252f, 0.38749993f,
			-2.3093753f, 0.57500005f, -2.415625f, 0.5812502f, -2.421875f, 0.5937499f, -2.421875f, 0.6f, -2.4281251f,
			0.6124997f, -2.4281251f, 0.7375002f, -2.421875f, 0.74374974f, -2.421875f, 1.0624999f, -2.2156253f,
			1.1374998f, -2.3468752f, 1.2312502f, -2.421875f, 1.3749999f, -2.421875f, 1.4499998f, -2.3468752f,
			1.5687501f, -1.8968751f, 1.7000002f, -1.7656251f, 1.8437499f, -1.7531251f, 1.9125003f, -1.6656251f,
			1.9062501f, -1.6656251f, 1.9187498f, -1.484375f, 1.9125003f, -1.484375f, 1.7000002f, -1.2718751f, 1.74375f,
			-1.1843752f, 1.9499999f, -1.1718751f, 1.9499999f, -1.1656251f, 1.9499999f, -1.153125f, 2.0125003f,
			-0.9906251f, 2.2125f, -0.5906251f, 2.3250003f, -0.49062505f, 2.5937498f, -0.43437496f, 3.1875002f,
			-0.44062498f, 3.1875002f, -0.43437496f, 3.2062502f, -0.4281251f, 3.3875f, -0.34062508f, 15f, -0.32812506f,
			15f,-3.5f};

	public Body b2Body;

	public WoeBackground(World world) {

		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		ChainShape shape = new ChainShape();
		shape.createChain(relieve);
//		PolygonShape shape = new PolygonShape();
//		shape.set(relieve);
				
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		b2Body = world.createBody(bd);
		b2Body.createFixture(fd);
		shape.dispose();
	}

	public void draw(Batch batch) {
		batch.disableBlending();
//		float w = Stage1.WORLD_WIDTH;
//		float h = Stage1.WORLD_WIDTH * Assets.stage1.getRegionHeight() / Assets.stage1.getRegionWidth();
		float w = Level1.WORLD_WIDTH;
		float h = Level1.WORLD_WIDTH * Assets.stage1.getRegionHeight() / Assets.stage1.getRegionWidth();
		float orgY = 0;
		float orgX = 0;
		// batch.draw(Assets.stage1,-viewportWidth/2,-(viewportWidth*h/w)/2,viewportWidth,viewportWidth*h/w);
		// t = new TextureRegion(region, x, y, width, height)
//		batch.draw(Assets.stage1, -w/2 , -h/2 , orgX, orgY, w, h, 1f, 1f, 0);
		batch.draw(Assets.stage1, -w/2 , -h/2 , w, h);
		batch.enableBlending();

	}
}
