// import java.util.Arrays;

public class border{
    
    int l;int w;
    float p;
    sensor[][] boundary;
    

    public border(int length,int width,float prob){
        this.l = length;
        this.p = prob;
        this.w = width;
        this.boundary = new sensor[w][l];
    }

    public void initializer(){
        // System.out.println(args.length);
        // System.out.println(Arrays.toString(args));
        // update the loops so that we need not update all sensors all the time
        for (int i=0;i < w;i++){
            for (int j=0;j < l;j++){
                boundary[i][j] = new sensor(this.p);
                // boundary[i][j].print(i,j);
            }
        }
    }
}