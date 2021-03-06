import modules.ImageManagerModule;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import tools.StartImageParams;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 24.12.2016.
 */
public class AutoSegmentation {

    private Mat currentImgMat;
    private int currentHistogram;

    public AutoSegmentation(Mat currentImgMat){
        this.currentImgMat = currentImgMat;
    }

    public void start(StartImageParams stip, int currentH){
        try {
            // якщо currentH дорівнює 0, то присвоїти this.currentHistogram поч. значення, інакше присвоїти this.currentHistogram = currentH;
            if(currentH == 0){
                this.currentHistogram = stip.getHistogramAverage();
            }else{
                this.currentHistogram = currentH;
            }


            System.out.println("this.currentHistogram "+this.currentHistogram);
            int priorityID = 0;//результуючий ID
            List<Integer> histogramIDs = Main.storeData.selectHistogramIDs(this.currentHistogram);//

            for(int i = 0; i < histogramIDs.size(); i++)
            {
                System.out.println("ID " + histogramIDs.get(i));
            }

            if(histogramIDs.size() > 0){
                for(int i = 0; i < histogramIDs.size(); i++){
                    priorityID = Main.storeData.selectBlueValue(histogramIDs.get(i), stip.getBlueValue());
                }
                if(priorityID != 0){
                    this.getSPecialValuesForImage(priorityID);
                }else {
                    System.out.println("Blue value doesn't found. hist value " + histogramIDs.get(0));
                    this.getSPecialValuesForImage(histogramIDs.get(0));
                }

            }else {
                System.out.println("HistogramAverage value " + stip.getHistogramAverage() + " doesn't found");
                this.start(stip, this.currentHistogram-1);
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void getSPecialValuesForImage(int priorityID) throws SQLException {
        System.out.println("Result " + Main.storeData.getLowThresh(priorityID));

        int lowThreshValue =  Main.storeData.getLowThresh(priorityID);
        Mat newImageMat = new Mat();
        this.currentImgMat.copyTo(newImageMat);

        /** ЗОБРАЖЕННЯ ПІСЛЯ ОБРОБКИ*/
        ImageManagerModule imageManagerModule = new ImageManagerModule();
        newImageMat = imageManagerModule.autoImageCorrection(newImageMat,lowThreshValue);

        /*** ПОРОГОВА СЕШМЕНТАЦІЯ
         * перетворення експертного зобр в градації сірого*/
        Imgproc.threshold(newImageMat, newImageMat, 200, 255, Imgproc.THRESH_BINARY);
        //Highgui.imwrite(Main.pathToImg + Main.imgName + "\\" + "result_" + lowThreshValue + ".jpg", newImageMat);

        this.localProcessing(newImageMat);
    }

    private void localProcessing(Mat img5){

        System.out.println( "start");

        Mat src_gray = img5;
        src_gray.convertTo(src_gray, CvType.CV_32SC1);



        //Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));

        Mat canny_output = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(src_gray, contours, new Mat(), Imgproc.RETR_FLOODFILL, Imgproc.CHAIN_APPROX_SIMPLE);

// Draw all the contours such that they are filled in.
        Mat contourImg = new Mat();
        for (int i = 0; i < 15; i++) {

            Imgproc.drawContours(src_gray, contours, i, new Scalar(255, 0, 255),6);
        }

        Highgui.imwrite(Main.pathToImg + Main.imgName + "\\" + "result_" + 768 + ".jpg", src_gray);

System.out.println( "Fin");
    }

}
