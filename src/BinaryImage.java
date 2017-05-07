/**
 * Created by admin on 23.02.2017.
 */
public class BinaryImage extends Image {
    protected int []avg;
    BinaryImage(RGBImage image, int[] avg, int delta){
        data = new int [image.getData().length][];
        for(int i = 0; i < image.getData().length; i++) {
            data[i] = new int [image.getData()[i].length];
            for (int j = 0; j < image.getData()[i].length; j++) {
                if((avg[j]-delta<=image.getData()[i][j])&&(image.getData()[i][j]<=avg[j]+delta))
                    data[i][j]=1;
                else
                    data[i][j]=0;
            }
        }
        this.avg = this.getAverage();
    }
    public int [] getAverage (){
        double []buff = new double[data[0].length];
        int []avg = new int[data[0].length];
        double sum=0;
        for (int j=0; j<data[0].length; j++){
            sum=0;
            for (int i=0; i<data.length; i++){////????
                sum+=data[i][j];
            }
            buff[j]=sum/((float)data.length);
             System.out.print(buff[j] +" ");
            if (buff[j]>=0.5){avg[j]=1;}
            else avg[j]=0;
        }

         System.out.println();
        return avg;
    }

}
