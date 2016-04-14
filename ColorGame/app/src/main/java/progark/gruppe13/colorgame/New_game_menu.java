package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mac on 14.04.2016.
 */
public class New_game_menu extends GameState {
    // TODO: Rename and change types and number of parameters
    public static Start_menu newInstance() {
        Start_menu fragment = new Start_menu();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public New_game_menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game_menu, container, false);
    }

    public void onNewGameClick(View v){
        Log.i("klkikk", "klikk");
    }



    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }

}
