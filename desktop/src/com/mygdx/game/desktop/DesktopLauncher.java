package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.ElectroFunCop22;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Electrofun - cop22";
		config.width = 600;
	    config.height = 600;
		
		new LwjglApplication(new ElectroFunCop22(), config);
	}
}
