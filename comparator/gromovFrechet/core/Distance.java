package comparator.gromovFrechet.core;

import comparator.Comparator;
import comparator.frechet.FrechetComparator;
import comparator.utils.IsometricUtils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.List;

/**
 * Відстань на основі метрики Хаусдорфа
 * використовується повний перебір точок
 * Created by Vit on 07.02.2016.
 */
public class Distance {

    public static double getGromovFrechetDistance(MatOfPoint contour1, MatOfPoint contour2) {

        IsometricUtils isometricUtils = new IsometricUtils();
        List<Point> contourA = contour1.toList();
        List<Point> contourB = contour2.toList();

        Point contour1Center = isometricUtils.getContourCenter(contour1);
        double contour1Angle = isometricUtils.getAngle(contourA);

        isometricUtils.rotate(contourA, contour1Angle, contour1Center);

        isometricUtils.move(contour2, contour1Center);
        double contour2Angle = isometricUtils.getAngle(contourB);
        isometricUtils.rotate(contourB, contour2Angle, contour1Center);

        contour1 = isometricUtils.listToMatOfPoint(contourA);
        contour2 = isometricUtils.listToMatOfPoint(contourB);

        Comparator frechet = new FrechetComparator();
        return frechet.getDistance(contour1, contour2);
    }
}