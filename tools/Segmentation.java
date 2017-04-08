package tools;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static tools.HistogramEq.HE.Histogram;

/**
 * Created by oleh on 02.01.16.
 */
public class Segmentation {

    /**
     * Детектор Кенні
     * @param image - Вхідне зображення
     * @param size - нижній поріг
     * @return Mat результат
     */
    public static Mat cannyDetection(Mat image, int size){

        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // cконвертація у градації сірого
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // видалення шумів
        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

        Imgproc.Canny(detectedEdges, detectedEdges, size, size/3, 3, false);
        return detectedEdges;
    }


    public static Mat Laplacian(Mat source, int size, int delta){

        int ddepth = CvType.CV_16S;

        Mat abs_dst,dst;

        Imgproc.GaussianBlur(source, source, new Size(3.0, 3.0), 0);

        Imgproc.GaussianBlur(source, source, new Size(3, 3), 0, 0,  Imgproc.BORDER_DEFAULT);
        //cvtColor( src, gray, CV_RGB2GRAY );

        /// Apply Laplace function
        Imgproc.Laplacian(source, source, CvType.CV_16S, size, 5, delta, Imgproc.BORDER_DEFAULT);
        return source;
    }

    /**
     *
     * @param source - Вхідне зображення
     * @param delta - дельта
     * @return
     */
    public static Mat Sobel(Mat source, int delta ){

        Mat grey = new Mat();
        Imgproc.cvtColor(source, grey, Imgproc.COLOR_BGR2GRAY);
        Mat sobelx = new Mat();
        Imgproc.Sobel(grey, sobelx, CvType.CV_32F, 1, delta);

        double minVal, maxVal;
        Core.MinMaxLocResult minMaxLocResult=Core.minMaxLoc(sobelx);
        minVal=minMaxLocResult.minVal;
        maxVal=minMaxLocResult.maxVal;

        Mat draw = new Mat();
        sobelx.convertTo(draw, CvType.CV_8U, 255.0 / (maxVal - minVal), -minVal * 255.0 / (maxVal - minVal));
        return draw;
    }


    /**
     *
     * @param img - Вхідне зображення
     * @return Mat - результат
     */
    public static Mat watershed(Mat img, int lowLevel)
    {
        Mat threeChannel = new Mat();

        Imgproc.cvtColor(img, threeChannel, Imgproc.COLOR_BGR2GRAY);


        Imgproc.threshold(threeChannel, threeChannel, lowLevel, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C);


        Mat fg = new Mat(img.size(),CvType.CV_8U);
        Imgproc.erode(threeChannel,fg,new Mat());

        Mat bg = new Mat(img.size(),CvType.CV_8U);
        Imgproc.dilate(threeChannel,bg,new Mat());
        Imgproc.threshold(bg,bg,1, 128, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);


        Mat markers = new Mat(img.size(), CvType.CV_8U, new Scalar(0));
        Core.add(fg, bg, markers);
        Mat result = new Mat();

        WatershedSegmenter segmenter = new WatershedSegmenter();
        segmenter.setMarkers(markers);
        result = segmenter.process(img);

        return result;
    }

    public Mat histogrmEqualization(Mat rgba){
        Mat mHSV = new Mat();
        Imgproc.cvtColor(rgba, mHSV, Imgproc.COLOR_RGBA2RGB,3);
        Imgproc.cvtColor(rgba, mHSV, Imgproc.COLOR_RGB2HSV,3);
        List<Mat> hsv_planes = new ArrayList<Mat>(3);
        Core.split(mHSV, hsv_planes);




        Mat channel = hsv_planes.get(0);
        channel = Mat.zeros(mHSV.rows(),mHSV.cols(), CvType.CV_8UC1);
        hsv_planes.set(2,channel);
        Core.merge(hsv_planes,mHSV);

        mHSV.convertTo(mHSV, CvType.CV_8UC1);
        mHSV = Histogram(mHSV);
        return mHSV;
    }

    /**
     *
     * @param src - вхідне зображення
     * @return Mat результат
     */
    public static Mat kmeans(Mat src){

        Mat mHSV = new Mat();
        Imgproc.cvtColor(src, mHSV, Imgproc.COLOR_RGBA2RGB,3);
        Imgproc.cvtColor(src, mHSV, Imgproc.COLOR_RGB2HSV,3);
        List<Mat> hsv_planes = new ArrayList<Mat>(3);
        Core.split(mHSV, hsv_planes);

        Mat channel = hsv_planes.get(0);
        channel = Mat.zeros(mHSV.rows(),mHSV.cols(),CvType.CV_8UC1);
        hsv_planes.set(2,channel);
        Core.merge(hsv_planes,mHSV);

        Mat clusteredHSV = new Mat();
        mHSV.convertTo(mHSV, CvType.CV_32FC3);
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER,200,0.1);
        Core.kmeans(mHSV, 3, clusteredHSV, criteria, 30, Core.KMEANS_PP_CENTERS);

        return mHSV;
    }

    /**
     * порогова сегментація
     * @param src1 - вхідне зображення
     * @param minValue - нижній поріг
     * @param maxValue - верхній поріг
     * @param threhType - тип сегментації
     * @return
     */
    public static Mat thresholding(Mat src1, int minValue, int maxValue, String threhType){

        Mat frame = new Mat();
        Imgproc.cvtColor(src1, src1, Imgproc.COLOR_BGR2GRAY);
        switch (threhType){
            case "THRESH_OTSU":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_OTSU);
                break;
            case "THRESH_BINARY":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_BINARY);
                break;
            case "THRESH_BINARY_INV":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_BINARY_INV);
                break;
            case "THRESH_TOZERO":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_TOZERO);
                break;
            case "THRESH_TRUNC":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_TRUNC);
                break;
            case "ADAPTIVE_THRESH_GAUSSIAN_C":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);
                break;
            case "ADAPTIVE_THRESH_MEAN_C":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C);
                break;
            case "THRESH_BINARY+THRESH_OTSU":
                Imgproc.threshold(src1, frame, minValue, maxValue, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
                break;
        }

        return frame;
    }

    public static Mat grabCut(Mat src){

        // by https://github.com/tanaka0079/java/blob/master/opencv/image/Grabcut.java
        Mat mask = new Mat();
        Mat bgModel = new Mat();
        Mat fgModel = new Mat();
        Rect rect = new Rect(10, 10,250,290);
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(src, mask, rect, bgModel, fgModel, 1, 0);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat fg = new Mat(src.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        src.copyTo(fg, mask);
        return fg;
    }
}