package progark.gruppe13.colorgame;


import android.app.Fragment;

public abstract class GameState extends Fragment {

    public abstract void update();

    public abstract void onEnter();

}
