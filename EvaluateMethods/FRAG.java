package EvaluateMethods;

import org.opencv.core.Mat;
import tools.FIndObjectByContours;

/**
 * Created by oleh on 19.12.2016.
 */
public class FRAG {

    private Mat img1, img2;

    public FRAG(Mat img1, Mat img2){
    this.img1 = img1;
    this.img2 = img2;
    }

    public double getResult(){

        FIndObjectByContours fIndObjectByContoursEtalon = new FIndObjectByContours(img1);
        FIndObjectByContours fIndObjectByContoursSegmented = new FIndObjectByContours(img2);

        double a = 0.16, b = 2;
        double frag = 1 / (1 + Math.pow(Math.abs(a * ( fIndObjectByContoursSegmented.getObjectCount() -
                fIndObjectByContoursEtalon.getObjectCount() )), b) );

        return frag;
    }
}
