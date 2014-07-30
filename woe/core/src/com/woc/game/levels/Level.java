package com.woc.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.woc.game.etities.ColorBall;

public interface Level extends Screen{

	float getWidth();

	float getHeight();

	World getWorld();

	ArrayList<ColorBall> getPotencialsJoints(ColorBall colorBall);

}
