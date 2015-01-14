package es.morse.flash.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import es.morse.flash.MorseFlash;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		MorseFlash mf = new MorseFlash();
		mf.setCameraResolver(new AndroidCameraResolver(getApplicationContext(), mf));		
		
		initialize(mf, config);
	}
}
