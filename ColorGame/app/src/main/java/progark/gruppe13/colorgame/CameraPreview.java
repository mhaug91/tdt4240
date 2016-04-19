package progark.gruppe13.colorgame;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

import progark.gruppe13.colorgame.util.States;

/**
 * Created by mac on 14.04.2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("joi", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("tag", "Error starting camera preview: " + e.getMessage());
        }
    }


    public static class main extends Activity implements GameState.OnFragmentInteractionListener {
        /**
         * Called when the activity is first created.
         */

        private GameState currentState;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.changeState(States.STARTMENU);
        }


        private void changeState(States toState){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (toState){
                case STARTMENU:
                    currentState = new Start_menu();
                    fragmentTransaction.replace(android.R.id.content, currentState);
                    fragmentTransaction.commit();
                    break;

                case JOINGAME:
                    currentState = new JoinGame();
                    fragmentTransaction.replace(android.R.id.content, currentState);
                    fragmentTransaction.commit();
                    break;

                case NEW_GAME_MENU:
                    currentState = new New_game_menu();
                    fragmentTransaction.replace(android.R.id.content, currentState);
                    fragmentTransaction.commit();
                    break;
                case CAMERA_STATE:
                    Log.i("kamera", "kamerastate");
                    Intent intent = new Intent("progark.gruppe13.colorgame.CameraActivity");
                    startActivity(intent);
                    finish();
                    break;
                case CAMERA_FRAGMENT_STATE:
                    currentState = new Camera_Fragment();
                    fragmentTransaction.replace(android.R.id.content, currentState);
                    fragmentTransaction.commit();
                    break;
            }


        }


        /** Check if this device has a camera */
        private boolean checkCameraHardware(Context context) {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // this device has a camera
                return true;
            } else {
                // no camera on this device
                return false;
            }
        }

        /** A safe way to get an instance of the Camera object. */
        public static Camera getCameraInstance(){
            Camera c = null;
            try {
                c = Camera.open(); // attempt to get a Camera instance
            }
            catch (Exception e){
                // Camera is not available (in use or does not exist)
            }
            return c; // returns null if camera is unavailable
        }

        public void onNewClick(View v){
            changeState(States.NEW_GAME_MENU);
        }

        public void onStartGameClick(View v){changeState(States.CAMERA_FRAGMENT_STATE);}
        public void onJoinClick(View v){changeState(States.JOINGAME);}
        public void onStartCameraActivityClick(View v){ changeState(States.CAMERA_STATE);}
        //public void onJoinSessionClick(View v){changeState(States.LOBBY);}

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }
}