package comparator.atallah;

import comparator.Comparator;
import comparator.atallah.core.Distance;
import comparator.utils.GeometryUtils;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * Повертаэ відстань в метриці Хаусдорфа
 * базується на алгоритмі Аталаха
 *
 * працює тільки для опуклих фігур
 *
 * Created by Vit on 08.02.2016.
 *
 */
public class AtallahComparator implements Comparator {

    public static final String NAME = "Attalah";

    @Override
    public double getDistance(List<MatOfPoint> image1, List<MatOfPoint> image2) {
        return 0;
    }

    @Override
    public double getDistance(MatOfPoint contour1, MatOfPoint contour2) {
        if(!GeometryUtils.isPolyIntersect(contour1.toList(), contour2.toList())){
            return Distance.getHausdorffDistanceModDisjoint(contour1, contour2);
        }
        return comparator.hausdorff.core.Distance.getHausdorffDistance(contour1,contour2);
    }

    @Override
    public String getName() {
        return NAME;
    }
}