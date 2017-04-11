import Genetic.Genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by amir on 4/11/17.
 */
public class Test {
    public static void main(String[] args) {
        String encryptedFilePath = "EncryptedText";
        String fitnessWeightsPath = "fitnessWeights";
        Genetic G = new Genetic(encryptedFilePath,fitnessWeightsPath,"ABCDEFGHIJKLMNOPQRSTUVWXYZ",20,80,1);
//        G.testKeyString("CPRBGVNZDUEQAMWYTLKJOIHFSX");
        while (true)
        {
            G.goToNextGeneration();
            System.out.println(G.getScore());
            try
            {
                PrintWriter pw = new PrintWriter(new FileOutputStream("Outpit"));
                pw.println(G.decrypt());
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

    }
}
