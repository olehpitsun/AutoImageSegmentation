package modules;

import org.opencv.core.Mat;
import tools.Filter.FiltersOperations;
import tools.PreProcImgOperations;
import tools.Segmentation;


/**
 * Created by oleh on 11.08.2016.
 */
public class ImageManagerModule {

    public Mat autoImageCorrection(Mat src, int lowLevel){

        FiltersOperations filtroperation = new FiltersOperations(src, "4", "5", "", "", ""); // медіанний фільтр

        Mat brightMat = PreProcImgOperations.bright(filtroperation.getOutputImage(), 10); // яскравість
        filtroperation.getOutputImage().release();
        Mat contrastMat = PreProcImgOperations.contrast(brightMat, 1.0);
        brightMat.release();

        //Mat histogramMat = histogrmEqualization(brightMat);

        Mat result = Segmentation.watershed(contrastMat, lowLevel);

        return result;
    }

    public Mat autoThresholdCorrection(Mat src, int lowLevel, String type){

        FiltersOperations filtroperation = new FiltersOperations(src, "4", "5", "", "", ""); // медіанний фільтр

        Mat brightMat = PreProcImgOperations.bright(filtroperation.getOutputImage(), 10); // яскравість
        filtroperation.getOutputImage().release();
        Mat contrastMat = PreProcImgOperations.contrast(brightMat, 1.0);
        brightMat.release();

        //Mat histogramMat = histogrmEqualization(brightMat);

        Mat result = Segmentation.thresholding(contrastMat, lowLevel, 255, type);

        return result;
    }

    public Mat autoKmeansdCorrection(Mat src){

        FiltersOperations filtroperation = new FiltersOperations(src, "4", "5", "", "", ""); // медіанний фільтр

        Mat brightMat = PreProcImgOperations.bright(filtroperation.getOutputImage(), 10); // яскравість
        filtroperation.getOutputImage().release();
        Mat contrastMat = PreProcImgOperations.contrast(brightMat, 1.0);
        brightMat.release();

        //Mat histogramMat = histogrmEqualization(brightMat);

        Mat result = Segmentation.kmeans(contrastMat);

        return result;
    }


}
