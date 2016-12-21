package comparator;

import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * ��� ��������� �� ���� ����������� ���� �������������
 *
 * Created by Vit on 07.02.2016.
 */
public interface Comparator {

    // ������� ������� ��� ������ ����������
    // ���� ������ �� ������������ ��� �����, ������� �� � �� �����������
    double getDistance(List<MatOfPoint> image1, List<MatOfPoint> image2);

    //������� ������� ����� �� ����� ��������
    double getDistance(MatOfPoint contour1, MatOfPoint contour2);

    //������� ����� (�������/��������) ���������
    String getName();
}
