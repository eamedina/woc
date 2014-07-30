package com.woc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.woc.game.etities.ColorBall;
import com.woc.game.levels.Level;

public class WocCamera extends OrthographicCamera implements GestureListener, InputProcessor {

	private static final float VIEWPORT_WIDTH = 10;
	private static final float SPEED = 2f;
	private Level level;
	ColorBall target;
	private float initialScale = 1;// ZOOM
	private float maxZoom;

	public WocCamera(Level level, int x, int y) {
		this.level = level;
		// position.set(x, y, 0);
		update();
	}

	public void setTarget(ColorBall target) {
		this.target = target;
	}

	public void removeTarget() {
		this.target = null;
	}

	public void resize(int width, int height) {
		viewportWidth = VIEWPORT_WIDTH;
		viewportHeight = VIEWPORT_WIDTH * height / width;
		if (viewportHeight > level.getHeight()) {
			viewportWidth = level.getHeight() * width / height;
			viewportHeight = level.getHeight();
		}
		maxZoom = Math.max(level.getWidth() / viewportWidth, level.getHeight() / viewportHeight);
		validateZoomAndPosition();
		update();
	}

	private void validateZoomAndPosition() {
		zoom = Math.min(zoom, maxZoom);
		float maxY = (level.getHeight() - viewportHeight * zoom) / 2;
		float maxX = (level.getWidth() - viewportWidth * zoom) / 2;
		if (viewportWidth * zoom > level.getWidth()) {
			position.x = 0;
		} else {
			position.x = Math.max(Math.min(position.x, maxX), -maxX);
		}
		if (viewportHeight * zoom > level.getHeight()) {
			position.y = 0;
		} else {
			position.y = Math.max(Math.min(position.y, maxY), -maxY);
		}
	}

	public void step(float delta) {
		if (target != null) {
			float top = viewportHeight / 2 * zoom + position.y;
			float boton = -viewportHeight / 2 * zoom + position.y;
			float left = viewportWidth / 2 * zoom + position.x;
			float right = -viewportWidth / 2 * zoom + position.x;
			if (top - target.getY() < 1f)
				position.y += delta * SPEED;
			else if (boton - target.getY() > -1f)
				position.y -= delta * SPEED;
			if (left - target.getX() < 1f)
				position.x += delta * SPEED;
			else if (right - target.getX() > -1f)
				position.x -= delta * SPEED;
			validateZoomAndPosition();
			update();
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		float cameraToWorld = level.getWidth() / Gdx.graphics.getWidth();
		if (target == null) {
			float camX = position.x - deltaX * cameraToWorld;
			float camY = position.y + deltaY * cameraToWorld;
			position.set(camX, camY, 0);
			validateZoomAndPosition();
			update();
		}
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		initialScale = zoom;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		float ratio = initialDistance / distance;
		zoom = initialScale * ratio;
		validateZoomAndPosition();
		update();
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void win() {
		position.set(Vector3.Zero);
		zoom = 2;
		update();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		zoom += amount * .1f;

		validateZoomAndPosition();
		update();
		return true;
	}

}
