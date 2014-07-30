package com.woe.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.woc.game.WocGame;

public class WocDesktopLauncher {
	public static void main (String[] arg) {
		boolean rebuildAtlas=true;
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
//			settings.debug = true;
			TexturePacker2.process(settings, "raw",
			"../android/assets",
			"images.pack");
			}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height=600;
		new LwjglApplication(new WocGame(), config);
	}
}
