package tools;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by oleh on 15.12.2016.
 */
public class ImageSettings {

    private Mat original, expert;

    /**
     *
     * @param original - вхідне оригвнальен зображення
     * @param expert - маска (зображення, оброблене експертом)
     */
    public ImageSettings(Mat original, Mat expert){
        this.original = original;
        this.expert = expert;
    }

    /**
     *
     * @return зображення, підігнане під експерта
     */
    public Mat getResizedImageMat(){

        Rect rectangle = new Rect(0, 0, this.expert.cols(), this.expert.rows());
        Mat newOriginMat = this.original.submat(rectangle);
        return newOriginMat;
    }
}
