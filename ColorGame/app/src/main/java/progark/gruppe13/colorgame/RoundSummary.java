package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Ingrid on 15.04.2016.
 */
public class RoundSummary extends GameState{

    ArrayList<String> topFiveHighScoresList;

    public static RoundSummary newInstance() {
        RoundSummary fragment = new RoundSummary();
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
        return inflater.inflate(R.layout.fragment_roundsummary, container, false);
    }

    public void showResults(){

    }

    @Override
    public void update() {
    }
    @Override
    public void onEnter() {
    }
}
