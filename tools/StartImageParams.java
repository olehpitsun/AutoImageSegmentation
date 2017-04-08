package tools;

import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * Created by Oleh7 on 2/26/2016.
 */
public class StartImageParams {

    private int redValue, greenVlue, blueValue, histogramAverage;

    public StartImageParams(){}

    public void getStartValues(Mat inputImg){
        String timeStamp = "temp.png";
        matToBufImg matToBufImg = new matToBufImg(inputImg,timeStamp);
        BufferedImage bi = matToBufImg.getImage();

        for (int i = 0; i < 256; i++) {}

        int x0 =0;
        int y0 = 0;
        int w = bi.getWidth();
        int h = bi.getHeight();

        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Color pixel = new Color(bi.getRGB(x, y));
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
            }
        }
        int num = w * h;
        this.redValue = (int)sumr/num;
        this.greenVlue = (int)sumg/num;
        this.blueValue = (int)sumb/num;

        int bright = (int) (299 * sumr + 587 * sumg + 114 * sumb) / num;
        //bright = bright/1000;

        this.histogramAverage = Math.abs(bright);
        System.out.println( "Brightness " + this.histogramAverage + " Dlue " +this.blueValue );

    }

    public int getRedValue(){
        return this.redValue;
    }

    public int getGreenVlue(){
        return this.greenVlue;
    }

    public int getBlueValue(){
        return this.blueValue;
    }

    public int getHistogramAverage(){
        return histogramAverage;
    }
}

