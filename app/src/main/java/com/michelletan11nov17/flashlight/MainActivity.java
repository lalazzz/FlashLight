package com.michelletan11nov17.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

// If the extend AppCompatActivity is as Activity,
// it will only support in current android version and will not work on backward compatibility.
public class MainActivity extends AppCompatActivity {

    //Define MainActivity global variables
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters param; //if the text is strikeout, it shows that the library is not the latest.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("MainActivity","OnCreate");

        // First check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        getCamera();

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);

        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });


    } //End of onCreate


    //Create method getCamera(), in MainActivity class.
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                param = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Failed to Open. Mia Camera Error: ", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || param == null) {
                Log.v("AndroidATC", "Camera is NULL");
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashOn = true;

            Log.v("AndroidATC", "Flash has been turned on ...");
        }

    }

    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || param == null) {
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn = false;

            Log.v("AndroidATC", "Flash has been turned off ...");

        }
    }

    //Examples that if the orientation is rotated, the following lifecycle should be look into
    @Override
    protected void onPause() {
        super.onPause();
        //Turning off the camera flash
        turnOffFlash();
        Log.v("Mia Class", "onPause invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Mia Class","OnDestroy invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("Mia Class", "onRestart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Mia Class", "onResume invoked");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //to get the camera start againg after rotating
        getCamera();
        Log.v("Mia Class", "onStart invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //
        if (camera != null) {
            camera.release();
            camera = null;
        }
        Log.v("Mia Class", "onStop invoked");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
