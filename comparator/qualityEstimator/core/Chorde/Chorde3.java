package comparator.qualityEstimator.core.Chorde;

import comparator.utils.GeometryUtils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ihor on 14.01.2016.
 */
public class  Chorde3 {
    private List<Point> _chordePoints = new ArrayList<>();
    private double angle;
    private double distance;
    protected Point initialBasePoint;

    public double getAngle(){
        return this.angle;
    }
    public double getDistance(){
        return this.distance;
    }
    public MatOfPoint getChorde() {
        MatOfPoint mop = new MatOfPoint();
        mop.fromList(this._chordePoints);
        return mop;
    }

    public Chorde3(Point a, Point b,Point initialBasePoint){
        this._chordePoints.add(a);
        this._chordePoints.add(b);
        this.initialBasePoint = initialBasePoint;
        this.distance = GeometryUtils.getEuclideanDistance(a,b);
        this.setEngle();
    }

    public Chorde3(Chorde3 chorde3){
        this._chordePoints.add(new Point(chorde3._chordePoints.get(0).x,chorde3._chordePoints.get(0).y));
        this._chordePoints.add(new Point(chorde3._chordePoints.get(1).x,chorde3._chordePoints.get(1).y));
        this.initialBasePoint = new Point(chorde3.initialBasePoint.x,chorde3.initialBasePoint.y);
        this.distance = chorde3.getDistance();
        this.angle = chorde3.getAngle();
    }

    protected void setEngle(){
        Point hierPoint = this._chordePoints.get(0);
        Point lowerPoint = this._chordePoints.get(1);
        if(hierPoint.y >  this._chordePoints.get(1).y){
            hierPoint = this._chordePoints.get(1);
            lowerPoint =  this._chordePoints.get(0);
        }

        double lineWith = GeometryUtils.getEuclideanDistance(this._chordePoints.get(0),this._chordePoints.get(1));
        double perpendWith  = GeometryUtils.getEuclideanDistance(hierPoint, new Point(0, hierPoint.y));
        double oppositeSide =  GeometryUtils.getEuclideanDistance(lowerPoint,new Point(0,hierPoint.y));
        double cosEngle = ((Math.pow(lineWith,2) + Math.pow(perpendWith,2) - Math.pow(oppositeSide,2)) /
                ( 2 *lineWith * perpendWith ));
        if(this.initialBasePoint.y == lowerPoint.y && this.initialBasePoint.x == lowerPoint.x){
            this.angle =  Math.toDegrees(Math.acos(cosEngle));
        }else {
            this.angle =  180 + Math.toDegrees(Math.acos(cosEngle));
        }
    }

    public Chorde3 rotate(Point p, double angle){
        List<Point> resPoints = new ArrayList<>();
        for(int i = 0; i < _chordePoints.size(); i++){
            double newX = p.x + (_chordePoints.get(i).x - p.x) * Math.cos(Math.toRadians(angle)) -
                    (_chordePoints.get(i).y - p.y) * Math.sin(Math.toRadians(angle));
            double newY = p.y + (_chordePoints.get(i).y - p.y) * Math.cos(Math.toRadians(angle)) +
                    (_chordePoints.get(i).x - p.x) * Math.sin(Math.toRadians(angle));
            resPoints.add(i, new Point(newX,newY));
        }
        this._chordePoints = resPoints;
        return  this;
    }

    public Chorde3 shift(double x, double y){
        List<Point> resPoints = new ArrayList<>();
        for(int i = 0; i < this._chordePoints.size(); i++){
            double newX = _chordePoints.get(i).x + x;
            double newY =_chordePoints.get(i).y + y;
            resPoints.add(i, new Point(newX,newY));
        }
        this._chordePoints = resPoints;
        return this;
    }
}
