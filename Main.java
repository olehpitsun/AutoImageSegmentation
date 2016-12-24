import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.SegmentationResults;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import tools.DBParameters;
import tools.StartImageParams;
import lib.DB.storeData.storeData;
import java.util.Scanner;

/**
 * Created by oleh on 21.12.2016.
 */
public class Main {

    public static String pathToImg = "C:\\bioimg\\testsegmentation\\watershed\\auto\\";
    public static String imgName = "TS_06_19_13_17_42";
    public static storeData storeData;
    public static ObservableList<SegmentationResults> segmentationResults = FXCollections.observableArrayList();

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        DBParameters.setConnectionValues();//вхідні дані для підключення до БД
        storeData = new storeData(); //sql  connect

        /*** ОРИГІНАЛЬНЕ ЗОБРАЖЕННЯ*/
        String imagePath = pathToImg + imgName + "\\" + imgName + ".bmp";
        Mat originalMat = Highgui.imread(imagePath); // зчитування зображення

        /*** ВХІДНІ ДАНІ ЗОБРАЖЕННЯ*/
        StartImageParams stip = new StartImageParams();
        stip.getStartValues(originalMat);

        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equals("exit")) {
            System.out.println("Select mode: \n 1 - teaching \n 2 - auto segmentation \n exit - exit");
            String mode = scanner.nextLine();
            switch (mode){
                case "1" :
                    Teaching teaching = new Teaching(originalMat);
                    teaching.generateImages(stip);
                    teaching.setOutputValues();
                    break;
                case "2" :
                    AutoSegmentation autoSegmentation = new AutoSegmentation(originalMat);
                    autoSegmentation.start(stip);
                    break;
            }
        }
    }
}
