package es.morse.flash;

import com.badlogic.gdx.utils.Disposable;

public interface CameraResolver extends Disposable {
	
	void dispose();
	
	void update(double delta);
	
	boolean isProccessing();
	
	void queueLetter(String letter);
	public boolean turnOnFlash();
	public boolean turnOnFlash(double time);
	public boolean turnOffFlash();
	public boolean turnOffFlash(double time);
	public void releaseCamera ();
	public void connectToCamera ();
}
