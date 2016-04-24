package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import progark.gruppe13.colorgame.util.States;

import java.util.ArrayList;

/**
 * Created by Ingrid on 15.04.2016.
 */
public class RoundSummary extends GameState{

    private boolean hasRenderedScores = false;
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
        Button startGameButton = (Button) view.findViewById(R.id.startNewRoundButton);
        startGameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStartNewRoundClick();
                    }
                }
        );

        Button exitGameButton = (Button) view.findViewById(R.id.buttonExitGame);
        exitGameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExitGameClick();
                    }
                }
        );


        return view;
    }

    private void onExitGameClick() {
        System.out.println("exitgameclicked, returning to main");
        ((main)getActivity()).returnToStartMenu();
    }

    private void onStartNewRoundClick() {
        System.out.println("new round clicked, beginning round.");
        main.serverHandler.beginRound();
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
                    for (int i = 0; i < userAndScoreArray.size(); i += 2) {
                        playersAndScoreTextView.append(userAndScoreArray.get(i));
                        playersAndScoreTextView.append(": \t");
                        playersAndScoreTextView.append(userAndScoreArray.get(i + 1));
                        playersAndScoreTextView.append("\n");
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            hasRenderedScores = true;
                        }

                    }, 1000); // 5000ms delay
                }
            });
        }else if (cm.getType() == ColorMessage.BEGIN && hasRenderedScores){
            if (cm.getMessage().get(0).equals("success"))
                ((main)getActivity()).changeState(States.CAMERA_FRAGMENT_STATE);
        }
    }

}
