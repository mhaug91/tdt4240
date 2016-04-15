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
import android.view.View;

import progark.gruppe13.colorgame.util.States;

public class main extends Activity implements GameState.OnFragmentInteractionListener {
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
            case CAMERA_STATE:
                Log.i("kamera", "kamerastate");
                Intent intent = new Intent("progark.gruppe13.colorgame.CameraActivity");
                startActivity(intent);
                finish();
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

    public void onStartGameClick(View v){changeState(States.CAMERA_STATE);}
    public void onJoinClick(View v){changeState(States.JOINGAME);}

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
