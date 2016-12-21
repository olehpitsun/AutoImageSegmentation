package comparator.frechet.core;

import comparator.utils.GeometryUtils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.List;

/**
 * Відстань на основі метрики Хаусдорфа
 * використовується повний перебір точок
 * Created by Vit on 07.02.2016.
 */
public class Distance {

    private double ca[][];
    private List<Point> contourA;
    private List<Point> contourB;

    public static double getFrechetDistance(MatOfPoint contour1, MatOfPoint contour2) {
        return new Distance().frechet(contour1.toList(), contour2.toList());
    }

    // just the discrete frechet distance algorithm.
    // don't worry about this code
    private double frechet(List<Point> contourA, List<Point> contourB) {
        int aSize = contourA.size();
        int bSize = contourB.size();
        this.contourA = contourA;
        this.contourB = contourB;

        this.ca = new double[aSize][bSize];

        for(int i=0; i<aSize; i++) {
            for(int j=0; j<bSize; j++) {
                this.ca[i][j] = -1;
            }
        }
        return this.c(aSize - 1, bSize - 1);
    }

    // and this too
    private double c(int i, int j) {
        if(this.ca[i][j] > -1) {
            return ca[i][j];
        } else if(i == 0 && j == 0) {
            ca[i][j] = GeometryUtils.getEuclideanDistance(this.contourA.get(0), this.contourB.get(0));
        } else if(i > 0 && j == 0) {
            ca[i][j] = Math.max(this.c(i - 1, 0), GeometryUtils.getEuclideanDistance(this.contourA.get(i), this.contourB.get(0)));
        } else if(i == 0 && j > 0) {
            ca[i][j] = Math.max(this.c(0, j - 1), GeometryUtils.getEuclideanDistance(this.contourA.get(0), this.contourB.get(j)));
        } else if(i > 0 && j > 0) {
            double min = Math.min(this.c(i - 1, j), this.c(i - 1, j - 1));
            min = Math.min(min, this.c(i, j - 1));
            this.ca[i][j] = Math.max(min, GeometryUtils.getEuclideanDistance(this.contourA.get(i), this.contourB.get(j)));
        } else {
            ca[i][j] = 1;
        }
        return ca[i][j];
    }
}