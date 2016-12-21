package comparator.skeletonEstimator;

import comparator.skeletonEstimator.Trees.Branch;

/**
 * Created by oleh on 05.06.2016.
 *
 * pointNum_1 - кількість точок першої гілки
 * pointNum_2 - кількість точок другої гілки
 */
public class Skeleton {

    private int pointNum_1;
    private int pointNum_2;
    private Double maxDistance;

    public Skeleton(double[][] branch1, double[][] branch2){

        pointNum_1 = branch1.length;
        pointNum_2 = branch2.length;

        Compare compare = new Compare();

        /** Порівняння 1 гілки до 1 гілки
         * якщо к-сть точок дорівнює 2*/
        if( pointNum_1 == 2 && pointNum_1 == pointNum_2)
        {
            maxDistance = compare.OneToOne(branch1, branch2, 1);
            System.out.println("Відстань: " + Branch.getBranchName() + " " + maxDistance);
        }

        /** Порівняння 1 гілки до N гілки
         * якщо к-сть точок дорівнює 2 та к-сть точок 1 гілки менше 2 */
        else if(pointNum_1 == 2 && pointNum_1 < pointNum_2)
        {
            maxDistance = compare.OneToMany(branch1, branch2, pointNum_2);
            System.out.println("Відстань: " + Branch.getBranchName() + " " + maxDistance);
        }

        /** Порівняння N гілок до  1 гілки
         * якщо к-сть точок 1 більше ніж у 2 та к-сть точок другої гілки = 2 */
        else if(pointNum_1 > pointNum_2 && pointNum_2 == 2)
        {
            maxDistance = compare.ManyToOne(branch1, branch2, pointNum_1);
            System.out.println("Відстань: " + Branch.getBranchName() + " " + maxDistance);
        }

        /** Порівняння N гілок до  N гілок */
        else if(pointNum_1 > 2 && pointNum_2 > 2)
        {
            maxDistance = compare.ManyToMany(branch1, branch2, pointNum_1, pointNum_2);
            System.out.println("Відстань: " + Branch.getBranchName() + " " + maxDistance);
        }

        else {System.out.println("Значення не задовільняють жодну умову");}

    }


}
