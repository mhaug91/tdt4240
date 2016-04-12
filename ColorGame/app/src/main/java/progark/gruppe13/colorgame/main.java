package progark.gruppe13.colorgame;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import progark.gruppe13.colorgame.util.States;

public class main extends Activity {
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
        }


    }
}
