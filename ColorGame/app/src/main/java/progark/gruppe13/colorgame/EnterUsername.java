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
 * Created by jonmartin on 14.04.2016.
 */
public class EnterUsername extends GameState {
    View view;
    EditText userEdit;

    public static EnterUsername newInstance() {
        EnterUsername fragment = new EnterUsername();
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
        view = inflater.inflate(R.layout.fragment_enter_username, container, false);
        Button b = (Button) view.findViewById(R.id.buttonConfirmUsername);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmUsernameClick();
            }
        });

        userEdit = (EditText) view.findViewById(R.id.enterUsernameEdit);
        /*userEdit.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus)
                    userEdit.setHint("");
                else
                    userEdit.setHint("Your hint");
            }
        });*/

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_joingame, container, false);

    }
    public void onConfirmUsernameClick(){
        Context context = getActivity().getApplicationContext();
        String input = userEdit.getText().toString();
        if(input.matches("")){
        }
        else{
            main.serverHandler.sendUsername(input);
        }

    }

    @Override
    public void serverCallback(ColorMessage cm){
        if (cm.getType() == ColorMessage.USERNAME){
            String result = cm.getMessage().get(0);
            if (result.equals("ERROR: Username taken")){
                System.out.println("username taken yo!");
            }
            else{
                ((main)getActivity()).changeState(States.LOBBY);

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
