package comparator.skeletonEstimator;

import comparator.skeletonEstimator.Trees.Branch;

/**
 * Created by oleh on 05.06.2016.
 */
public class BranchDivision {

    /**
     *
     * @param targetBranch масив точок 1 гілки
     * @param div_Num - число областей, на які треба ділити targetBranch
     * @return
     */
    public double[][] branchDiv(double[][] targetBranch, int div_Num){

        double X_temp = targetBranch[0][0];
        double[][] dividedBranch = new double[div_Num + 1][div_Num + 1];
        double delta = Math.abs((targetBranch[1][0] - targetBranch[0][0]) / div_Num);

        System.out.println("Delta = " + delta);System.out.println();

        dividedBranch[0][0] = targetBranch[0][0];
        dividedBranch[0][1] = targetBranch[0][1];
        System.out.println("X_" + 0 + " "+ + targetBranch[0][0] + " Y_" + 0 + " " + targetBranch[0][1]);

        for(int i = 1; i <= div_Num; i++){

            X_temp +=delta;
            double y =  Math.abs(Branch.getA()*X_temp + Branch.getB());
            dividedBranch[i][0] = X_temp;
            dividedBranch[i][1] = y;
            System.out.println("X_" + i + " "+ + dividedBranch[i][0] + " Y_" + i + " " + dividedBranch[i][1]);
        }
        return dividedBranch;
    }
}
