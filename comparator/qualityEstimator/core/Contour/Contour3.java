package comparator.qualityEstimator.core.Contour;

import comparator.qualityEstimator.core.Chorde.Chorde3;
import comparator.utils.GeometryUtils;
import comparator.utils.PolySimplifier;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ihor on 10.01.2016.
 */
public class Contour3 extends Contour2 {
    private List<Chorde3> _chordes = new ArrayList<>();

    public List<Chorde3> getChordes(){
        return this._chordes;
    }

    public Contour3(Mat image, MatOfPoint contour, String name){
        this._contour = PolySimplifier.reduceRDP(contour, EPSILON);
        this._image = image;
        this._name = name;
        this.setCentroid();
        this.setChordes(15);
        int a = 0;
    }

    public Contour3(Contour3 contour3){
        this._contour = contour3.getContour();
        this._image = contour3._image;
        this._name = contour3.getName();
        this._centroid = contour3.getCentroid();
        for (int i = 0; i < contour3.getChordes().size(); i++){
            this._chordes.add(new Chorde3(contour3.getChordes().get(i)));
        }
    }

    private void setChordes(int rotationAngle) {
        List<Point> contourPoints = this._contour.toList();
        Point initialBasePoint = getInitialBasePoint(contourPoints);   //������������ ����� �� ������ ���
        Point initialPoint = getInitialPoint(contourPoints);            //���������

        for(int i = rotationAngle; i <= 180;i+=rotationAngle){
            Point rotatedPoint = this._rotatePoint(initialBasePoint, initialPoint, i);
            Point intercectionPoint = this.getIntersectionPoint(initialBasePoint, rotatedPoint);
            if(intercectionPoint.x != 0 && intercectionPoint.y != 0 && GeometryUtils.getEuclideanDistance(initialBasePoint,intercectionPoint) >  20){
                this._chordes.add(new Chorde3(initialBasePoint, intercectionPoint,initialBasePoint));
            }
        }

    }

    private Point getIntersectionPoint( Point initialBasePoint, Point RotatedPoint){
        List<Point> contourPoints = _contour.toList();
        double maxDist = 0;
        Point p = new Point();
        for (int i=0;i < contourPoints.size() - 1 ;i++){
            Point p1 = GeometryUtils.getIntersectionPointOfLines(initialBasePoint, RotatedPoint, contourPoints.get(i), contourPoints.get(i + 1));
            if(p1 != null){
                double dist = GeometryUtils.getEuclideanDistance(initialBasePoint,p1);
                if(maxDist < dist){
                    p = p1;
                    maxDist = dist;
                }
            }
        }
        return p;
    }

    private Point getInitialBasePoint(List<Point> contourPoints){
        Point centroid = this.getCentroid();
        Point initialPoint = new Point();
        double dist = 0;
        for(int i = 0; i < contourPoints.size(); i++){
            double tmpDist = GeometryUtils.getEuclideanDistance(centroid, contourPoints.get(i));
            if(tmpDist > dist){
                dist = tmpDist;
                initialPoint = contourPoints.get(i);
            }
        }
        return  initialPoint;
    }

    private Point getInitialPoint(List<Point> contourPoints){
        Point centroid = this.getCentroid();
        Point initialPoint = new Point();
        double dist = 999999999;
        for(int i = 0; i < contourPoints.size(); i++){
            double tmpDist = GeometryUtils.getEuclideanDistance(centroid,contourPoints.get(i));
            if(tmpDist < dist){
                dist = tmpDist;
                initialPoint = contourPoints.get(i);
            }
        }
        return  initialPoint;
    }

    protected Point _rotatePoint(Point base,Point p,double angle){
        double newX = base.x + (p.x - base.x) * Math.cos(Math.toRadians(angle)) -
                    (p.y - base.y) * Math.sin(Math.toRadians(angle));
        double newY = base.y + (p.y - base.y) * Math.cos(Math.toRadians(angle)) +
                    (p.x - base.x) * Math.sin(Math.toRadians(angle));
        return new Point(newX,newY);
    }

    public Contour3 rotate(double angle){
        for (int i = 0; i < getChordes().size();i ++) {
            this.getChordes().get(i).rotate(this._centroid,angle);
        }
        this._contour = this._rotate(this._contour, angle);
        return  this;
    }

    public Contour3 shift(double x, double y){
        for (int i = 0; i < getChordes().size();i ++) {
            this.getChordes().get(i).shift(x, y);
        }
        this._contour = this._shift( x,  y,this._contour,false);
        this.setCentroid();
        return this;
    }

    protected MatOfPoint _rotate(MatOfPoint mop,double angle){
        Point[] points = mop.toArray();
        Point[] resPoints = new Point[points.length];
        for(int i = 0; i < points.length; i++){
            double newX = this._centroid.x + (points[i].x - _centroid.x) * Math.cos(Math.toRadians(angle)) -
                    (points[i].y - this._centroid.y) * Math.sin(Math.toRadians(angle));
            double newY = _centroid.y + (points[i].y - _centroid.y) * Math.cos(Math.toRadians(angle)) +
                    (points[i].x - _centroid.x) * Math.sin(Math.toRadians(angle));
            resPoints[i] = new Point(newX,newY);
        }
        MatOfPoint mop1 = new MatOfPoint();
        mop1.fromArray(resPoints);
        return mop1;
    }
}
