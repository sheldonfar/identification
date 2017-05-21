import java.util.ArrayList;

public class Main {

    private static int[][] calcSk(RGBImage image1, RGBImage image2) {
        int sk[][] = new int[2][image1.getFeaturesNum()];
        int[][][] binaries = {image1.getBinary(), image2.getBinary()};
        int vector[] = image1.getVector();

        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < binaries[i].length; k++) {
                for (int z = 0; z < vector.length; z++) {
                    sk[i][z] += Math.abs(vector[z] - binaries[i][k][z]);
                }
            }
        }
        return sk;
    }

    private static int calcD(int featuresNum, int[][] sk, int delta) {
        double[] k1 = new double[featuresNum];
        double[] k2 = new double[featuresNum];
        double[] k3 = new double[featuresNum];
        double[] k4 = new double[featuresNum];

        double[] D1 = new double[featuresNum];
        double[] a = new double[featuresNum];
        double[] b = new double[featuresNum];
        double[] D2 = new double[featuresNum];
        double[] J = new double[featuresNum];

        for (int i = 0; i < featuresNum; i++) {
            for (int j = 0; j < featuresNum; j++) {
                if (sk[0][j] <= i) {
                    k1[i] += 1;
                } else {
                    k3[i] += 1;
                }
                if (sk[1][j] <= i) {
                    k2[i] += 1;
                } else {
                    k4[i] += 1;
                }
            }
            D1[i] = k1[i] / 100;
            a[i] = k3[i] / 100;
            b[i] = k2[i] / 100;
            D2[i] = k4[i] / 100;
        }

        for (int i = 0; i < featuresNum; i++) {
            if ((D1[i] >= 0.2) && (D2[i] >= 0.2)) {
                double a_ = a[i];
                double b_ = b[i];
                J[i] = (Math.log((2 - (a_ + b_)) / (a_ + b_)) / Math.log(2)) * (1 - (a_ + b_)); // критерій Кульбака
            } else {
                J[i] = 0;
            }
        }

        int d = 0;
        double max_J = 0;
        for (int i = 0; i < featuresNum; i++) {
            if (J[i] > max_J) {
                max_J = J[i];
                d = i;
            }
        }

        System.out.println(max_J + "\t" + delta);

        return d;
    }

    public static void main(String[] args) {
        RGBImage[] imgs = new RGBImage[3];
        int d[] = new int[imgs.length];
        int optDelta[] = new int[imgs.length];
        int dist[] = new int[imgs.length];
        double m[] = new double[imgs.length];
        int featuresNum = 0;

        for (int deltaL = 25, deltaU = 25; deltaL >= 0; deltaL--, deltaU++) {
            imgs[0] = new RGBImage("img/field.jpg", deltaL, deltaU);
            imgs[2] = new RGBImage("img/road.jpg", deltaL, deltaU);
            imgs[1] = new RGBImage("img/forest.jpg", deltaL, deltaU);

            featuresNum = imgs[0].getFeaturesNum();

            ArrayList<Integer> minIndices = new ArrayList<Integer>();

            for (int i = 0; i < imgs.length; i++) {
                minIndices.add(0);
                int min = Integer.MAX_VALUE;
                for (int j = 0; j < imgs.length; j++) {
                    int distance = imgs[i].distanceTo(imgs[j].getVector());
                    if (distance < min && distance != 0) {
                        min = distance;
                        minIndices.set(i, j);
                    }
                }
            }

            int sk[][][] = new int[imgs.length][][];

            for (int i = 0; i < imgs.length; i++) {
                sk[i] = calcSk(imgs[i], imgs[minIndices.get(i)]);
                int newD1 = calcD(featuresNum, sk[i], deltaL);
                int newD2 = calcD(featuresNum, sk[i], deltaU);
                if (newD1 > d[i]) {
                    d[i] = newD1;
                    optDelta[i] = deltaL;
                }
                if (newD2 < d[i]) {
                    d[i] = newD2;
                    optDelta[i] = deltaU;
                }
            }
        }

        double maxM = 0;
        int maxMPos = 0;
        for (int i = 0; i < imgs.length; i++) {
            RGBImage exam = new RGBImage("img/exam.jpg", optDelta[i], optDelta[i]);
            for (int j = 0; j < featuresNum; j++) {
                dist[i] = Math.abs(exam.getVector()[j] - imgs[i].getVector()[j]);
                m[i] += 1 - dist[i] * 1.0 / d[i];
            }

            m[i] /= featuresNum;

            if (m[i] > maxM) {
                maxM = m[i];
                maxMPos = i + 1;
            }
            System.out.println("Distance to class " + (i + 1) + " = " + m[i]);
        }

        System.out.println("Image belongs to class " + maxMPos);
    }
}
