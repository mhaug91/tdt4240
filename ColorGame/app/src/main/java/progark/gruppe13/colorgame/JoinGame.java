package progark.gruppe13.colorgame;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        Context context = getActivity().getApplicationContext();
        sessionEdit  = (EditText) view.findViewById(R.id.sessionInputText);
        String input = sessionEdit.getText().toString();
        Toast toast;
        if(input.matches("")){
            toast = Toast.makeText(context, "Plz insert a session id to play", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            toast = Toast.makeText(context, "Joining " + input + "...", Toast.LENGTH_LONG);
            toast.show();
            joinSession();
        }

    }



    private void joinSession() {
        //koble til sesssion her
    }


    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
}
