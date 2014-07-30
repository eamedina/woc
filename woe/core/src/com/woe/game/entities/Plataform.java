package com.woe.game.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Plataform {
	public Body b2Body;
	float x, y;
	float height = 1;
	float width = 14;
	private Texture texture;

	public Plataform(World world, float x, float y) {
		this.x = x;
		this.y = y;
		createProceduralPixmap();
		createB2Body(world);
	}

	private void createB2Body(World world) {
		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2, height/2);
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		b2Body = world.createBody(bd);
//		b2Body.createFixture(fd);
		
		shape.setAsBox(width/14, height*10);
		fd.shape = shape;
		b2Body.createFixture(fd);
		shape.dispose();
	}

	public void draw(Batch batch) {
//		batch.draw(texture, x-width/2, y-height/2, width, height);
	}

	private void createProceduralPixmap() {
		Pixmap pixmap = new Pixmap(10, 10, Format.RGBA8888);
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		// pixmap.drawLine(0, 0, width, height);
		// pixmap.drawLine(width, 0, 0, height);
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		// pixmap.drawRectangle(0, 0, width, height);
		texture = new Texture(pixmap);
	}
}
