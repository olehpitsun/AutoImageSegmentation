import modules.ImageManagerModule;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import tools.StartImageParams;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by oleh on 24.12.2016.
 */
public class AutoSegmentation {

    private Mat currentImgMat;

    public AutoSegmentation(Mat currentImgMat){
        this.currentImgMat = currentImgMat;
    }

    public void start(StartImageParams stip){
        try {
            int priorityID = 0;//результуючий ID
            List<Integer> histogramIDs = Main.storeData.selectHistogramIDs(stip.getHistogramAverage());
            if(histogramIDs.size() > 0){
                for(int i = 0; i < histogramIDs.size(); i++){
                    priorityID = Main.storeData.selectBlueValue(histogramIDs.get(i), stip.getBlueValue());
                }
                if(priorityID != 0){
                    this.getSPecialValuesForImage(priorityID);
                }else {System.out.println("Blue value doesn't found");}

            }else {System.out.println("HistogramAverage value " + stip.getHistogramAverage() + " doesn't found");}
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
        Highgui.imwrite(Main.pathToImg + "OK___" + lowThreshValue + ".jpg", newImageMat);
    }

}
