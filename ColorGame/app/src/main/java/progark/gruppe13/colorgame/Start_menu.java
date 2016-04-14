package progark.gruppe13.colorgame;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        return inflater.inflate(R.layout.fragment_start_menu, container, false);
    }



    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }



}
