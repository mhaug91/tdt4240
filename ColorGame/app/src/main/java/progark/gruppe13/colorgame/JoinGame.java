package progark.gruppe13.colorgame;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import progark.gruppe13.colorgame.util.States;

/**
 * Created by jonmartin on 14.04.2016.
 */
public class JoinGame extends GameState {
    View view;
    EditText sessionEdit;

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
        view = inflater.inflate(R.layout.fragment_joingame, container, false);
        Button b = (Button) view.findViewById(R.id.buttonJoinSession);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoinSessionClick();
            }
        });

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_joingame, container, false);

    }
    public void onJoinSessionClick(){
        Log.i("OIDA", "klikka her duuu..");
        //ServerHandler mySH = new ServerHandler("127.0.0.1", 1501, "julenissen");
        Context context = getActivity().getApplicationContext();
        sessionEdit  = (EditText) view.findViewById(R.id.sessionIdEdit);
        String input = sessionEdit.getText().toString();
        Toast toast;
        if(input.matches("")){
            toast = Toast.makeText(context, "Please insert a username to start a game, dude", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            main.serverHandler.joinGame(input);
        }

    }

    @Override
    public void serverCallback(ColorMessage cm){
        System.out.println("Dette skjedde");
        System.out.println(cm.getMessage().get(0));
        if (cm.getType() == ColorMessage.JOIN){
            String result = cm.getMessage().get(0);
            if (result.equals("Joined the game session")){
                ((main)getActivity()).changeState(States.ENTERUSERNAME);
            }
            else{
                System.out.println("Ville ikke joine");
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
}
