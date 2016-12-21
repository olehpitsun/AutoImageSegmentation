package comparator.utils;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Admin on 13.02.2016.
 */
public class IsometricUtils {
    public Point getContourCenter(MatOfPoint contour) {
        Rect boundingRect = Imgproc.boundingRect(contour);

        Point contourCenter = new Point();
        contourCenter.x = boundingRect.x + (boundingRect.width / 2);
        contourCenter.y = boundingRect.y + (boundingRect.height / 2);

        return contourCenter;
    }

    public List<Point> rotate(List<Point> contour, double angle, Point center) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (int i = 0; i < contour.size(); i++) {
            double subX = (contour.get(i).x - center.x);
            double subY = (contour.get(i).y - center.y);

            Point newPoint = new Point();
            newPoint.x = center.x + subX * cos - subY * sin;
            newPoint.y = center.y + subX * sin + subY * cos;
            contour.set(i, newPoint);
        }

        return contour;
    }

    public double getAngle(Point a, Point b) {
        double cb = GeometryUtils.getEuclideanDistance(a, b);
        Point corner = new Point(b.x, a.y);

        double cd = GeometryUtils.getEuclideanDistance(a, corner);
        double cos = cd / cb;
        double angle = Math.toDegrees(Math.acos(cos));

        return a.y > b.y ? angle : -1 * angle;
    }

    public double getAngle(List<Point> contour) {
        double maxLength = 0;
        Point a = contour.get(0);
        Point b = contour.get(1);

        for (int z = 0; z < contour.size(); z++) {
            for (int y = 0; y < contour.size(); y++) {
                if (z != y) {
                    double length = GeometryUtils.getEuclideanDistance(contour.get(z), contour.get(y));
                    if (length > maxLength) {
                        maxLength = length;
                        a = contour.get(z);
                        b = contour.get(y);
                    }
                }
            }
        }

        return this.getAngle(a, b);
    }

    public List<Point> move(MatOfPoint contour, Point coordinatesOfNewCenter) {
        Point currentCenter = this.getContourCenter(contour);
        List<Point> contourList = contour.toList();

        double deltaX = currentCenter.x - coordinatesOfNewCenter.x;
        double deltaY = currentCenter.y - coordinatesOfNewCenter.y;

        for (int i = 0; i < contourList.size(); i++) {
            Point movedPoint = new Point();
            movedPoint.x = contourList.get(i).x - deltaX;
            movedPoint.y = contourList.get(i).y - deltaY;
            contourList.set(i, movedPoint);
        }

        return contourList;
    }

    public MatOfPoint listToMatOfPoint(List<Point> contour) {
        MatOfPoint mop = new MatOfPoint();
        mop.fromList(contour);
        return mop;
    }
}