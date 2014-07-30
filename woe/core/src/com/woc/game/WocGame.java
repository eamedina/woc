package com.woc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.woc.game.levels.Menu;

public class WocGame extends Game {
	public static final float COLOR_WITH = .45f;
	public static final float MAX_NODE_DIST = COLOR_WITH * 4f;
	public static final float MIN_NODE_DIST = COLOR_WITH * 1.5f;
	public static final float JOINT_FREQUENCY = 9.0f; // Joint flexibility
	public static final Rectangle FINGER_RECT = new Rectangle(0f, 0f, .7f, .7f);
	public static final float MIN_COLOR_SPEED = .3f;
	public static final float MAX_COLOR_SPEED = MIN_COLOR_SPEED * 2;
	public static final int BALL_WEIGHT = 3;

	@Override
	public void create() {
		setScreen(new Menu(this));
	}

}
