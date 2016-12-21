package comparator.qualityEstimator.core.Contour;

/**
 * Created by ihor on 14.11.2015.
 */

import comparator.qualityEstimator.core.Chorde.Chorde;
import comparator.utils.GeometryUtils;
import comparator.utils.PolySimplifier;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Contour
{
    final double EPSILON = 10;

    private MatOfPoint _contour;
    private MatOfPoint _convexContour;
    private List<Chorde> _chordes = new ArrayList<>();
    private int _chordesCount;
    private Chorde _maxChorde;
    private Point _centroid = new Point();
    private String _name;
    private Mat _image;
    private int _rows;
    private int _cols;
    private  double _overlapCoef;
    private  double _distCoef;

    public Mat getImage(){
        return this._image;
    }
    public int getChordesMaxCount(){
        return this._chordesCount;
    }
    public int getCols(){
        return  this._cols;
    }
    public int getRows(){
        return  this._rows;
    }
    public Chorde getMaxChorge(){
        return this._maxChorde;
    }
    public Chorde getChorde(int nomber){
        return this._chordes.get(nomber);
    }
    public double getPercentCoef(){
        return this._overlapCoef;
    }
    public double getDistCoef(){
        return  this._distCoef;
    }
    public int getChordesCount(){
        return this._chordes.size();
    }
    public String getName(){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }
    public MatOfPoint getContour(){
        return this._contour;
    }
    public MatOfPoint getConvexContour(){
        return this._convexContour;
    }
    public List<Chorde> getChordesList(){
        return this._chordes;
    }
    public Point getCentroid(){
        return this._centroid;
    }
    public Point getPointOfConvex(int i){
        List<Point> points = this._convexContour.toList();
        return points.get(i);
    }
    protected void setCentroid(){
        this._centroid =  GeometryUtils.getCentroid(this._contour);
    }

    public Contour(){
    }
    public Contour(Mat image,MatOfPoint contour, String name,double distCoef, double percentCoef)
    {
        this._distCoef = distCoef;
        this._overlapCoef = percentCoef;
        this._rows =  image.rows();
        this._cols =  image.cols();
        this._image = image;
        this._name = name;
        this._contour = new MatOfPoint();
        this._contour = PolySimplifier.reduceRDP(contour, EPSILON);
        this.removeNotConvex();
        this.setChordes();
        this._chordesCount = this._chordes.size();
        this.sortChordes();
        this.setCentroid();
    }

    public Contour(Contour contour)
    {
        this._distCoef = contour.getDistCoef();
        this._overlapCoef = contour.getPercentCoef();
        this._rows = contour.getRows();
        this._cols = contour.getCols();
        this._name = contour.getName();
        this._image = contour.getImage();
        this._contour = new MatOfPoint();
        this._contour = contour.getContour();
        this._convexContour = contour.getConvexContour();
        this._maxChorde = contour.getMaxChorge();
        this._chordes = contour.getChordesList();
        this._chordesCount = contour.getChordesMaxCount();
        this._centroid = contour.getCentroid();
    }

    private void sortChordes(){
        double maxDist =  this._maxChorde.getDistance();

        for(int i = 0; i<this._chordes.size();i++){
            Chorde currentChorde = this._chordes.get(i);
            this._chordes.get(i).setCoefficient(
                    (currentChorde.getDistance() / maxDist) +
                            (currentChorde.getIntercectPercent() / 100)
            );
        }
        this._maxChorde.setCoefficient(
                (this._maxChorde.getDistance()/maxDist) +
                        (this._maxChorde.getIntercectPercent()/100)
        );
        boolean is;
        do{
            is = false;
            for(int i = 0; i<this._chordes.size()-1;i++){
                Chorde currentChorde = this._chordes.get(i);
                Chorde nextChorde =  this._chordes.get(i+1);
                if(currentChorde.getCoefficient() < nextChorde.getCoefficient()){
                    this._chordes.set(i,nextChorde);
                    this._chordes.set(i+1,currentChorde);
                    is = true;
                }
            }
        }while (is);

        List<Chorde> tmpChordes = new ArrayList<>();
        Chorde firstChorde = this._chordes.get(0);
        for(int i = 0; i<this._chordes.size();i++){
            double overlapCoef = this._chordes.get(i).getPercentCoef();
            if(overlapCoef >= this._overlapCoef) {
                double distCoef = this._chordes.get(i).getDistance()/this.getMaxChorge().getDistance();
                if(distCoef >= this._distCoef) {
                    tmpChordes.add(this._chordes.get(i));
                }
            }
        }
        if(tmpChordes.size() == 0){
            tmpChordes.add(firstChorde);
        }
        this._chordes = tmpChordes;
    }

    private void setChordes ()
    {   double maxDist = 0;
        Chorde maxChorde = new Chorde(0,0,0,0,this);
        List <Point> points = this._convexContour.toList();
        for(int i = 0; i < points.size(); i++){
            for(int j = i + 2; j < points.size(); j++){
                if(i==0 && j == points.size()-1){continue;}
                List<Point> line = this.rasteredLine(this.getPointOfConvex(i), this.getPointOfConvex(j));
                double dist =  GeometryUtils.getEuclideanDistance(this.getPointOfConvex(i), this.getPointOfConvex(j));
                double percentCoef = getChordeIntersectionCoef(line);
                Chorde chorde = new Chorde(i,j,dist,percentCoef,this);
                if(dist > maxDist) {
                    maxDist = dist;
                    maxChorde = new Chorde(i,j,dist,percentCoef,this);
                }
                this._chordes.add(chorde);
            }
        }
        this._maxChorde = maxChorde;
    }

    protected double getChordeIntersectionCoef(List<Point> line){
        int pointNum = 0;
        for(int i = 0; i<line.size();i++){
            double[] rez = this.getImage().get((int) line.get(i).y, (int) line.get(i).x);
            if( rez[0]>0){
                pointNum++;
            }
        }
        return ((double)pointNum/(double)line.size());
    }

    public Contour rotate(double angle){
        this._contour = this._rotate(this._contour,angle);
        this._convexContour = this._rotate(this._convexContour,angle);
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

    private void  removeNotConvex(){
        Point[] points = this._contour.toArray();
        List<Point> resPoints = new ArrayList<>();
        MatOfInt hull = new MatOfInt();
        Imgproc.convexHull(this._contour, hull);
        int[] hullArray = hull.toArray();
        for(int i =0; i< hullArray.length ; i++){
            resPoints.add(points[hullArray[i]]);
        }
        MatOfPoint mop =  new MatOfPoint();
        mop.fromList(resPoints);
        this._convexContour = mop;
    }

    public Contour shift(double x, double y){
        this._contour = this._shift( x,  y,this._contour,false);
        this._convexContour = this._shift( x,  y,this._convexContour,true);
        return this;
    }


    protected   MatOfPoint _shift(double x, double y, MatOfPoint mop, boolean isConv){
        Point[] points = mop.toArray();
        Point[] resPoints = new Point[points.length];
        for(int i = 0; i < points.length; i++){
            double newX = points[i].x + x;
            double newY = points[i].y + y;
            resPoints[i] = new Point(newX,newY);
        }
        if(!isConv) {
            this._centroid = new Point(this._centroid.x + x, this._centroid.y + y);
        }
        MatOfPoint mop1 = new MatOfPoint();
        mop1.fromArray(resPoints);
        return mop1;
    }

    private List<Point> rasteredLine(Point p1, Point p2)
    {
        int x0 = (int)p1.x;
        int y0 = (int)p1.y;
        int x1 = (int)p2.x;
        int y1 = (int)p2.y;
        List<Point> linePoints = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x1 >= x0 ? 1 : -1;
        int sy = y1 >= y0 ? 1 : -1;

        if (dy <= dx)
        {
            int d = (dy << 1) - dx;
            int d1 = dy << 1;
            int d2 = (dy - dx) << 1;
            linePoints.add(new Point(x0, y0));
            for(int x = x0 + sx, y = y0, i = 1; i <= dx; i++, x += sx)
            {
                if ( d >0)
                {
                    d += d2;
                    y += sy;
                }
                else
                    d += d1;
                linePoints.add(new Point(x, y));
            }
        }
        else
        {
            int d = (dx << 1) - dy;
            int d1 = dx << 1;
            int d2 = (dx - dy) << 1;
            linePoints.add(new Point(x0, y0));
            for(int y = y0 + sy, x = x0, i = 1; i <= dy; i++, y += sy)
            {
                if ( d >0)
                {
                    d += d2;
                    x += sx;
                }
                else
                    d += d1;
                linePoints.add(new Point(x, y));
            }
        }
        return linePoints;
    }

}
