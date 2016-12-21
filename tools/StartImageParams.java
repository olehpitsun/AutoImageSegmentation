package tools;

import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Oleh7 on 2/26/2016.
 */
public class StartImageParams {

    private double redValue, greenVlue, blueValue;
    private float histogramAverage;

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
        this.redValue = sumr/num;
        this.greenVlue = sumg/num;
        this.blueValue = sumb/num;

        float bright = (299 * sumr + 587 * sumg + 114 * sumb) / 10000;
        bright = bright/100000;
        this.histogramAverage = bright;
    }

    public double getRedValue(){
        return this.redValue;
    }

    public double getGreenVlue(){
        return this.greenVlue;
    }

    public double getBlueValue(){
        return this.blueValue;
    }

    public float getHistogramAverage(){
        return histogramAverage;
    }
}

