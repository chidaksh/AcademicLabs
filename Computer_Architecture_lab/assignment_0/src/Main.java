import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        int length = 1000;
        File file = new File(args[0]);
        File myobj = new File("output.txt");
        if (myobj.exists()){
            myobj.delete();
        }
        File fp = new File("output.txt");

        Scanner sc = new Scanner(file);
        int count=500;
        while (0<count-- & sc.hasNextLine()) {
            String str = sc.nextLine();
            String[] after_split = str.split(" ",2);

            float p = Float.parseFloat(after_split[0]);
            int width = new BigDecimal(after_split[1]).intValue();
            
            ArrayList<Integer> time = new ArrayList<Integer>();
            for (int i = 0; i < 5; i++) {
                infiltrator warrior = new infiltrator(length, width, p);
                warrior.initializer();
                clock c = new clock();
                while (!warrior.goal()) {
                    warrior.movement();
                    c.calculate_time();
                    warrior.initializer();
                }
                // System.out.println("Successfully ran after " + c.time + " seconds");
                time.add(c.time);
            }
            int average_time = 0;
            for (int i : time) {
                average_time += i;
            }
            average_time /= 5;

            try{
                String var = "" + average_time + "";
                BufferedWriter out = new BufferedWriter(new FileWriter(fp,true));
                out.write(var + "\n");
                out.close();
                // FileWriter fr = new FileWriter("output.txt");
                // fr.write(average_time);
                // fr.close();
            } catch (IOException e){
                System.out.println("An error Ocuured! " + e);
            }

            // System.out.println("Successfully ran: average time= " + average_time + " seconds");
            // System.out.println(time);
        }
        sc.close();
    }
}
