package com.woe.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.woe.game.entities.Box2Sprite;
import com.woe.game.entities.FreeBall;
import com.woe.game.entities.Plataform;
import com.woe.game.entities.WoodPlank;
import com.woe.game.tanks.Tank;

public class NodeStructure {
	private static final float[] INIT = { -4f,0f,-4f,0.5f,-5f,0f};
	private static final float[] END = { 4f,-0.2f,4f,0.3f,5f,-0.2f};
	Plataform plataform[] = new Plataform[2];
	ArrayList<Box2Sprite> fixedSprites = new ArrayList<Box2Sprite>();
	public ArrayList<FreeBall> freeSprites = new ArrayList<FreeBall>();
	private ArrayList<WoodPlank>  woodPlanks = new ArrayList<WoodPlank>();
	private World world;
	

	public NodeStructure(World world) {
		this.world = world;
		plataform[0] = new Plataform(world, -4f-7f, 0.3f);
		createBase(world,INIT);
		plataform[1] = new Plataform(world, 4f+7f, 0.3f);
		createBase(world,END);
		for (int i = 0; i < 20; i++) {
			freeSprites.add(new FreeBall(fixedSprites.get(0)));
		}
		
	}

	private void createBase(World world, float[] nodes) {
		for (int i = 0; i < 3; i++) {
			Box2Sprite s = new Box2Sprite(world, this, nodes[i*2], nodes[i*2+1], false);
			fixedSprites.add(s);
			if (i >= 1)
				fixedSprites.get(fixedSprites.size() - 2).join(fixedSprites.get(fixedSprites.size() - 1));
		}
		fixedSprites.get(fixedSprites.size() - 3).join(fixedSprites.get(fixedSprites.size() - 1));
	}

	public void update(float delta) {
		for (FreeBall sprite : freeSprites) {
			sprite.update(delta);
		}
	}

	public void draw(Batch batch) {
		for (int i = 0; i < plataform.length; i++) {
			plataform[i].draw(batch);
		}
		for (Box2Sprite sprite : fixedSprites) {
			sprite.draw(batch);
		}
		for (WoodPlank woodPlank : woodPlanks) {
			woodPlank.draw(batch);
		}
		for (FreeBall sprite : freeSprites) {
			sprite.draw(batch);
		}
		
	}

	/**
	 * try to Join a node to the structure as free o fixed
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean try2Join(Box2Sprite sprite) {
		ArrayList<Box2Sprite> valids = getClosestFixNodes(sprite);
		if (enclosed(sprite, valids)) {
			freeSprites.add(new FreeBall(valids.get(0)));
			sprite.dispose();
			return true;
		}
		if (valids.size() >= 2 && nearest(sprite, valids) > WoeGame.MIN_NODE_DIST) {
			for (Box2Sprite valid : valids) {
				sprite.join(valid);
//				TODO wood plank addition
//				woodPlanks.add(new WoodPlank(world, sprite,valid));
			}
			fixedSprites.add(sprite);
			return true;
		}
		return false;
	}

	private float nearest(Box2Sprite sprite, ArrayList<Box2Sprite> valids) {
		float dist = Float.MAX_VALUE;
		for (Box2Sprite box2Sprite : valids) {
			dist = Math.min(dist, sprite.getPosition().dst(box2Sprite.getPosition()));
		}
		return dist;
	}

	/**
	 * return the nodes that potentially can be used to fix the sprite
	 * 
	 * @param sprite
	 * @return
	 */
	public ArrayList<Box2Sprite> getPotencialsJoints(Box2Sprite sprite) {
		ArrayList<Box2Sprite> valids = getClosestFixNodes(sprite);
		if (nearest(sprite, valids) < WoeGame.MIN_NODE_DIST || enclosed(sprite, valids)) {
			valids.clear();
		}
		return valids;
	}

	private boolean enclosed(Box2Sprite sprite, ArrayList<Box2Sprite> valids) {
		if (valids.size() < 3)
			return false;
		ArrayList<Vector2> vs = new ArrayList<Vector2>(valids.size());
		for (Box2Sprite box2Sprite : valids) {
			vs.add(box2Sprite.getPosition().sub(sprite.getPosition()));
		}
		Collections.sort(vs, new Comparator<Vector2>() {
			@Override
			public int compare(Vector2 o1, Vector2 o2) {
				return o1.angle() < o2.angle() ? -1 : 1;
			}
		});
		float[] vertices = new float[vs.size() * 2];
		for (int i = 0; i < vs.size(); i++) {
			vertices[i * 2] = vs.get(i).x;
			vertices[i * 2 + 1] = vs.get(i).y;
		}
		return new Polygon(vertices).contains(0, 0);
	}

	private ArrayList<Box2Sprite> getClosestFixNodes(Box2Sprite sprite) {
		ArrayList<Box2Sprite> r = new ArrayList<Box2Sprite>();
		for (Box2Sprite ball : fixedSprites) {
			float dist = ball.getPosition().dst(sprite.getPosition());
			if (dist < WoeGame.MAX_NODE_DIST && ball.b2Body.getJointList().size >= 1) {
				r.add(ball);
			}
		}
		return r;
	}

}
