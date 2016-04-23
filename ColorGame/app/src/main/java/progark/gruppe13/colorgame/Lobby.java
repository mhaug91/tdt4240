package progark.gruppe13.colorgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import progark.gruppe13.colorgame.util.States;

import java.util.ArrayList;

/**
 * Created by jonmartin on 15.04.2016.
 */
public class Lobby extends GameState {

    TextView textList;
    View view;

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

        view = inflater.inflate(R.layout.fragment_lobby, container, false);

        textList = (TextView) view.findViewById(R.id.usersInLobbyList);
        Button startGameButton = (Button) view.findViewById(R.id.buttonStartGame);
        startGameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStartGameClick();
                    }
                }
        );

        main.serverHandler.getUsernames("anything");
        return view;
    }


    public void onStartGameClick(){
        ((main)getActivity()).changeState(States.CAMERA_FRAGMENT_STATE);
    }


    @Override
    public void serverCallback(final ColorMessage cm){
        //hver gang noen andre logger på
        if (cm.getType() == ColorMessage.USERNAME || cm.getType() == ColorMessage.GETNAMES){
            final ArrayList<String> usernames = cm.getMessage();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textList.setSingleLine(false);
                    textList.setText("");
                    for (String id : usernames){
                        textList.append(id);
                        textList.append("\n");
                    }

                }
            });

        }
    }



    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
}
