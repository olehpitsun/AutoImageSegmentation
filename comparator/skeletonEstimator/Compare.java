package comparator.skeletonEstimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalDouble;

/**
 * Created by oleh on 05.06.2016.
 */
public class Compare {

    private double distance;
    private double[] distances = new double[4];

    /**
     * @param branch1 - гілка 1 скелетону
     * @param branch2 - гілка 3 скелетону
     * @param r - номер області
     * @return maxValue - макс. значення серед 4
     */
    public Double OneToOne(double[][] branch1, double[][] branch2, int r){

        distances[0] = Math.sqrt(Math.pow((branch2[r-1][0] - branch1[r-1][0]),2) + Math.pow((branch2[r-1][1] - branch1[r-1][1]),2));
        distances[1] = Math.sqrt(Math.pow((branch2[r-1][0] - branch1[r][0]),2) + Math.pow((branch2[r-1][1] - branch1[r][1]),2));
        distances[2] = Math.sqrt(Math.pow((branch2[r][0] - branch1[r][0]),2) + Math.pow((branch2[r][1] - branch1[r][1]),2));
        distances[3] = Math.sqrt(Math.pow((branch2[r][0] - branch1[r-1][0]),2) + Math.pow((branch2[r][1] - branch1[r][1]),2));

        OptionalDouble maxValue = Arrays.stream(distances).max();

        return maxValue.getAsDouble();
    }

    /**
     * @param branch1 - гілка 1 скелетону
     * @param branch2 - гілка 2 скелетону
     * @param pointNum_2 - к-сть точок 2 гілки
     * @return minIndex - мін значення серед максимумів (відстанб Ферше)
     */
    public Double OneToMany(double[][] branch1, double[][] branch2, int pointNum_2)
    {
        ArrayList<Double> maxValuesList = new ArrayList<Double>();// список максимальних значень для пари гілок

        BranchDivision branchDivision = new BranchDivision();
        double[][] dividedBranch = branchDivision.branchDiv(branch1, pointNum_2-1 ); // новий масив точок для гілки branch1
        for(int i=1; i<dividedBranch.length; i++)
        {
            maxValuesList.add(OneToOne(dividedBranch, branch2, i));
        }
        double minIndex = Collections.min(maxValuesList); // відстань Ферше

        return minIndex;
    }

    /**
     * @param branch1 - гілка 1 скелетону
     * @param branch2 - гілка 2 скелетону
     * @param pointNum_1 - к-сть точок 1 гілки
     * @return minIndex - мін значення серед максимумів (відстанб Ферше)
     */
    public Double ManyToOne(double[][] branch1, double[][] branch2, int pointNum_1)
    {
        ArrayList<Double> maxValuesList = new ArrayList<Double>();// список максимальних значень для пари гілок

        BranchDivision branchDivision = new BranchDivision();
        double[][] dividedBranch = branchDivision.branchDiv(branch2, pointNum_1-1 ); // новий масив точок для гілки branch1
        for(int i=1; i<dividedBranch.length; i++)
        {
            maxValuesList.add(OneToOne( branch1, dividedBranch, i));
        }
        double minIndex = Collections.min(maxValuesList); // відстань Ферше

        return minIndex;
    }

    /**
     * @param branch1 - гілка 1 скелетону
     * @param branch2 - гілка 2 скелетону
     * @param pointNum_1 - к-сть точок 1 гілки
     * @param pointNum_1 - к-сть точок 2 гілки
     * @return minIndex - мін значення серед максимумів (відстанб Ферше)
     * @return
     */
    public Double ManyToMany(double[][] branch1, double[][] branch2, int pointNum_1, int pointNum_2)
    {
        ArrayList<Double> maxValuesList = new ArrayList<Double>();// список максимальних значень для пари гілок
        int minValue = Math.min(pointNum_1, pointNum_2);

        for(int i=1; i<minValue; i++)
        {
            maxValuesList.add(OneToOne( branch1, branch2, i));
        }
        double minIndex = Collections.min(maxValuesList); // відстань Ферше

        return minIndex;
    }
}
