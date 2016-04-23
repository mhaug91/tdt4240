package progark.gruppe13.colorgame;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import progark.gruppe13.colorgame.util.States;

/**
 * Created by mac on 14.04.2016.
 */
public class New_game_menu extends GameState {

    View view;
    EditText usernameEdit;

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

        view = inflater.inflate(R.layout.fragment_new_game_menu, container, false);

        Button startGameButton= (Button) view.findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNewGameClick();
                    }
                }
        );
        return view;

    }

    public void onNewGameClick(){
        usernameEdit  = (EditText) view.findViewById(R.id.editTextUsernameNewGame);
        String username = usernameEdit.getText().toString();
        main.serverHandler.startGame(username);
    }

    @Override
    public void serverCallback(ColorMessage cm){
        if (cm.getType() == ColorMessage.START){
            String gameID = cm.getMessage().get(0);
            System.out.println("jeg har connected og id er: " + gameID);
            ((main)getActivity()).changeState(States.LOBBY);
        }
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
