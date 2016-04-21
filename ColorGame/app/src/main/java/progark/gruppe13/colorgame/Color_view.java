package progark.gruppe13.colorgame;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/*
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;
*/

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Color_view#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Color_view extends GameState {


    // TODO: Rename and change types and number of parameters
    public static Color_view newInstance() {
        Color_view fragment = new Color_view();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public Color_view() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap temporary= BitmapFactory.decodeResource(getResources(),R.drawable.ladybug);//R.drawable.bitmap is the bitmap in your drawable folder.
        Bitmap capturedImage = Bitmap.createBitmap(temporary);
        //calculateImageScore(capturedImage);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_menu, container, false);
/*
        Button b = (Button) view.findViewById(R.id.buttonJoinGame);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoinClick();
            }

        });
*/
        return view;



    }
/*
    public void onJoinClick(){
        Context context = getActivity().getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "HEI PÅ DEG", Toast.LENGTH_LONG);
        toast.show();
    }
*/


    @Override
    public void update() {

    }

    @Override
    public void onEnter() {

    }
/*
    private void calculateImageScore(Bitmap capturedImage) {

        MBFImage input = null;
        //input = createMBFImage(""); //DENNE SKAL BRUKES.

        input= ResizeProcessor.halfSize(input);
        input=ResizeProcessor.halfSize(input);


        //*******************************************
        //To start our implementation, we’ll first apply a colour-space transform to the image:
        input= ColourSpace.convert(input,ColourSpace.CIE_Lab);
        //We can then construct the K-Means algorithm:
        FloatKMeans cluster = FloatKMeans.createExact(3);
        //The parameter (2) is the number of clusters or classes we wish the algorithm to generate.
        //We can optionally provide a second integer argument that controls the maximum number of iterations of the
        //algorithm (the default is 30 iterations if we don't specify otherwise).


        //The FloatKMeans algorithm takes its input as an array of floating point vectors (float[][]).
        //We can flatten the pixels of an image into the required form using the getPixelVectorNative() method:
        float[][] imageData = input.getPixelVectorNative(new float[input.getWidth() * input.getHeight()][3]);

        //The K-Means algorithm can then be run to group all the pixels into the requested number of classes:
        FloatCentroidsResult result = cluster.cluster(imageData);

        //Each class or cluster produced by the K-Means algorithm has an index, starting from 0.
        //Each class is represented by its centroid (the average location of all the points belonging to the class).
        //We can print the coordinates of each centroid:
        float[][] centroids = result.centroids;
        for(
                float[] fs
                :centroids)

        {
            System.out.println(Arrays.toString(fs));
        }//Now is a good time to test the code. Running it should print the (L, a, b) coordinates of each of the classes.


        //takes a vector (the L, a, b value of a single pixel) and returns the index of the class that it belongs to.
        //We’ll start by creating an image that visualises the pixels and their respective classes
        //by replacing each pixel in the input image with the centroid of its respective class:
        HardAssigner<float[], ?, ?> assigner = result.defaultHardAssigner();
        for(
                int y = 0;
                y<input.getHeight();y++)

        {
            for (int x = 0; x < input.getWidth(); x++) {
                float[] pixel = input.getPixelNative(x, y);
                int centroid = assigner.assign(pixel);
                input.setPixelNative(x, y, centroids[centroid]);
            }
        }

        //We can then display the resultant image. Note that we need to convert the image back to RGB colour space for it to display properly:
        input=ColourSpace.convert(input,ColourSpace.RGB);
        //DisplayUtilities.display(input);


        //The GreyscaleConnectedComponentLabeler class can be used to find the connected components:
        //Note that the GreyscaleConnectedComponentLabeler only processes greyscale images
        GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
        List<ConnectedComponent> components = labeler.findComponents(input.flatten());


        //The ConnectedComponent class has many useful methods for extracting information about the shape of the region.
        //Lets draw an image with the components numbered on it. We’ll use the centre of mass of each region to
        //position the number and only render numbers for regions that are over a certain size (50 pixels in this case):
        Color[] colors = new Color[5];
        int i = 0;

        for(
                ConnectedComponent comp
                :components)

        {


            if (comp.calculateArea() < input.getWidth() / 2) {
                continue;
            }


            //comp.pixels.iterator().next().binaryHeader();
            String color888 = comp.extractPixels1d(input).getBand(0).toString().split(" ")[0].substring(1);
            System.out.println(color888);
            double color666 = (Double.parseDouble(comp.extractPixels1d(input).getBand(0).toString().split(" ")[0].substring(1).replace(',', '.')) * 250);

            int color1 = (int) (Double.parseDouble(comp.extractPixels1d(input).getBand(0).toString().split(" ")[0].substring(1).replace(',', '.')) * 250);
            int color2 = (int) (Double.parseDouble(comp.extractPixels1d(input).getBand(1).toString().split(" ")[0].substring(1).replace(',', '.')) * 250);
            int color3 = (int) (Double.parseDouble(comp.extractPixels1d(input).getBand(2).toString().split(" ")[0].substring(1).replace(',', '.')) * 250);

            int R = color1;
            int G = color2;
            int B = color3;



            float[] hsbColor = new float[3];

            //System.out.println(getNameReflection(tempColor));
            //System.out.print(colorUtils.getColorNameFromColor(tempColor));


            float Y = (R + R + B + G + G + G) / 6;
            System.out.println("y: " + Y);

            //System.out.println(tempColor.RGBtoHSB(color1, color2, color3, hsbColor));

            for (float f : hsbColor) {
                //System.out.println("hihi: " + f);
            }
            //input.drawText("Point:" + comp.calculateCentroidPixel().binaryHeader(), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
            //frame.drawText("Y:" + Y, comp.calculateCentroidPixel().x*32,comp.calculateCentroidPixel().y*32, HersheyFont.TIMES_MEDIUM, 20);


            //input.drawText(colorUtils.getMoodFromColor(tempColor), comp.calculateCentroidPixel().x,comp.calculateCentroidPixel().y, HersheyFont.TIMES_MEDIUM, 25);

            //input.drawText(colorUtils.getMoodFromColor(tempColor), comp.calculateCentroidPixel().x,comp.calculateCentroidPixel().y+10, HersheyFont.TIMES_MEDIUM, 12);
            System.out.println("position of Jacob: " + comp.calculateCentroidPixel().x + "-" + comp.calculateCentroidPixel().y);


            /******* CALCULATE MOOD ************/


            //frame.drawText(colorUtils.getColorNameFromColor(tempColor), comp.calculateCentroidPixel().x*32,comp.calculateCentroidPixel().y*32, HersheyFont.TIMES_MEDIUM, 20);


     /*   colors[i] = tempColor;
        if (i == 4) {
            break;
        }
        System.out.println();
        i++;*/
            //System.out.println("colourspace: "+comp.extractPixels1d(input).getBand(0).split("+") + "," + comp.extractPixels1d(input).getBand(1) + "," + comp.extractPixels1d(input).getBand(2));
            //System.out.println("yolo : " + comp.extractPixels1d(input).

            //System.out.println(comp.pixels.iterator().next().binaryHeader());
            //input.drawText("color: " + comp.pixels.iterator().next().binaryHeader(),comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
/*
        }
        //Finally, we can display the image with the labels:

        /**DISPLAY SOMETHING*/
    //}


}
