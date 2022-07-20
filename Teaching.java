import EvaluateMethods.FRAG;
import modules.ImageManagerModule;
import objects.SegmentationResults;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import tools.ImageSettings;
import tools.StartImageParams;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by oleh on 21.12.2016.
 */
public class Teaching {

    private String expertImgPath = Main.pathToImg + Main.imgName + "\\" + "expert.png";/*** ЕКСПЕРТНЕ ЗОБРАЖЕННІ*/
    private Mat expertImgMat; // експертне зображення
    private Mat normalSegmentedImgMat; // підігнане по розміру оригінальне зображення

    public Teaching(Mat originalMat){

        this.expertImgMat = Highgui.imread(expertImgPath); // зчитування експертного зображення
        this.normalImageSize(originalMat);

        Imgproc.cvtColor(this.expertImgMat, this.expertImgMat, Imgproc.COLOR_RGB2GRAY); // перетворення зобр в градації сірого
    }

    /**
     ПІДІГНАТИ РОЗМІРИ ЗОБРАЖЕНЬ
     * @param originalMat - оригінальне RGB зображення
     */
    private void normalImageSize(Mat originalMat){
        ImageSettings imageSettings = new ImageSettings(originalMat, this.expertImgMat);
        Mat normalSegmentedImgMat = imageSettings.getResizedImageMat();
        this.normalSegmentedImgMat = normalSegmentedImgMat;
    }


    /**
     * Генерація зображень з різними вхідними даними
     * @param stip - вхідні дані
     */
    public void generateImages(StartImageParams stip){

        long start = System.currentTimeMillis();

        List<Integer> lowTreshValue = Arrays.asList(65,75,80,85,90,95, 100, 105);

        int lastID = setInputValues(stip.getHistogramAverage(), stip.getRedValue(), stip.getGreenVlue(), stip.getBlueValue());

//        for(int i = 0; i < lowTreshValue.size(); i++)
//        {
//            Mat newImageMat = new Mat();
//            this.normalSegmentedImgMat.copyTo(newImageMat);
//
//            /** ЗОБРАЖЕННЯ ПІСЛЯ ОБРОБКИ*/
//            ImageManagerModule imageManagerModule = new ImageManagerModule();
//            newImageMat = imageManagerModule.autoImageCorrection(newImageMat,lowTreshValue.get(i));
//
//            /*** ПОРОГОВА СЕШМЕНТАЦІЯ
//             * перетворення експертного зобр в градації сірого*/
//            Imgproc.threshold(newImageMat, newImageMat, 200, 255, Imgproc.THRESH_BINARY);
//            Highgui.imwrite(Main.pathToImg + Main.imgName + "\\" + i + "___" + lowTreshValue.get(i) + ".jpg", newImageMat);
//
//            /*** ПОРІВНЯННЯ ЗОБРАЖЕНЬ*/
//            System.out.println("\n====================\n lowTreshValue = " + lowTreshValue.get(i) );
//            //comparator.Main.main(newImageMat, this.expertImgMat);
//
//            /** FRAG*/
//            FRAG frag = new FRAG(newImageMat, expertImgMat);
//            System.out.println("\nFRAG = " + frag.getResult());
//
//            Main.segmentationResults.add(new SegmentationResults(lastID, Main.imgName, lowTreshValue.get(i),
//                    comparator.Main.getDistance(),frag.getResult(),0));
//        }

        System.out.println("Start " + System.currentTimeMillis());
        executeQueries();


    }

    public  void executeQueries() {

        //List<Integer> lowTreshValue = Arrays.asList(65,75,80,85,90,95, 100, 105);

        Integer lowTreshValue[] = {65,75,80,85,90,95, 100, 105};

        ExecutorService threadPool = Executors.newFixedThreadPool(lowTreshValue.length);
        // submit jobs to be executing by the pool
        for (int i = 0; i < lowTreshValue.length; i++) {
            final int j = i;
            threadPool.submit(() -> executeQuery(j));
        }
        // once you've submitted your last job to the service it should be shut down
        threadPool.shutdown();
        // wait for the threads to finish if necessary
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  void executeQuery(final int index) {

        System.out.println("result = " + index);
        Integer lowTreshValue[] = {65,75,80,85,90,95, 100, 105};
        Mat newImageMat = new Mat();
        this.normalSegmentedImgMat.copyTo(newImageMat);

        /** ЗОБРАЖЕННЯ ПІСЛЯ ОБРОБКИ*/
        ImageManagerModule imageManagerModule = new ImageManagerModule();
        newImageMat = imageManagerModule.autoImageCorrection(newImageMat,lowTreshValue[index]);

        /*** ПОРОГОВА СЕШМЕНТАЦІЯ
         * перетворення експертного зобр в градації сірого*/
        Imgproc.threshold(newImageMat, newImageMat, 200, 255, Imgproc.THRESH_BINARY);
        Highgui.imwrite(Main.pathToImg + Main.imgName + "\\" + index + "___" + lowTreshValue[index] + ".jpg", newImageMat);

        /*** ПОРІВНЯННЯ ЗОБРАЖЕНЬ*/
        System.out.println("\n====================\n lowTreshValue = " + lowTreshValue[index] );
        //comparator.Main.main(newImageMat, this.expertImgMat);

        /** FRAG*/
        FRAG frag = new FRAG(newImageMat, this.expertImgMat);
        System.out.println("\nFRAG = " + frag.getResult());

        long end = System.currentTimeMillis();

        System.out.println("End Time = " + end);

        //Main.segmentationResults.add(new SegmentationResults(lastID, Main.imgName, lowTreshValue.get(i),
                //comparator.Main.getDistance(),frag.getResult(),0));
    }



    public void setOutputValues() {

        /**
         int index = 0; // індекс елемента в колекції
         double minDistance = Main.segmentationResults.get(0).distance;
         for(int i = 1; i < Main.segmentationResults.size(); i++ ){
         if(Main.segmentationResults.get(i).distance < minDistance){
         minDistance = Main.segmentationResults.get(i).distance;
         index = i;
         }
         }
         storeResultDataToDB (index);// зберігання запису в БД
         **/
        setHumanValue();
        Main.segmentationResults.clear();
    }

    /**
     * Вивід повідомлення для вводу номеру кращого зображення
     */
    private void setHumanValue(){
        System.out.println("Введіть номер найкращого зображення ");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        storeResultDataToDB(num, num);
    }
    /**
     * анесення результатів до БД
     * @param index - індекс запису в колекції
     */
    private void storeResultDataToDB(int index){
        Main.storeData.insertOutputValues(Main.segmentationResults.get(index).lastID, Main.segmentationResults.get(index).imgName,
                Main.segmentationResults.get(index).lowThresh, Main.segmentationResults.get(index).distance,
                (double)Main.segmentationResults.get(index).FRAG,0 );
    }

    /**
     * @param index - індекс запису в колекції
     * @param humanV - оцінка зображення людиною
     */
    private void storeResultDataToDB(int index, int humanV){
        Main.storeData.insertOutputValues(Main.segmentationResults.get(index).lastID, Main.segmentationResults.get(index).imgName,
                Main.segmentationResults.get(index).lowThresh, Main.segmentationResults.get(index).distance,
                (double)Main.segmentationResults.get(index).FRAG,humanV );
    }
    /**
     * Додати вхідні параметри в БД
     * вивести останній ід
     */
    private static int setInputValues(double histogramAverage, double redValue, double greenVlue, double blueValue) {
        return Main.storeData.insertInputValues(histogramAverage, redValue, greenVlue, blueValue);
    }


}