package comparator.hausdorff.core;

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

    public static double getHausdorffDistance(MatOfPoint contour1, MatOfPoint contour2) {
        // визначаэмо максимальне відхилення контурів
        return Math.max(getDeviation(contour1.toList(), contour2.toList())
                , getDeviation(contour2.toList(), contour1.toList()));
    }

    // відхилення точки від контура (для Хаусдорфа), повертає відстань найменшу выдстань до множини
    private static double getDeviation(Point p, List<Point> contour) {

        double minDistance = Double.MAX_VALUE;      // мінімальна відстань по Евкліду, від p до contour

        // визначаэмо відстань від вхідної точки до кожної точки контура
        // і зберігаємо найменшу
        for (int i = 0; i < contour.size(); i++) {
            double distance = GeometryUtils.getEuclideanDistance(p, contour.get(i));

            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    // відхилення контура від контура (для Хаусдорфа), повертає відстань
    // порядок аргументів має значення getDeviation(c1, c2) != getDeviation(c2, c1)
    private static double getDeviation(List<Point> contour1, List<Point> contour2) {

        double maxDistance = 0;             // максимальна відстань по Евкліду, від c1 до с2

        // визначаємо відхилення кожної точки c1 до с2
        // зберігаємо найбільшу
        for (int i = 0; i < contour1.size(); i++) {

            // визначаємо відхилення точки від контура
            double distance = Distance.getDeviation(contour1.get(i), contour2);

            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        return maxDistance;
    }


}
