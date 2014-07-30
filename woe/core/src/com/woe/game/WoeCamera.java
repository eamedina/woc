package com.woe.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.woe.game.tanks.Tank2;

public class WoeCamera extends OrthographicCamera{

	private static final float VIEWPORT_WIDTH = 10;
	private Tank2 target;

	public void resize(int width, int height) {
		viewportWidth = VIEWPORT_WIDTH;
		viewportHeight = VIEWPORT_WIDTH*height/width;
		update();
	}

	public void setTarget(Tank2 tank) {
		this.target = tank;
	}
	
	public void target() {
		if (target ==null) return;
		position.x = target.getX();
		position.y = target.getY();
		update();
	}
}
