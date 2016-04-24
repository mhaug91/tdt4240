package progark.gruppe13.colorgame.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Thomas on 21.04.2016.
 */
public class Analyzer {

    public static int scoreBitmap(Bitmap bitmap, int goalRed, int goalGreen, int goalBlue){
        int PIXEL_STEP = 30;

        int imgH = bitmap.getHeight();
        int imgW = bitmap.getWidth();
        int score = 0;
        int maxLeeway = 13000;
        int pixelCount = 0;

        for (int x = 0; x < imgW; x += PIXEL_STEP) {
            for (int y = 0; y < imgH ; y += PIXEL_STEP) {
                int pixel = bitmap.getPixel(x,y);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                pixelCount++;
                if (similarTo(redValue, greenValue,blueValue,goalRed,goalGreen,goalBlue,maxLeeway)){
                    score++;
                }
            }
        }
        System.out.println(pixelCount);
        System.out.println("The score is: " + score/4);
        pixelCount = pixelCount/100000;
        return score/pixelCount;
    }


    private static ArrayList<Integer> rgb2lab(int R, int G, int B) {
        ArrayList<Integer> lab = new ArrayList<Integer>();
        float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
        float Ls, as, bs;
        float eps = 216.f/24389.f;
        float k = 24389.f/27.f;

        float Xr = 0.964221f;  // reference white D50
        float Yr = 1.0f;
        float Zr = 0.825211f;

        // RGB to XYZ
        r = R/255.f; //R 0..1
        g = G/255.f; //G 0..1
        b = B/255.f; //B 0..1

        // assuming sRGB (D65)
        if (r <= 0.04045)
            r = r/12;
        else
            r = (float) Math.pow((r+0.055)/1.055,2.4);

        if (g <= 0.04045)
            g = g/12;
        else
            g = (float) Math.pow((g+0.055)/1.055,2.4);

        if (b <= 0.04045)
            b = b/12;
        else
            b = (float) Math.pow((b+0.055)/1.055,2.4);


        X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
        Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
        Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

        // XYZ to Lab
        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > eps )
            fx =  (float) Math.pow(xr, 1/3.);
        else
            fx = (float) ((k * xr + 16.) / 116.);

        if ( yr > eps )
            fy =  (float) Math.pow(yr, 1/3.);
        else
            fy = (float) ((k * yr + 16.) / 116.);

        if ( zr > eps )
            fz =  (float) Math.pow(zr, 1/3.);
        else
            fz = (float) ((k * zr + 16.) / 116);

        Ls = ( 116 * fy ) - 16;
        as = 500*(fx-fy);
        bs = 200*(fy-fz);

        lab.add((int) (2.55*Ls + .5));
        lab.add((int) (as + .5));
        lab.add((int) (bs + .5));

        return lab;
    }

    private static boolean similarTo(int imgRed, int imgGreen, int imgBlue, int gRed, int gGreen, int gBlue, int maxLeeway){

        ArrayList<Integer> imgRes = rgb2lab(imgRed, imgGreen, imgBlue);
        ArrayList<Integer> goalRes = rgb2lab(gRed, gGreen, gBlue);

        double labDist = Math.pow(imgRes.get(0) - goalRes.get(0), 2) +
                Math.pow(imgRes.get(1) - goalRes.get(1), 2) +
                Math.pow(imgRes.get(2) - goalRes.get(2), 2);

        return labDist < maxLeeway;
    }

}