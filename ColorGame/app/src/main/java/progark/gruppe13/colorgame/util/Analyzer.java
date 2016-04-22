package progark.gruppe13.colorgame.util;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Thomas on 21.04.2016.
 */
public class Analyzer {

    public static int scoreBitmap(Bitmap bitmap, int goalRed, int goalGreen, int goalBlue){
        int imgH = bitmap.getHeight();
        int imgW = bitmap.getWidth();
        int score = 0;
        int maxLeeway = 1500;

        for (int x = 0; x < imgW; x++) {
            for (int y = 0; y < imgH ; y++) {
                int pixel = bitmap.getPixel(x,y);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                if (similarTo(redValue, greenValue,blueValue,goalRed,goalGreen,goalBlue,maxLeeway)){
                    score++;
                }
            }
        }
        return score/4;
    }



    private static boolean  similarTo(int imgRed, int imgGreen, int imgBlue, int gRed, int gBlue, int gGreen, int maxLeeway){

        double distance = (imgRed - gRed)*(imgRed - gRed) + (imgGreen - gGreen)*(imgGreen - gGreen) + (imgBlue - gBlue)*(imgBlue - gBlue);
        if(distance < maxLeeway){
            return true;
        }else{
            return false;
        }
    }

}