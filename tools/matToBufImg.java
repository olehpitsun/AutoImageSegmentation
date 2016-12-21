package tools;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by oleh on 22.07.2016.
 */
public class matToBufImg {
    Mat matrix;
    MatOfByte mob;
    String fileExten;

    // The file extension string should be ".jpg", ".png", etc
    public matToBufImg(Mat amatrix, String fileExtension){
        matrix = amatrix;
        fileExten = fileExtension;
        mob = new MatOfByte();
    }

    public BufferedImage getImage(){
        //convert the matrix into a matrix of bytes appropriate for
        //this file extension
        Highgui.imencode(fileExten, matrix ,mob);
        //convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }
}
