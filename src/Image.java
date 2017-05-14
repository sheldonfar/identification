/**
 * Created by admin on 23.02.2017.
 */
public abstract class Image {
    public int[][] getData() {
        return data;
    }

    public void setData(int[][] data) {
        this.data = data;
    }

    protected int[][] data;

    public abstract int[] getAverage();

    public void printDataset() {

        for (int i = 0; i < data.length; i += 5) {
            for (int j = 0; j < data[i].length; j += 5) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }
}



