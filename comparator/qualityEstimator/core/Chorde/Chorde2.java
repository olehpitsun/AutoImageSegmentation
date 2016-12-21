package comparator.qualityEstimator.core.Chorde;

import comparator.qualityEstimator.core.Contour.Contour2;
import comparator.utils.GeometryUtils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baron on 27.12.2015.
 */
public class Chorde2 extends Chorde {
    private int chordePoint1;
    private int chordePoint2;
    private double angle;
    private double distance;
    private MatOfPoint maxPerpendicular;
    private Contour2 contour;

    public double getAngle(){
        return this.angle;
    }
    public void setMaxPerpendicular(MatOfPoint mop){
      this.maxPerpendicular = mop;
    }
    public MatOfPoint getMaxPerpendicular(){
        return this.maxPerpendicular;
    }
    public int getIndexOfPoint1(){
        return this.chordePoint1;
    }
    public int getIndexOfPoint2(){
        return this.chordePoint2;
    }
    public double getDistance(){
        return this.distance;
    }
    public MatOfPoint getMOP(){
        MatOfPoint mop = new MatOfPoint();
        List<Point> points = new ArrayList<>();
        points.add(this.contour.getPoint(this.chordePoint1));
        points.add(this.contour.getPoint(this.chordePoint2));
        mop.fromList(points);
        return  mop;
    }
    public Chorde2(){
    }
    public Chorde2(int a, int b, double dist, Contour2 contour){
        this.chordePoint1 = a;
        this.chordePoint2 = b;
        this.contour = contour;
        this.setEngle();
        this.distance = dist;
    }

    protected void setEngle(){
        List<Point> points = new ArrayList<>();
        points.add(this.contour.getPoint(this.chordePoint1));
        points.add(this.contour.getPoint(this.chordePoint2));

        Point hierPoint = points.get(0);
        Point lowerPoint = points.get(1);
        if(hierPoint.y >  points.get(1).y){
            hierPoint = points.get(1);
            lowerPoint =  points.get(0);
        }

        double lineWith = GeometryUtils.getEuclideanDistance(points.get(0),points.get(1));
        double perpendWith  = GeometryUtils.getEuclideanDistance(hierPoint, new Point(0, hierPoint.y));
        double oppositeSide =  GeometryUtils.getEuclideanDistance(lowerPoint,new Point(0,hierPoint.y));
        double cosEngle = ((Math.pow(lineWith,2) + Math.pow(perpendWith,2) - Math.pow(oppositeSide,2)) /
                ( 2 *lineWith * perpendWith ));
        this.angle =  Math.toDegrees(Math.acos(cosEngle));
    }
}
