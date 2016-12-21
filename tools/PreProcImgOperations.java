package tools;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by oleh on 02.01.16.
 * методи попередньої обробки
 */
public class PreProcImgOperations {


    public static Mat contrast (Mat image, Double a){

        Mat dst = new Mat(image.rows(), image.cols(), image.type());
        image.copyTo(dst);

        Scalar modifier;
        modifier = new Scalar(1,1,1.2,1);
        Core.multiply(dst, modifier, dst);
        return dst;
    }

    /**
     *
     * @param image - вхідне зображення в форматі Mat
     * @param sz
     * @return
     */
    public static Mat bright(Mat image, int sz){

        Mat dst = new Mat(image.rows(), image.cols(), image.type());
        image.convertTo(dst, -1, 10d * sz / 100, 0);
        return dst;
    }

    /**
     *
     * @param image - вхідне зображення
     * @param kernel - ядро
     * @return Mat
     */
    public static Mat Erode(Mat image, int kernel){

        final Mat dst = new Mat(image.cols(), image.rows(), CvType.CV_8UC3);
        image.copyTo(dst);
        Imgproc.erode(dst, dst, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kernel,kernel)));
        return dst;
    }

    /**
     *
     * @param image - вхідне зображення
     * @param kernel - ядро
     * @return
     */
    public static Mat Dilate(Mat image, int kernel){

        final Mat dst = new Mat(image.cols(), image.rows(), CvType.CV_8UC3);
        image.copyTo(dst);
        Imgproc.dilate(dst, dst, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kernel,kernel)));
        return dst;
    }


}
