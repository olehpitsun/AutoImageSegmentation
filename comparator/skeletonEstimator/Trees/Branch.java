package comparator.skeletonEstimator.Trees;

/**
 * Created by oleh on 05.06.2016.
 */
public class Branch {

    public static Double A, B;
    public static String branchName;

    public static void setA(Double A1){
        A = A1;
    }
    public static Double getA(){
        return A;
    }

    public static void setB(Double B1){
        B = B1;
    }
    public static Double getB(){
        return B;
    }

    public static void setBranchName(String branchName) {
        Branch.branchName = branchName;
    }
    public static String getBranchName() {
        return branchName;
    }
}
