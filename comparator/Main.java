package comparator;

import comparator.atallah.AtallahComparator;
import comparator.frechet.FrechetComparator;
import comparator.gromovFrechet.GromovFrechetComparator;
import comparator.gromovHausdorff.GromovHausdorffComparator;
import comparator.hausdorff.HausdorffComparator;
import comparator.utils.ImageOperations;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * тут все запускається.
 * Велике прохання не чіпати {@link comparator.Comparator} і {@link comparator.MainComparator}
 * Бажано не міняти коду взагалі, а просто додайте своє.
 * <p>
 * Created by Vit on 07.02.2016.
 */
public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static double distanceResult;


    public static void main(Mat image, Mat expert) {
        //System.out.println("Open CV version - " + Core.VERSION);

        //виклик функції порівняння скелетонів
        //skeletonEstimator();
        /*
        String expertImgName = "C:\\bioimg\\cytology20161114T070235Z\\cytology\\TS_04_25_15_44_56\\TS_04_25_15_44_56_expert.png";
        String thresholdImgName = "C:\\bioimg\\cytology20161114T070235Z\\cytology\\TS_04_25_15_44_56\\TS_04_25_15_44_56_threshold.png";
        String watershedImgName = "C:\\bioimg\\cytology20161114T070235Z\\cytology\\TS_04_25_15_44_56\\TS_04_25_15_44_56_watershed.png";

        //завантажуємо зображення
        Mat imgExpert = ImageOperations.prepareImage(expertImgName,"THRESH_TRIANGLE");
        Mat imgWatershed = ImageOperations.prepareImage(watershedImgName , "THRESH_TRIANGLE");
        Mat imgThreshold = ImageOperations.prepareImage(thresholdImgName, "THRESH_TRIANGLE");
*/
        // знаходимо і проріджуємо контури
        List<MatOfPoint> contoursExpert = ImageOperations.prepareContours(expert);
        List<MatOfPoint> contoursWatershed = ImageOperations.prepareContours(image);
        //List<MatOfPoint> contoursThreshold = ImageOperations.prepareContours(imgThreshold);

        System.out.println(contoursExpert.size() + ", " + contoursWatershed.size() );

        MainComparator mainComparator = new MainComparator();

        // тут додаэмо всі компаратори
        mainComparator.add(new HausdorffComparator());
        mainComparator.add(new AtallahComparator());
        mainComparator.add(new GromovHausdorffComparator());
        mainComparator.add(new FrechetComparator());
        mainComparator.add(new GromovFrechetComparator());

        // виводимо результат в консоль
        // повинно бути 0
        //System.out.println("expert" + " => " + "expert");
        //mainComparator.compare(contoursExpert, contoursExpert);

       // System.out.println("\n====================\n" + thresholdImgName + " => " + expertImgName);
        //mainComparator.compare(contoursExpert, contoursThreshold);

        //System.out.println("\n====================\n" + "image" + " => " + "expert");
        mainComparator.compare(contoursExpert, contoursWatershed);
        distanceResult = mainComparator.getResultDistance();
    }

    public static Double getDistance(){
        return distanceResult;
    }
    /*
    public static void skeletonEstimator(){

        HashMap<Integer,Trees> tree1 = new HashMap<Integer, Trees>();
        HashMap<Integer,Trees> tree2 = new HashMap<Integer,Trees>();

        double[][] Line_1 = {
                {297,452},
                {435,508},

        };

        double[][] Line_2 = {
                {342,102},
                {303,253},
                {277,268},
                {263,304},
                {311,351},
        };

        tree1.put(0, new Trees("R1=>R2", 0.4, 336.0, Line_1));
        tree1.put(1, new Trees("R1=>R3", 2.4, 44.0, Line_1));
        tree1.put(2, new Trees("R1=>R4", 3.4, 326.0, Line_1));

        tree2.put(0, new Trees("R1=>R2", 4.4, 336.0, Line_2));
        tree2.put(1, new Trees("R1=>R4", 5.4, 76.0, Line_2));

        for(int i = 0; i < tree1.size(); i++)
        {
            Trees branch_1 = tree1.get(i);
            double[][] branch_1arr = branch_1.getPoints();

            for(int j = 0; j < tree2.size(); j++)
            {
                Branch.setBranchName(branch_1.getBranch());
                Trees branch_2 = tree2.get(j);
                double[][] branch_2arr = branch_2.getPoints();

                if(branch_1arr.length > branch_2arr.length){
                    Branch.setA(branch_2.getA());
                    Branch.setB(branch_2.getB());
                }else{
                    Branch.setA(branch_1.getA());
                    Branch.setB(branch_1.getB());
                }

                Skeleton skeleton = new Skeleton(branch_1arr, branch_2arr);
            }
        }

    }*/
}
