package comparator.qualityEstimator.core;

import comparator.qualityEstimator.core.Contour.Contour;
import comparator.qualityEstimator.core.Contour.Contour2;
import comparator.qualityEstimator.core.Contour.Contour3;
import comparator.utils.GeometryUtils;

/**
 * Created by Ihor on 15.11.2015.
 */
public class IsometricTranslation {
    public static double distBy360Rotation (Contour contour1, Contour contour2){
        double minDist = 999999999;
        for(int i = 0; i < 360;i++){
            double difX =  contour1.getCentroid().x - contour2.getCentroid().x;
            double difY =  contour1.getCentroid().y -  contour2.getCentroid().y;

            contour2.shift(difX,difY);
            Contour modifiedContour = new Contour(contour2);
            double dist = GeometryUtils.distHausdorff(contour1.getContour(),modifiedContour.rotate(i).getContour());
            if(minDist > dist){
                minDist = dist;
            }
            do{
                dist = GeometryUtils.distHausdorff(contour1.getContour(),modifiedContour.shift(1, 0).getContour());
                if(minDist > dist){
                    minDist = dist;
                }else {
                    modifiedContour.shift(-1, 0);
                    break;
                }
            }while (dist < minDist);
            do{
                dist = GeometryUtils.distHausdorff(contour1.getContour(),modifiedContour.shift(-1, 0).getContour());
                if(minDist > dist){
                    minDist = dist;
                }else {
                    modifiedContour.shift(1, 0);
                    break;
                }
            }while (dist < minDist);
            do{
                dist = GeometryUtils.distHausdorff(contour1.getContour(),modifiedContour.shift(0, 1).getContour());
                if(minDist > dist){
                    minDist = dist;
                }else {
                    modifiedContour.shift(0, -1);
                    break;
                }
            }while (dist < minDist);
            do{
                dist = GeometryUtils.distHausdorff(contour1.getContour(),modifiedContour.shift(0, -1).getContour());
                if(minDist > dist){
                    minDist = dist;
                }else {
                    modifiedContour.shift(0, 1);
                    break;
                }
            }while (dist < minDist);
        }
        return minDist;
    }

    public static double distBySecantMethod(Contour3 contour1, Contour3 contour2){
        double minDist = 999999999;
        double difX =  contour1.getCentroid().x - contour2.getCentroid().x;
        double difY =  contour1.getCentroid().y -  contour2.getCentroid().y;
        contour2 = contour2.shift(difX,difY);
        for (int i=0; i<contour1.getChordes().size();i++){
            for (int j=0; j<contour2.getChordes().size();j++) {
                Contour3 modifiedContour2 = new Contour3(contour2);
                double engle =  modifiedContour2.getChordes().get(j).getAngle() - contour1.getChordes().get(i).getAngle();
                double dist = GeometryUtils.distHausdorff(contour1.getContour(), modifiedContour2.rotate(engle).getContour());
                if(minDist > dist){
                    minDist = dist;
                }
            }
        }
        return minDist;
    }

    public static double distByLongestChordAndPerpendiular(Contour2 contour1, Contour2 contour2){
        double angle =  contour2.getChorde().getAngle() - contour1.getChorde().getAngle();
        double difX =  contour1.getCentroid().x - contour2.getCentroid().x;
        double difY =  contour1.getCentroid().y -  contour2.getCentroid().y;
        contour2 =  contour2.shift(difX, difY).rotate(angle);
        if(contour1.isMiddleHigher() != contour2.isMiddleHigher()){
            contour2 = contour2.rotate(180);
        }
        double dist = GeometryUtils.distHausdorff(contour1.getContour(), contour2.getContour());
        return dist;
    }

    public static double distByLongestChord(Contour contour1, Contour contour2) {
        double difX = contour1.getCentroid().x - contour2.getCentroid().x;
        double difY = contour1.getCentroid().y - contour2.getCentroid().y;
        double engle = contour2.getMaxChorge().getAngle() - contour1.getMaxChorge().getAngle();
        double dist1 = GeometryUtils.distHausdorff(contour1.getContour(), contour2.shift(difX, difY).rotate(engle).getContour());

        double dist2 = GeometryUtils.distHausdorff(contour1.getContour(), contour2.rotate(180).getContour());
        return Math.min(dist1,dist2);
    }

    public static double distByListOfChordes(Contour contour1, Contour contour2){
        double difX =  contour1.getCentroid().x - contour2.getCentroid().x;
        double difY =  contour1.getCentroid().y - contour2.getCentroid().y;
        contour2.shift(difX,difY);
        double minDist = 999999999;
        for (int i=0; i<contour1.getChordesCount();i++){
            for (int j=0; j<contour2.getChordesCount();j++){

                Contour modifiedContour2 = new Contour(contour2);
                double engle =  modifiedContour2.getChorde(j).getAngle() - contour1.getChorde(i).getAngle();
                double dist = GeometryUtils.distHausdorff(contour1.getContour(), modifiedContour2.rotate(engle).getContour() );
                if(minDist > dist){
                    minDist = dist;
                }
                dist = GeometryUtils.distHausdorff(contour1.getContour(), modifiedContour2.rotate(180).getContour() );
                if(minDist > dist){
                    minDist = dist;
                }
            }
        }
        return minDist;
    }
}