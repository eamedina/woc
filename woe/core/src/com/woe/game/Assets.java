package com.woe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static TextureAtlas atlas;
	public static TextureRegion woe1;
	public static TextureRegion woe2;
	public static TextureRegion woe3;
	public static TextureRegion stage1;
	public static TextureRegion branch;
	public static TextureRegion tank;
	public static TextureRegion woodplank;
	public static TextureRegion white;
	
	public static TextureRegion tankchassis;
	public static TextureRegion caterpillar1;
	public static TextureRegion caterpillar2;
	public static TextureRegion cannon;
	public static TextureRegion tankwheel1;
	public static TextureRegion tankwheel2;
	public static TextureRegion tankwheel5;
	
	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("images.pack"));
		woe1 = atlas.findRegion("woe1");
		woe2 = atlas.findRegion("woe2");
		woe3 = atlas.findRegion("woe3");
		stage1 = atlas.findRegion("stage1");
		branch = atlas.findRegion("branch");
		tank = atlas.findRegion("tank");		
		woodplank = atlas.findRegion("woodplank");
		white = atlas.findRegion("white");
		
		tankchassis = atlas.findRegion("tankchassis");
		caterpillar1 = atlas.findRegion("caterpillar1");
		caterpillar2 = atlas.findRegion("caterpillar2");
		cannon = atlas.findRegion("cannon");
		tankwheel1 = atlas.findRegion("tankwheel1");
		tankwheel2 = atlas.findRegion("tankwheel2");	
		tankwheel5 = atlas.findRegion("tankwheel5");	
	}

	public static void dispose() {
		atlas.dispose();
	}
}
