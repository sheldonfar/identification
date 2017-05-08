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

//        int delta = 15;
//
//        BinaryImage[] binImgs = new BinaryImage[imgs.length];
//        for (int i = 0; i < binImgs.length; i++) {
//            binImgs[i] = new BinaryImage(imgs[i], imgs[0].average(), delta);
//            binImgs[i].printDataset();
//        }
//
//        int[][] etalons = new int[binImgs.length][binImgs[0].getData().length];
//        for (int i = 0; i < etalons.length; i++) {
//            etalons[i] = binImgs[i].avg;
//            imgs[i].printDataset();
//            System.out.println("-----------------------------------------");
//            binImgs[i].printDataset();
//            //   for (int j =0; j < binImgs[i].avg.length; j++) {
//            //   System.out.print(binImgs[i].avg[j]);
//
//        }
//        System.out.println("-----------------------------------------");
//        //  }
//        // write your code here
    }
}
