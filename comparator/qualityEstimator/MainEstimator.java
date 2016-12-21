package comparator.qualityEstimator;

import comparator.qualityEstimator.core.Contour.Contour;
import comparator.qualityEstimator.core.Contour.Contour2;
import comparator.qualityEstimator.core.Contour.Contour3;
import comparator.qualityEstimator.core.IsometricTranslation;
import comparator.utils.GeometryUtils;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Oleh7 on 2/8/2016.
 */
public class MainEstimator {

    public void Estimator(Mat im1, Mat im2){

        float distCoef = (float)0.5;
        float overlapCoef = (float)0.6;

        double[] dist1 = byLongestChorde(im1, im2);
        double[] dist2 = byLongestChordeAndPerpendicular(im1.clone(), im2.clone());
        double[] dist3 = bySecantMethod(im1.clone(), im2.clone());
        double[] dist4 = byListOfChordes(im1.clone(), im2.clone(),distCoef,overlapCoef);
        double[] dist5 = byFullSearch(im1.clone(), im2.clone());

        System.out.println("\n--------------------------------Isometric Translation----------------------------------");

        for(int i = 0; i< dist1.length; i++){
            System.out.println("longest chorde\t\t3 dots\t\t\t\tsecant method\tlist of chordes\t\tfull search");
            System.out.println(dist1[i] +"\t" + dist2[i] + "\t" + dist3[i] + "\t" + dist4[i] + "\t"+ dist5[i]);
        }
    }

    public static double[] byListOfChordes(Mat img1,Mat img2,float distCoef,float overlapCoef){
        List<Contour> etalonContours = GeometryUtils.getContours(img1.clone(), "Etalon", distCoef, overlapCoef);
        List<Contour> testContours = GeometryUtils.getContours(img2.clone(), "Test",distCoef, overlapCoef);

        double[] distValueArr = new double[etalonContours.size()];
        for(int i=0; i<etalonContours.size(); i++){
            double dist = IsometricTranslation.distByListOfChordes(etalonContours.get(i), testContours.get(i));
            distValueArr[i] = dist;
        }
        return distValueArr;

    }

    public static double[] byLongestChorde(Mat img1,Mat img2){
        List<Contour> etalonContours = GeometryUtils.getContours(img1.clone(), "Etalon", 1,0);
        List<Contour> testContours = GeometryUtils.getContours(img2.clone(), "Test",1,0);

        double[] distValueArr = new double[etalonContours.size()];
        for(int i=0; i<etalonContours.size(); i++) {
            double dist = IsometricTranslation.distByLongestChord(etalonContours.get(i), testContours.get(i));
            distValueArr[i] = dist;
        }
        return distValueArr;
    }

    public static double[] byLongestChordeAndPerpendicular(Mat img1,Mat img2) {
        List<Contour2>  etalonContours = GeometryUtils.getContours2(img1.clone(), "Etalon");
        List<Contour2> testContours = GeometryUtils.getContours2(img2.clone(), "Test");

        double[] distValueArr = new double[etalonContours.size()];
        for(int i=0; i<etalonContours.size(); i++) {
            double dist = IsometricTranslation.distByLongestChordAndPerpendiular(etalonContours.get(i), testContours.get(i));
            distValueArr[i] = dist;
        }
        return  distValueArr;
    }

    public static double[] bySecantMethod(Mat img1,Mat img2){
        List<Contour3>  etalonContours = GeometryUtils.getContours3(img1.clone(), "Etalon");
        List<Contour3>  testContours = GeometryUtils.getContours3(img2.clone(), "Test");

        double[] distValueArr = new double[etalonContours.size()];
        for(int i=0; i<etalonContours.size(); i++) {
            double dist = IsometricTranslation.distBySecantMethod(etalonContours.get(i), testContours.get(i));
            distValueArr[i] = dist;
        }
        return  distValueArr;
    }

    public static double[] byFullSearch(Mat img1, Mat img2){
        List<Contour>  etalonContours = GeometryUtils.getContours(img1.clone(), "Etalon", 1, 1);
        List<Contour>  testContours = GeometryUtils.getContours(img2.clone(), "Test", 1,1);

        double[] distValueArr = new double[etalonContours.size()];
        for(int i=0; i<etalonContours.size(); i++) {
            double dist = IsometricTranslation.distBy360Rotation(etalonContours.get(i), testContours.get(i));
            distValueArr[i] = dist;
        }
        return  distValueArr;
    }
}
