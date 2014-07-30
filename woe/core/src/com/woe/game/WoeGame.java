package com.woe.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.woe.game.levels.Level1;

public class WoeGame extends Game {
	public static final float EDUS_WITH= .35f;
	public static final float MAX_NODE_DIST = EDUS_WITH *4f;
	public static final float MIN_NODE_DIST = EDUS_WITH * 1.5f;
	public static final float JOINT_FREQUENCY = 20.0f; // Joint flexibility
	public static final Rectangle FINGER_RECT= new Rectangle(0f,0f, .9f, .9f);
	
	@Override
	public void create () {
		setScreen(new Level1());
	}

}
