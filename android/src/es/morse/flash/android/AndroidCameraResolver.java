package es.morse.flash.android;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import es.morse.flash.CameraResolver;
import es.morse.flash.MorseFlash;
import es.morse.flash.TextToMorse;
import es.morse.flash.TextToMorse.Item;
import es.morse.flash.TextToMorse.Letter;

public class AndroidCameraResolver implements CameraResolver {
	MorseFlash _app;
	Context _context;
	Camera _camera;
	double _targetTime = 0;
	
	Parameters _param;
	
	Queue<Letter> queue;
	
	AndroidCameraResolver(Context context, MorseFlash mf) {
		super();
		_app = mf;
		_context = context;
		_camera = Camera.open();
		_param = _camera.getParameters();
		
		queue = new LinkedList<Letter>();
	}

	public void queueLetter(String letter) {
		queue.add(TextToMorse.getLetter(letter));
	}
	
	@Override
	public void update(double delta) {
		
		if(_targetTime == 0) { // If we are not processing
			if(!queue.isEmpty()) { // If there is something to process
				Letter l = queue.element();
				Item current = l._items.poll();
				if(current._light) {
					System.out.println("TIEMPO LUZ: " + current._time);
					turnOnFlash(current._time);
				}
				else {
					turnOffFlash(current._time);
					if(l._items.isEmpty()) {
						_app.colorLetter();
						queue.poll();
					}
				}
			}
			else if(_camera.getParameters().getFlashMode() != Parameters.FLASH_MODE_OFF) {
				turnOffFlash();
			}
		}
		else if(_targetTime > 0) {
			_targetTime -= delta;
			_targetTime = Math.max(0, _targetTime);
		}
	}
	
	@Override
	public boolean turnOnFlash() {
		if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			_param.setFlashMode(Parameters.FLASH_MODE_TORCH);
			_camera.setParameters(_param);
			_camera.startPreview();
			return true;
		}
		return false;
	}

	@Override
	public boolean turnOnFlash(double time) {
		if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			
			
			_param.setFlashMode(Parameters.FLASH_MODE_TORCH);
			_camera.setParameters(_param);
			_camera.startPreview();
			_targetTime = time;
			
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean turnOffFlash() {
		if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			_param.setFlashMode(Parameters.FLASH_MODE_OFF);
			_camera.setParameters(_param);
			return true;
		}
		return false;
		
	}
	
	@Override
	public boolean turnOffFlash(double time) {
		if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			_param.setFlashMode(Parameters.FLASH_MODE_OFF);
			_camera.setParameters(_param);
			_targetTime = time;

			return true;
		}
		return false;
		
	}
	
	@Override
	public void connectToCamera() {
		_camera = Camera.open(); // Must deal with null return
		_param = _camera.getParameters();
	}
	
	@Override
	public void releaseCamera() {
		if(_camera != null) {
			_camera.stopPreview();
			_camera.release();
		}
	}
	
	@Override
	public void dispose() {
		//releaseCamera();
	}

	@Override
	public boolean isProccessing() {
		return !queue.isEmpty();
	}

}
