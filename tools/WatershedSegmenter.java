package tools;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * by https://github.com/rahul411/Watershed-based-image-segmentation-in-Android/blob/master/watershed/src/com/example/watershed/MainActivity.java
 */
public class WatershedSegmenter {

    public Mat markers=new Mat();

    public void setMarkers(Mat markerImage)
    {
        markerImage.convertTo(markers, CvType.CV_32SC1);
    }

    public Mat process(Mat image)
    {
        Imgproc.watershed(image,markers);
        markers.convertTo(markers,CvType.CV_8U);
        return markers;
    }
}