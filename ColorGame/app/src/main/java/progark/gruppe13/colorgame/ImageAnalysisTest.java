package progark.gruppe13.colorgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import progark.gruppe13.colorgame.util.Analyzer;

/**
 * Created by mac on 14.04.2016.
 */
public class ImageAnalysisTest extends GameState {
    // TODO: Rename and change types and number of parameters
    public static Start_menu newInstance() {
        Start_menu fragment = new Start_menu();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ImageAnalysisTest() {
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

        View view = inflater.inflate(R.layout.fragment_image_test, container, false);


        //ImageView img = (ImageView) view.findViewById(R.id.testImage);
        //img.setImageResource(R.drawable.test);

        TextView tw = (TextView) view.findViewById(R.id.imageScoreTextView);

        Bitmap testImage = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.test);
        tw.setText(Integer.toString(Analyzer.scoreBitmap(testImage,161,33,30)));
        //red: 161,33,30
        //blue: 66,75,120

        return view;
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