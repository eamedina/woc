package com.woc.game.levels;

import com.woc.game.WocGame;
import com.woc.game.etities.Bulb;
import com.woc.game.etities.ColorBall;
import com.woc.game.etities.PowerStation;

public class Level3 extends SimpleAbstractLevel{
	public Level3(WocGame wocGame) {
		super(wocGame);
	}

	public static final float WORLD_WIDTH = 14;
	public static final float WORLD_HEIGHT = 10f;
	private static final int RED_BALLS = 15;
	private static final int BLUE_BALLS = 15;
	
	void setEntities() {
		powerStation = new PowerStation(this, -5, -4f);
		bulb = new Bulb(this, 5f, +1f);
	}
	
	@Override
	void createColorBalls() {
		for (int i = 0; i < RED_BALLS; i++)
			freeBalls.add(new ColorBall(this,ColorBall.RED));
		for (int i = 0; i < BLUE_BALLS; i++)
			freeBalls.add(new ColorBall(this,ColorBall.BLUE));
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
