package comparator.skeletonEstimator.Trees;

/**
 * Created by oleh on 05.06.2016.
 */
public class Trees {

    String branch;
    double A,B;
    double[][] points = new double[20][20];

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setA(double a) {
        A = a;
    }

    public double getA() {
        return A;
    }

    public void setB(double b) {
        B = b;
    }

    public double getB() {
        return B;
    }

    public void setPoints(double[][] points) {
        this.points = points;
    }

    public double[][] getPoints() {
        return points;
    }

    public Trees(String branch, Double a, Double b, double[][] arr){
        this.branch = branch;
        this.A = a;
        this.B = b;
        this.points = arr;
    }
}
