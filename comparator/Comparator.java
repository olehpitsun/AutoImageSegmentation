package comparator;

import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * Цей інтерфейс має бути реалізований всіма компараторами
 *
 * Created by Vit on 07.02.2016.
 */
public interface Comparator {

    // повертає відстань для цілого зображення
    // поки можете не реалізовувати цей метод, можливо він і не пригодиться
    double getDistance(List<MatOfPoint> image1, List<MatOfPoint> image2);

    //повертає відстань тільки між двома фігурами
    double getDistance(MatOfPoint contour1, MatOfPoint contour2);

    //повертає назву (метрику/алгоритм) порівняння
    String getName();
}
