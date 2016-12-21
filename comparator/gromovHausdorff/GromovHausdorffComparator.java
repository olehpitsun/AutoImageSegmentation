package comparator.gromovHausdorff;

import comparator.Comparator;
import comparator.gromovHausdorff.core.Distance;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * Поріювнювач що використовує метрику Хаусдорфа
 *
 * Created by Vit on 07.02.2016.
 */
public class GromovHausdorffComparator implements Comparator {

    private static final String NAME = "Gromov-Hausdorff";

    @Override
    public double getDistance(List<MatOfPoint> image1, List<MatOfPoint> image2) {
        // Ще не реалізовано
        return 0;
    }

    @Override
    public double getDistance(MatOfPoint contour1, MatOfPoint contour2) {
        return Distance.getGromovHausdorffDistance(contour1, contour2);
    }

    @Override
    public String getName() {
        return GromovHausdorffComparator.NAME;
    }
}