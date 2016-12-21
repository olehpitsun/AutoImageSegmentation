package comparator;

import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Цей порівнювач буде містити в собі усі інші
 *
 *
 * Created by Vit on 07.02.2016.
 *
 */
public class MainComparator {

    List<Comparator> comparators;
    Double Gr_HausdorffDistance;
    Double GR_FrechetDistance;
    Double FrechetDistance;
    public double resultDistance;

    public MainComparator(){
        this.comparators = new ArrayList<>();
    }

    public void add(Comparator comparator){
        this.comparators.add(comparator);
    }

    /* порівнює зображення всіма доступними компараторами
     * результат виводиться в консоль (поки що)
     */
    public void compare(List<MatOfPoint> image1, List<MatOfPoint> image2) {
        long time;
        for (Comparator comparator : this.comparators) {
            time = System.currentTimeMillis();

            Double Distance =  getDistance(image1, image2, comparator);
            //System.out.println("\n" + comparator.getName() + ". distance - " + Distance);
            //System.out.println("Time - " + (System.currentTimeMillis() - time) + " millis");

            if(comparator.getName() =="Gromov-Frechet"){
                this.GR_FrechetDistance = Distance;
            }
            if(comparator.getName() =="Gromov-Hausdorff"){
                this.Gr_HausdorffDistance = Distance;
            }
            if(comparator.getName() =="Frechet"){
                this.FrechetDistance = Distance;
            }
        }

        Result();
    }

    /**
     * функція для підрахунку результату за 2 метриками
     */
    private void Result(){
        Double result = 0.5 * this.FrechetDistance + 0.5 * this.Gr_HausdorffDistance;
        System.out.print( "\u001B[34m" +"Result:  " + result + "\u001B[0m");
        resultDistance = result;
    }

    public double getResultDistance(){
        return resultDistance;
    }
    /* Алгоритм порівняння двох зображень (може бути змінений на будь-який інший)
    * Кожен контур першого зображення порівнюється з кожним контуром другого зображення
    * 1. Шукаємо найменшу відстань для від контура зображення 1 до контурів зображення 2. Збергіаємо це значення
    * 2. Вибираємо найбільше значення зі всіх зеберених на кроці 1.
    */
    private double getDistance(List<MatOfPoint> contours1, List<MatOfPoint> contours2, Comparator comparator){
        double max = 0;
        for(MatOfPoint contour1 : contours1){
            double min = Double.MAX_VALUE;
            for(MatOfPoint contour2 : contours2){
                double d = comparator.getDistance(contour2, contour1);
                if(d < min){
                    min = d;
                }
            }
            if(max < min){
                max = min;
            }
        }
        return max;
    }
}