package com.eyeverify.cameraplay;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button backFacing, frontFacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	backFacing = (Button)findViewById(R.id.BackFacingButton);
	frontFacing = (Button)findViewById(R.id.FrontFacingButton);

	frontFacing.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View view) {
		    Intent showViewFinderIntent = new Intent(MainActivity.this, CameraPreviewActivity.class);
		    showViewFinderIntent.putExtra("CameraID", "front");
		    startActivity(showViewFinderIntent);
		}
	    });

	backFacing.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View view) {
		    Intent showViewFinderIntent = new Intent(MainActivity.this, CameraPreviewActivity.class);
		    showViewFinderIntent.putExtra("CameraID", "back");
		    startActivity(showViewFinderIntent);
		}
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

}
