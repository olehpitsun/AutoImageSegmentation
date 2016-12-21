package comparator.frechet;

import comparator.Comparator;
import comparator.frechet.core.Distance;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * Поріювнювач що використовує метрику Хаусдорфа
 *
 * Created by Vit on 07.02.2016.
 */
public class FrechetComparator implements Comparator {

    private static final String NAME = "Frechet";

    @Override
    public double getDistance(List<MatOfPoint> image1, List<MatOfPoint> image2) {
        // Ще не реалізовано
        return 0;
    }

    @Override
    public double getDistance(MatOfPoint contour1, MatOfPoint contour2) {
        return Distance.getFrechetDistance(contour1, contour2);
    }

    @Override
    public String getName() {
        return FrechetComparator.NAME;
    }
}
