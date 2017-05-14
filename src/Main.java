public class Main {

    public static int[][] calcSk(RGBImage[] imgs, int[] minDistIndex) {
        int sk[][] = new int[2][imgs[0].getFeaturesNum()];

        for (int i = 0; i < 2; i++) {
            int binary[][] = imgs[minDistIndex[i]].getBinary();
            int vector[] = imgs[minDistIndex[0]].getVector();
            for (int k = 0; k < binary.length; k++) {
                for (int z = 0; z < vector.length; z++) {
                    sk[i][z] += Math.abs(vector[z] - binary[k][z]);
                }
            }
        }
        return sk;
    }

    public static void main(String[] args) {
        RGBImage[] imgs = new RGBImage[3];
        imgs[0] = new RGBImage("img/field.jpg");
        imgs[2] = new RGBImage("img/road.jpg");
        imgs[1] = new RGBImage("img/forest.jpg");

        int featuresNum = imgs[0].getFeaturesNum();

        int[][] distanceMatrix = new int[imgs.length][imgs.length];
        int minDist = imgs[0].distanceTo(imgs[1].getVector());
        int[] minDistIndex = {0, 0};

        for (int i = 0; i < imgs.length; i++) {
            for (int j = 0; j < imgs.length; j++) {
                distanceMatrix[i][j] = imgs[i].distanceTo(imgs[j].getVector());
                System.out.print(distanceMatrix[i][j] + "  ");
                if (distanceMatrix[i][j] < minDist && distanceMatrix[i][j] != 0) {
                    minDist = distanceMatrix[i][j];
                    minDistIndex = new int[]{i, j};
                }
            }
            System.out.println(" ");
        }

        System.out.println("Min dist: " + minDist + ", Index: " + minDistIndex[0] + "," + minDistIndex[1]);

        //sk
        int sk[][] = calcSk(imgs, minDistIndex);

        int min = distanceMatrix[minDistIndex[1]][0];
        int minIndex = 0;
        for (int i = 0; i < distanceMatrix[minDistIndex[1]].length; i++) {
            int dist = distanceMatrix[minDistIndex[1]][i];
            if (dist < min && dist != 0) {
                min = dist;
                minIndex = i;
            }
        }
        System.out.println("NEW MIN " + minIndex);

        //sk_para
        int skPara[][] = calcSk(imgs, new int[]{minDistIndex[1], minIndex});


        System.out.println("-----SK-----");
        for (int i = 0; i < sk.length; i++) {
            for (int j = 0; j < sk[0].length; j++) {
                System.out.print(sk[i][j] + " ");
            }
            System.out.println("  ");
        }

        System.out.println("-----SK_PARA-----");
        for (int i = 0; i < skPara.length; i++) {
            for (int j = 0; j < skPara[0].length; j++) {
                System.out.print(skPara[i][j] + " ");
            }
            System.out.println("  ");
        }

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
            System.out.println("D1 ----> " + k1[i] / 100);
            D1[i] = k1[i] / 100;
            a[i] = k3[i] / 100;
            b[i] = k2[i] / 100;
            D2[i] = k4[i] / 100;
        }

        for (int i = 0; i < featuresNum; i++) {
            System.out.println("D1[i] " + D1[i] + ", D2[i] " + D2[i]);
            if ((D1[i] >= 0.2) && (D2[i] >= 0.2)) {
                double a_ = a[i];
                double b_ = b[i];
                //$temp=1+0.5*(($a/($a+$D2))*(log($a/($a+$D2)))/log(2)+($D1/($D1+$b))*(log($D1/($D1+$b)))/log(2)+($b/($D1+$b))*(log($b/($D1+$b)))/log(2)+($D2/($a+$D2))*(log($D2/($a+$D2)))/log(2)); //критерій Шеннона
                J[i] = (Math.log((2 - (a_ + b_)) / (a_ + b_)) / Math.log(2)) * (1 - (a_ + b_)); // критерій Кульбака
            } else {
                J[i] = 0;
            }
        }

        double max_J = 0;
        for (int i = 0; i < featuresNum; i++) {
            System.out.println("J " + J[i]);
            if (max_J < J[i]) {
                max_J = J[i];
            }
        }

        System.out.println("MAX J " + max_J);
    }


    public static void calcAccuracy() {

    }
}
