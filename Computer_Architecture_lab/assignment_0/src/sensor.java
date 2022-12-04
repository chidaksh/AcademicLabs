import java.util.Random;

public class sensor {
    // run for array of probabilities
    float p = 0.5f;
    boolean sensor_on;

    Random rand = new Random();

    public sensor(float prob){
        this.p=prob;
        float temp = rand.nextFloat();

        if (temp < this.p){
            this.sensor_on = true;
        }
        else{
            this.sensor_on = false;
        }
    }

    // public void print(int i,int j){
    //     System.out.println("Cell (" + i + "," + j + ") has a probability " + p + " to be on and the sensor_on boolean instance field is set to " + sensor_on);
    // }

}
