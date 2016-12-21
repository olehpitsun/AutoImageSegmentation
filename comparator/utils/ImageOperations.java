package comparator.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

//import org.opencv.imgcodecs.Imgcodecs;

/**
 * Тут знаходження контурів, прорідження і тд.
 *
 * Created by Vit on 07.02.2016.
 */
public class ImageOperations {

    // кількість точок, до якої буде спрощено вхідний контур
    private static int NUM_TO_KEEP = 30;

    public static List<MatOfPoint> prepareContours(Mat img){
        List<MatOfPoint> contours = getContours(img);
        contours = getReducedContours(contours, NUM_TO_KEEP);
        return contours;
    }

    // повертає список контурів
    private static List<MatOfPoint> getContours(Mat mat){

        List<MatOfPoint> contours = new ArrayList<>();
        //знаходимо контури
        Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE   );
        return contours;
    }

    /* Проріджує контур
     * використовується алгоритм вісвалінгема-ваєта
     */
    private static List<MatOfPoint> getReducedContours(List<MatOfPoint> contours, int numToKeep){
        List<MatOfPoint> reducedContours = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint mop = new MatOfPoint();
            mop.fromList(PolySimplifier.reduceVW(contours.get(i).toList(), numToKeep));
            reducedContours.add(mop);
        }
        return reducedContours;
    }

    /**
     * transformation image from string to Mat
     * @param path
     * @param treshType for threshold
     * @return Mat
     */
    /*
    public static Mat prepareImage(String path, String treshType) {
        Mat image = Imgcodecs.imread(path, Imgcodecs.IMREAD_COLOR);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

        switch (treshType) {
            case "THRESH_TRIANGLE":
                Imgproc.threshold(image, image, -1, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_TRIANGLE);
                break;

            case "THRESH_OTSU":
                Imgproc.threshold(image, image, -1, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
                break;
            default:
                Imgproc.threshold(image, image, -1, 255, Imgproc.THRESH_BINARY);
                break;
        }
        return image;
    }*/
}
