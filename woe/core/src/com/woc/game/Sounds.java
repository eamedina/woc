package com.woc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	public static Sound beamSound;
	public static Sound selectedSound;
	private static long idSelected;
	private static long idBeam;

	private static void init() {
		if (beamSound==null) {
			beamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/beam.mp3"));
			selectedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/selected.mp3"));
		}
	}
	public static void startBeam() {
		if(idBeam!=0)return;
		idBeam =beamSound.loop(1f);
	}

	public static void stopBeam() {
		if(idBeam==0)return;
		beamSound.stop(idBeam);
		idBeam=0;
	}
	public static void startSelected() {
		init();
		idSelected =selectedSound.loop(1f);
	}

	public static void stopSelected() {
		selectedSound.stop(idSelected);
	}
}
