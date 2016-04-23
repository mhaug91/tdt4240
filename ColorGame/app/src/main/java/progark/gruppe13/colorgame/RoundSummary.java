package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import progark.gruppe13.colorgame.util.States;

import java.util.ArrayList;

/**
 * Created by Ingrid on 15.04.2016.
 */
public class RoundSummary extends GameState{

    ArrayList<String> topFiveHighScoresList;
    private TextView playersAndScoreTextView;

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
        View view = inflater.inflate(R.layout.fragment_roundsummary, container, false);


        playersAndScoreTextView = (TextView) view.findViewById(R.id.scoreList);
        main.serverHandler.getScores();

        return view;
    }


    public void setTopFiveHighScoresList(ArrayList<String> topFiveHighScoresList) {
        this.topFiveHighScoresList = topFiveHighScoresList;
    }



    public void showResults(){


    }

    @Override
    public void serverCallback(ColorMessage cm){
        System.out.println("Her mottatt medling: " + cm.getMessage());
        if (cm.getType() == ColorMessage.AFTERMATH){

            System.out.println("Har mottatt scorelist: " + cm.getMessage());
            final ArrayList<String> userAndScoreArray = cm.getMessage();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playersAndScoreTextView.setSingleLine(false);
                    //bytt til topFiveHighScoresList
                    for (int i = 0; i < userAndScoreArray.size(); i+=2) {
                        playersAndScoreTextView.append(userAndScoreArray.get(i));
                        playersAndScoreTextView.append("\t");
                        playersAndScoreTextView.append(userAndScoreArray.get(i+1));
                        playersAndScoreTextView.append("\n");
                    }
                }
            });
        }
    }





    @Override
    public void update() {
        showResults();
    }
    @Override
    public void onEnter() {
    }
}
