package progark.gruppe13.colorgame;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import progark.gruppe13.colorgame.util.States;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Start_menu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Start_menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Start_menu extends GameState {


    // TODO: Rename and change types and number of parameters
    public static Start_menu newInstance() {
        Start_menu fragment = new Start_menu();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public Start_menu() {
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
        View view = inflater.inflate(R.layout.fragment_start_menu, container, false);

        Button buttonStart = (Button) view.findViewById(R.id.buttonNewGame);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewGameClicked();
            }
        });

        Button buttonJoin = (Button) view.findViewById(R.id.buttonJoinGame);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoinGameClicked();
            }
        });


        return view;
    }

    private void onJoinGameClicked() {
        ((main)getActivity()).changeState(States.JOINGAME);
    }

    private void onNewGameClicked() {
        ((main)getActivity()).changeState(States.NEW_GAME_MENU);
    }
}
