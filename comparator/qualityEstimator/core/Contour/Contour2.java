package comparator.qualityEstimator.core.Contour;

import comparator.qualityEstimator.core.Chorde.Chorde2;
import comparator.utils.GeometryUtils;
import comparator.utils.PolySimplifier;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ihor on 20.12.2015.
 */
public class Contour2 extends Contour {

    protected Mat _image;
    protected MatOfPoint _contour;
    protected String _name;
    private Chorde2 _maxChorde2;
    protected Point _centroid = new Point();

    public String getName(){
        return this._name;
    }
    public MatOfPoint getContour(){
        return this._contour;
    }
    public Point getCentroid(){
        return this._centroid;
    }
    public void setName(String name){
        this._name = name;
    }
    public Chorde2 getChorde(){
        return this._maxChorde2;
    }
    public Point getPoint(int i){
        List<Point> points = this._contour.toList();
        return points.get(i);
    }
    protected void setCentroid(){
        this._centroid =  GeometryUtils.getCentroid(this._contour);
    }

    public Contour2(){

    }
    public Contour2(Mat image,MatOfPoint contour, String name){
        this._contour = PolySimplifier.reduceRDP(contour, EPSILON);
        this._image = image;
        this._name = name;
        this.setMaxChorde();
        this.setPerpendicular(this.getChorde());
        this.setCentroid();
    }


    private void setMaxChorde () {
        double maxDist = 0;
        Chorde2 maxChorde = new Chorde2();
        List <Point> points = this._contour.toList();
        for(int i = 0; i < points.size(); i++){
            for(int j = i + 2; j < points.size(); j++){
                double dist =  GeometryUtils.getEuclideanDistance(this.getPoint(i), this.getPoint(j));
                if(dist > maxDist) {
                    maxDist = dist;
                    maxChorde = new Chorde2(i,j,dist,this);
                }
            }
        }
        this._maxChorde2 = maxChorde;
    }

    private void  setPerpendicular (Chorde2 ch2){
        List<Point> points = ch2.getMOP().toList();
        double middle_x = (points.get(0).x+points.get(1).x)/2;
        double middle_y = (points.get(0).y+points.get(1).y)/2;
        Point middle = new Point(middle_x,middle_y);
        ArrayList<Double> kb =  GeometryUtils.getStraightCoefficients(points.get(0), points.get(1));
        double k = -1 / kb.get(0);
        //  y = k(x-middle_x) + middle_y
        List<Point> contourPoints = this.getContour().toList();
        double maxPerpendicularDistance = 0;

        double xTmp = middle_x + ch2.getDistance();
        double yTmp = k*(xTmp - middle_x) + middle_y;
        Point tmpPoint = new Point(xTmp,yTmp);
        for (int i=0;i<contourPoints.size()-1;i++){
            Point p1 = GeometryUtils.getIntersectionPointOfLines(middle, tmpPoint, this.getPoint(i), this.getPoint(i + 1));
            if(p1 != null){
                ArrayList<Point> line = new ArrayList<>();
                line.add(0,middle);
                line.add(1,p1);
                MatOfPoint mop = new MatOfPoint();
                mop.fromList(line);
                double dist = GeometryUtils.getEuclideanDistance(middle,p1);
                if(dist > maxPerpendicularDistance){
                    maxPerpendicularDistance = dist;
                    ch2.setMaxPerpendicular(mop);
                }
            }
        }
    }

    public boolean isMiddleHigher(){
        Point middle = this.getChorde().getMaxPerpendicular().toList().get(0);
        Point p =  this.getChorde().getMaxPerpendicular().toList().get(1);
        if(middle.y <= p.y && middle.x <= p.x){
            return false;
        }
        else  return true;
    }

    public Contour2 shift(double x, double y){
        this._contour = this._shift( x,  y,this._contour,false);
        this._maxChorde2.setMaxPerpendicular(this._shift( x,  y,this._maxChorde2.getMaxPerpendicular(),false));
        this.setCentroid();
        return this;
    }

    public Contour2 rotate(double angle){
        this._contour = this._rotate(this._contour, angle);
        this._maxChorde2.setMaxPerpendicular( this._rotate(this._maxChorde2.getMaxPerpendicular(), angle));
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
