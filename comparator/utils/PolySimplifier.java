package comparator.utils;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Методи для прорідження контура
 */
public class PolySimplifier {

    // викидає точки з масиву з певним кроком
    public static List<Point> reduceSimple(List<Point> points, int step) {
        List<Point> tempPointList = new ArrayList<>();
        for (int i = 0; i < points.size(); i += step) {
            tempPointList.add(points.get(i));
        }
        return tempPointList;
    }

    /*
        алгоритм Дугласа-Рамера-Пекера
        epsilon - максимальна довжина відхилення від перпендикуляра (чим більше тим більше спрощення)
     */
    public static List<Point> reduceRDP(List<Point> inputPointList, double epsilon) {
        List<Point> outputPointList = new ArrayList<>();

        int startSegIndex = 0;                  // індекс точки початку відрізка
        int endSegIndex = inputPointList.size() - 1;     // індекс точки кінця відрізка

        double dMax = 0;                        //максимальна перпендикуляна відстань
        int maxDistancePointIndex = 0;          // індекс точки до якої відстань найбільша

        // шукаємо точку з найбільшою перпендикуялрною відстанню
        for (int i = startSegIndex; i < endSegIndex ; i++) {

            Point currentPoint = inputPointList.get(i);
            // обчислюємо для кожної точки вхідної фігури перпендикулярну відстань до відрізка
            Point perpendicularBasePoint = GeometryUtils.getPerpendicularBasePoint(currentPoint
                    , inputPointList.get(startSegIndex), inputPointList.get(endSegIndex));

            // якщо p == null то неможливо провести перпендикуляр
            if (perpendicularBasePoint == null)
                continue;

            // відстань від поточної точки до основи перпендикуляра
            double distance = GeometryUtils.getEuclideanDistance(currentPoint, perpendicularBasePoint);
            //System.out.println("distance = " + distance);

            if (distance > dMax) {
                dMax = distance;
                maxDistancePointIndex = i;
            }
        }

        // якщо максимальна відстань більша за epsilon то рукурсивно визиваємо функцію на ділянках
        if(dMax > epsilon){
            // ділимо поточну ламану на дві частини
            List<Point> pointList1Half = reduceRDP(inputPointList.subList(startSegIndex, maxDistancePointIndex), epsilon);
            List<Point> pointList2Half = reduceRDP(inputPointList.subList(maxDistancePointIndex, endSegIndex), epsilon);

            outputPointList.addAll(pointList1Half);
            outputPointList.addAll(pointList2Half);
        } else {
            // додаємо тільки першу і останню точку ламаної
            outputPointList.add(inputPointList.get(startSegIndex));
            outputPointList.add(inputPointList.get(endSegIndex));
        }
        return outputPointList;
    }

    public static MatOfPoint reduceRDP(MatOfPoint mop, double epsilon){
        List<Point> points = PolySimplifier.reduceRDP(mop.toList(),epsilon);
        MatOfPoint res = new MatOfPoint();
        res.fromList(points);
        return  res;
    }

    /*
        Алгоритм Visvalingam-Whyatt
        numberToKeep - кількість точок у вихідній ламаній
     */
    public static List<Point> reduceVW(List<Point> inputPointList, int numberToKeep){
        int numberToRemove = inputPointList.size() - numberToKeep;
        ArrayList<Point> outputPointList = new ArrayList<>(inputPointList);

        // якщо кількість точок для видалення <= 0, то повертаємо вхідну ламану
        if(numberToRemove <= 0){
            return outputPointList;
        }

        // якщо кількість точок у ламній менше 3, повертаємо вхідну ламану
        if(outputPointList.size() <= 3){
            return outputPointList;
        }

        // видаляэмо вказану кількість точок
        for(int i=0; i < numberToRemove; i++){

            int minIndex = 0;           // ідекс точки для якої площо трикутника найменша
            double minArea = GeometryUtils.getTriangleArea(outputPointList.get(0),
                    outputPointList.get(1), outputPointList.get(2));

            // шукаємо точку з найменшою площою
            // після видалення точки, потрібно знову перераховувати площі для кожної точки ламаної
            for(int j=2; j < outputPointList.size() - 1; j++){
                //обчилюємо площі для всі наступних точок в ламаній
                double area = GeometryUtils.getTriangleArea(outputPointList.get(j-1),
                        outputPointList.get(j),outputPointList.get(j+1));

                if(area < minArea){
                    minIndex = j;
                    minArea = area;
                }
            }
            // видаляємо точку для якої площа найменша
            outputPointList.remove(minIndex);
        }

        //System.out.println("VW reduce. num of points = " + outputPointList.size());
        return outputPointList;
    }
}
