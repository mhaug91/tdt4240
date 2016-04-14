package progark.gruppe13.colorgame;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
                currentState = new Start_menu();
                fragmentTransaction.replace(android.R.id.content, currentState);
                fragmentTransaction.commit();
                break;

            /*case NEW_GAME_MENU:
                currentState = new New_game_menu();
                fragmentTransaction.replace(android.R.id.content, currentState);
                fragmentTransaction.commit();
                */
        }


    }

    public void clickHandler(View v){

        switch (v.getId()){
            case R.id.joinGame:
                changeState(States.JOINGAME);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
