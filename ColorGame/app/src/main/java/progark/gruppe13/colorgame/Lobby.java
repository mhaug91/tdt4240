package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jonmartin on 15.04.2016.
 */
public class Lobby extends GameState {

    public static JoinGame newInstance() {
        JoinGame fragment = new JoinGame();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false);
    }

    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
}
