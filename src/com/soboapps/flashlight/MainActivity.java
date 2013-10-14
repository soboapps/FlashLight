package com.soboapps.flashlight;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements Callback {

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private SurfaceView sView; 
    private SurfaceHolder sHolder;
    Parameters params;
    MediaPlayer mp;
    ImageButton btnSwitch;
    TextView txtOnOff;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // flash switch button
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        txtOnOff = (TextView) findViewById(R.id.txtOnOff);
        sView = (SurfaceView) findViewById(R.id.PREVIEW);
        sHolder = sView.getHolder();

        // First check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
        	
            	// device doesn't have or support a LED flash
        	Toast toast = Toast.makeText(this, (R.string.no_flash_message), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER, 0, 0);
        	toast.show();
        	
            	// Show message and sho2w a white screen 
   		TextView txtBkupLight=(TextView)findViewById(R.id.tvBkupLightSrc);
   		txtBkupLight.setBackgroundColor(Color.WHITE);
   			
	        } else {
	
	        // get the camera
	        getCamera();
	
	        // displaying power button image
	        togglePowerButtonImage();
	
	        // Switch button click event to toggle flash on/off
	        btnSwitch.setOnClickListener(new View.OnClickListener() {
	
	            @Override
	            public void onClick(View v) {
	                if (isFlashOn) {
	                    // turn off flash
	                    turnOffFlash();
	                } else {
	                    // turn on flash
	                    turnOnFlash();
	                }
	            }
	        });
	        }
        }

    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
            	sHolder.addCallback(this);
                camera = Camera.open();
                try {
					camera.setPreviewDisplay(sHolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        
    	if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
      
            // check to see if the device can vibrate
            if ( getSystemService(VIBRATOR_SERVICE) != null ) {
            // vibrate device	
            	powerVibrate();
            }else{
            // play sound
                playSound();
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            togglePowerButtonImage();
        }
    }

    // Turning Off flash
    private void turnOffFlash() {
        
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            // check to see if the device can vibrate
            if ( getSystemService(VIBRATOR_SERVICE) != null ) {
            // vibrate device	
            	powerVibrate();
            }else{
            // play sound
                playSound();
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            togglePowerButtonImage();
        }
    }

    // Vibrate Phone
    public void powerVibrate() {
        if(isFlashOn){
            Vibrator vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
        }else{
            Vibrator vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
        }
    }

    // Playing sound
    // will play button toggle sound on flash on / off
    private void playSound(){
        if(isFlashOn){
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_off);
        }else{
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_on);
        }
        mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }

    /*
     * Toggle switch power button images
     * changing image states to on / off
     * */
    private void togglePowerButtonImage(){
        if(isFlashOn){
            btnSwitch.setImageResource(R.drawable.flashlight_on);
            txtOnOff.setText("ON");
        }else{
            btnSwitch.setImageResource(R.drawable.flashlight_off);
            txtOnOff.setText("OFF");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	    try {
			camera.setPreviewDisplay(sHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	    
        if (camera != null) {
            camera.release();
            camera = null;
        }
	}

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
        	case R.id.menuDonate:
                startActivity(new Intent(MainActivity.this, Donate.class));
                return true;
            case R.id.menuAbout:
                startActivity(new Intent(MainActivity.this, About.class));
                return true;
        	case R.id.menuExit:
        		finish();     
        }
        	return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
