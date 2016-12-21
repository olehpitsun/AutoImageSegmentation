package comparator.atallah.core;

import comparator.utils.GeometryUtils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Алгоритм Аталаха
 *
 * Created by Vit on 08.02.2016.
 */
public class Distance {

    /*
     Модифікований алгоритм пошуку відстані Хаусдорфа
     Обов'язково для розділених багатокутників (таких що не перетинаються і один не містаться в іншому)
  */
    public static double getHausdorffDistanceModDisjoint(MatOfPoint contour1, MatOfPoint contour2) {
        return Math.max(getAttalahDisjointDeviation(contour1.toList(), contour2.toList())
                , getAttalahDisjointDeviation(contour2.toList(), contour1.toList()));
    }

    // Повертає відхилення однієї множини від іншої
    // використовується алгоритм Аталаха для РОЗДІЛЕНИХ багатокутників
    private static double getAttalahDisjointDeviation(List<Point> contour1, List<Point> contour2) {

        contour1 = new LinkedList<Point>(contour1);
        contour2 = new LinkedList<Point>(contour2);

        double distance = 0;
        //індекс точки для якої відстань Хаусдорфа є найменшою на попередній ітерації
        int prevC2Index = 0;
        Point prevC2Point;

        /*
        Фаза 1 шукаємо найменшу відстань для першої точки
        стандартним способом
         */
        double minDistance = Double.MAX_VALUE;     // мінімальна відстань для першої фази
        for (int i = 0; i < contour2.size(); i++) {
            double d = GeometryUtils.getEuclideanDistance(contour1.get(0), contour2.get(i));
            if (d < minDistance) {
                minDistance = d;
                prevC2Index = i;
            }
        }

        distance = minDistance;
        prevC2Point = contour2.get(prevC2Index);

        /*
        Фаза 2: шукаємо відстані використовуючи алгоритм Аталаха
         */

        for (int i = 1; i < contour1.size(); i++) {
            Point curPointFromC1 = contour1.get(i);

            boolean clockwise = false;      // прапорець обходу контура
            boolean isNeedForSearch = true; // чи потрібно шукати точку для якої відстань найменша

            // визначаємо по яку сторону від вектора лежить поточна точка
            GeometryUtils.Position pos =
                    GeometryUtils.getPointPosition(prevC2Point, curPointFromC1, contour1.get(i - 1));

            if (GeometryUtils.Position.LIES_ON.equals(pos) || GeometryUtils.Position.AHEAD.equals(pos)) {
                // поточна точка з С2 буде мати найменшу відстань
                isNeedForSearch = false;
            } else if (GeometryUtils.Position.LEFT.equals(pos)) {
                // обхід за годинниковою стрілкою
                clockwise = false;
            } else {
                // обхід за проти годинникової
                clockwise = true;
            }

            if (isNeedForSearch) {
                // шукаємо точку на другому контурі, для якої відстань найменша
                int curC2Index = prevC2Index;
                for (int j = 0; j < contour2.size(); j++) {

                    boolean isCurPointShortest =
                            GeometryUtils.getScalarProduct(
                                    prevC2Point
                                    , curPointFromC1
                                    , prevC2Point
                                    , contour2.get(increment(curC2Index, contour2.size()))) < 0 &&
                                    GeometryUtils.getScalarProduct(
                                            prevC2Point
                                            , curPointFromC1
                                            , prevC2Point
                                            , contour2.get(decrement(curC2Index, contour2.size()))) < 0;

                    // якщо скалярний добуток вектора Хаусдорфа і кожного з ребер контура менший нуля
                    // то ця точка буде мати найменшу відстань
                    if (isCurPointShortest) {
                        break;
                    }

                    // якщо можна провести перендикуляра з C1 до поточного ребра С2
                    // то основа перпендикуляра - точка з найменшою відстанню
                    Point perpendicularBase = GeometryUtils.getPerpendicularBasePoint(curPointFromC1, prevC2Point
                            , contour2.get(getNextVertexIndex(curC2Index, contour2.size(), clockwise)));

                    if (perpendicularBase != null) {
                        if (!clockwise) {
                            curC2Index++;
                        }
                        contour2.add(curC2Index, perpendicularBase);
                        break;
                    }
                    curC2Index = getNextVertexIndex(curC2Index, contour2.size(), clockwise);
                    prevC2Point = contour2.get(curC2Index);
                }

                prevC2Index = curC2Index;
                prevC2Point = contour2.get(curC2Index);
            }

            // шукаємо відстань до точки
            double d = GeometryUtils.getEuclideanDistance(curPointFromC1, prevC2Point);
            if (d > distance) {
                distance = d;
            }
        }
        return distance;
    }


    // повертає наступну точку контура
    private static int getNextVertexIndex(int curPosition, int polySize, boolean clockwise) {
        return clockwise ? decrement(curPosition, polySize) : increment(curPosition, polySize);
    }

    // збільшує число на 1 по заданому модулю
    private static int increment(int value, int maxPossible) {
        return ((value + maxPossible + 1) % maxPossible);
    }

    // зменшує число на 1 по заданому модулю
    private static int decrement(int value, int maxPossible) {
        return ((value + maxPossible - 1) % maxPossible);
    }


}