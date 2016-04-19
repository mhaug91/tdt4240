package progark.gruppe13.colorgame;

import android.content.Context;
import android.os.Bundle;
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
        View view = inflater.inflate(R.layout.fragment_joingame, container, false);

        Button b = (Button) view.findViewById(R.id.buttonJoinSession);
        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoinSessionClick();
            }
        });
        */


        return view;


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_joingame, container, false);

    }
    public void onJoinSessionClick(){
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Max jobber med saken", Toast.LENGTH_LONG);
        toast.show();
        //sessionEdit = (EditText)findViewById(R.id.sessionInputText).
        joinSession();
        }

    private void joinSession() {
        //
    }





    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
}
