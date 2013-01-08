package com.eyeverify.cameraplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class CameraSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Camera.PreviewCallback {
	private static final String TAG = "CameraSurfaceView";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private byte[] mData;
	private long prevFrameTick = System.currentTimeMillis();
	private int perSecondFrameCount = 0;
	private TextView mTxtFrameRate;

	public CameraSurfaceView(Context context, Camera camera, TextView txtFrameRate) {
		super(context);
		mCamera = camera;
		mTxtFrameRate = txtFrameRate;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(holder);

			Size previewSize = mCamera.getParameters().getPreviewSize();
			mData = new byte[(int) (previewSize.height * previewSize.width * 1.5)];
			
			Size size = mCamera.getParameters().getPictureSize();
			Log.d(TAG, "Size -> Width: " + size.width + " Height: " + size.height);
			initBuffer();
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	private void initBuffer() {
		mCamera.addCallbackBuffer(mData);
		mCamera.addCallbackBuffer(mData);
		mCamera.addCallbackBuffer(mData);
		mCamera.setPreviewCallbackWithBuffer(this);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			initBuffer();
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		// System.arraycopy(data, 0, mData, 0, data.length);
		if (System.currentTimeMillis() - prevFrameTick < 1000) {
			perSecondFrameCount++;
		} else {
			Log.e("onPreviewFrame", perSecondFrameCount + " fps");
			mTxtFrameRate.setText(perSecondFrameCount + " fps");
			prevFrameTick = System.currentTimeMillis();
			perSecondFrameCount = 0;
		}

		mData = new byte[data.length];
		mCamera.addCallbackBuffer(mData);
	}
}
