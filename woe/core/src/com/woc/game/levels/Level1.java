package com.woc.game.levels;

import com.woc.game.WocGame;
import com.woc.game.etities.Bulb;
import com.woc.game.etities.ColorBall;
import com.woc.game.etities.PowerStation;

public class Level1 extends SimpleAbstractLevel{
	public Level1(WocGame wocGame) {
		super(wocGame);
	}

	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 14f;
	private static final int NUM_BALLS = 20;
	

	@Override
	void setEntities() {
		powerStation = new PowerStation(this, 0, -6f);
		bulb = new Bulb(this, 0f, +5f);
	}

	@Override
	void createColorBalls() {
		for (int i = 0; i < NUM_BALLS; i++)
			freeBalls.add(new ColorBall(this,ColorBall.GREEN));
	}
	@Override
	public float getWidth() {
		return WORLD_WIDTH;
	}

	@Override
	public float getHeight() {
		return WORLD_HEIGHT;
	}
}
