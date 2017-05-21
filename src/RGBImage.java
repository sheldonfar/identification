import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RGBImage extends Image {

    private int data[][];
    private int average[];
    private int featuresNum;
    private int samplesNum;
    private int deltaL;
    private int deltaU;
    private int binary[][];
    private int vector[];

    RGBImage(String path, int deltaL, int deltaU) {
        this.deltaL = deltaL;
        this.deltaU = deltaU;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        featuresNum = img.getWidth();
        samplesNum = img.getHeight();

        data = new int[featuresNum][samplesNum];

        for (int i = 0; i < samplesNum; i++) {
            for (int j = 0; j < samplesNum; j++) {
                Color color = new Color(img.getRGB(i, j));
                data[i][j] = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            }
        }

        average = getAverage();
        produceBinaryMatrix();
    }

    int getFeaturesNum() {
        return featuresNum;
    }

    public int[] getAverage() {
        int[] avg = new int[data[0].length];

        int sum = 0;

        for (int i = 0; i < featuresNum; i++) {
            for (int j = 0; j < samplesNum; j++) {
                sum += data[i][j];
            }
            avg[i] = Math.round(sum / data[i].length);
            sum = 0;
        }

        return avg;
    }

    private void produceBinaryMatrix() {
        int sum = 0;
        binary = new int[featuresNum][samplesNum];
        vector = new int[featuresNum];

        for (int i = 0; i < featuresNum; i++) {
            for (int j = 0; j < samplesNum; j++) {
                binary[i][j] = (data[i][j] > average[i] - deltaL && data[i][j] < average[i] + deltaU) ? 1 : 0;
                sum += binary[i][j];
            }
            vector[i] = (sum > featuresNum / 2) ? 1 : 0;
            sum = 0;
        }
    }

    int[] getVector() {
        return vector;
    }

    int[][] getBinary() {
        return binary;
    }


    int distanceTo(int[] otherVector) {
        if (vector.length != otherVector.length) {
            throw new Error("Vector dimensions must agree");
        }
        int distance = 0;
        for (int i = 0; i < otherVector.length; i++) {
            distance += Math.abs(vector[i] - otherVector[i]);
        }

        return distance;
    }

    public int[][] getData() {
        return data;
    }

    public void setData(int[][] data) {
        this.data = data;
    }
}
