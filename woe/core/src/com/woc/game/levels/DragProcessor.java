package com.woc.game.levels;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.woc.game.Sounds;
import com.woc.game.WocGame;
import com.woc.game.etities.ColorBall;

public class DragProcessor extends InputAdapter {

	private MouseJoint mouseJoint;
	private Vector3 v = new Vector3();
	private Vector2 v2 = new Vector2();
	private Rectangle fingerRect = WocGame.FINGER_RECT;
	private SimpleAbstractLevel level;

	public DragProcessor(SimpleAbstractLevel level) {
		this.level = level;
	}
	
	private Vector3 unproject(int x, int y) {
		v.x = x;
		v.y = y;
		level.camera.unproject(v);
		// System.out.print(v.x + "f," + v.y + "f,");
		return v;
	}

	private ColorBall createDraggedBall(ColorBall ball) {
		MouseJointDef jointDef = new MouseJointDef();
		jointDef.bodyA = level.background.b2Body;
		jointDef.collideConnected = true;
		jointDef.maxForce = 50000;
		jointDef.bodyB = ball.b2Body;
		jointDef.target.set(v.x, v.y);
		mouseJoint = (MouseJoint) level.world.createJoint(jointDef);
		return ball;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (mouseJoint != null)
			return false;
		v = unproject(x, y);
		System.out.println("x,y=" + v.x + "," + v.y);
		for (final ColorBall ball : level.freeBalls) {
			fingerRect.setCenter(v.x, v.y);
			if (ball.overlaps(fingerRect)) {
				level.camera.setTarget(ball);
				ball.b2Body.setTransform(v.x, v.y, 0);
				level.draggedBall = createDraggedBall(ball);
				Sounds.startSelected();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		v = unproject(x, y);
		if (mouseJoint != null) {
			mouseJoint.setTarget(v2.set(v.x, v.y));
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (mouseJoint != null) {
			level.world.destroyJoint(mouseJoint);
			mouseJoint = null;
			level.camera.removeTarget();
			level.try2Join(level.draggedBall);
			Sounds.stopSelected();
			Sounds.stopBeam();
			level.draggedBall = null;
			return true;
		}
		return false;
	}

}
