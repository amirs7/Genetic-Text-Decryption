package Genetic;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amir on 4/11/17.
 */
public class Fitness {
    private Map<String,Integer> fitnessWeights;
    private String encryptedText;
    public Fitness(String encryptedTextPath,String fitnessWeightsPath) {
        encryptedText = "";
        fitnessWeights = new HashMap<String, Integer>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(fitnessWeightsPath)));
            while (br.ready())
            {
                String[] words = br.readLine().split(" ");
                String key = words[0];
                Integer value = Integer.parseInt(words[1]);
                fitnessWeights.put(key,value);
            }
            br = new BufferedReader(new InputStreamReader( new FileInputStream(encryptedTextPath)));
            while (br.ready())
                encryptedText += br.readLine()+" ";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Integer evaluateChromosome(Chromosome chromosome){
        Integer score = 0;
        String decryptedText = decrypt(chromosome);
        for(String key: fitnessWeights.keySet()) {
            int lastIndex = 0;
            int count = 0;
            while (lastIndex != -1) {

                lastIndex = decryptedText.indexOf(key, lastIndex);

                if (lastIndex != -1) {
                    count++;
                    lastIndex += key.length();
                }
            }
            score += count * fitnessWeights.get(key);
        }
        chromosome.setScore(score);
        return score;
    }
    public String decrypt(Chromosome c){
        String decryptedText = "";
        Map<Character,Character> keyMap = c.getMap();
        for (int i = 0; i < encryptedText.length(); i++) {
            Character ch  = keyMap.get(encryptedText.charAt(i));
            if(ch == null)
                ch = encryptedText.charAt(i);
            decryptedText += ch;
        }
        return decryptedText;
    }
}
