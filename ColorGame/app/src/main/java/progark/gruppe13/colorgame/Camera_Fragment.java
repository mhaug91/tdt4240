package progark.gruppe13.colorgame;


import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import progark.gruppe13.colorgame.util.Analyzer;
import progark.gruppe13.colorgame.util.States;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Camera_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camera_Fragment extends GameState {

    int targetRed;
    int targetGreen;
    int targetBlue;
    TextView timerTextView;
    LinearLayout cameraLayoutHolder;
    long startTime = 0;

    private Camera mCamera;
    private CameraPreview mPreview;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Bitmap bm;

    // TODO: Rename and change types and number of parameters
    public static Camera_Fragment newInstance() {
        Camera_Fragment fragment = new Camera_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public Camera_Fragment() {
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
        View view = inflater.inflate(R.layout.camera_layout, container, false);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), mCamera);
        //FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
        FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        cameraLayoutHolder = (LinearLayout) view.findViewById(R.id.cameraLayoutHolder);
        //henter farge fra server
        main.serverHandler.getColor();


        // Add a listener to the Capture button
        Button captureButton = (Button) view.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
        return view;
    }

    @Override
    public void serverCallback(final ColorMessage cm) {
        System.out.println("got message: ");
        if (cm.getType() == ColorMessage.ROUND) {
            int r = Integer.parseInt(cm.getMessage().get(0));
            int g = Integer.parseInt(cm.getMessage().get(1));
            int b = Integer.parseInt(cm.getMessage().get(2));
            targetRed = r;
            targetGreen = g;
            targetBlue = b;
            final int targetColorForUi = Color.rgb(r, g, b);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cameraLayoutHolder.setBackgroundColor(targetColorForUi);
                }
            });
        } else if (cm.getType() == ColorMessage.COLOR) {
            System.out.println();
            if (cm.getMessage().get(0).equals("Success")) {
                ((main) getActivity()).changeState(States.ROUND_SUMMARY);
            }
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("TAG", "Error creating media file, check storage permissions: " );
                System.out.println("no picture file yo");
                main.serverHandler.sendScore(42752);
                return;
            }
            Log.i("tag", "image length: " + data.length);
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            System.out.println("har laget bitmap");
            int score = Analyzer.scoreBitmap(bm,targetRed,targetGreen,targetBlue);
            System.out.println("Har laget score : " + score);

            main.serverHandler.sendScore(score);
            System.out.println("Har sendt score : " + score);
        }
    };



    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        Log.i("Camera_fragment", "getoutputmediafile method");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        //c=Camera.open();
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}

