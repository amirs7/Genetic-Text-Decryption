package Genetic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amir on 4/11/17.
 */
public class Chromosome {
    static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String permutation;
    private Integer score;
    String getPermutation() {
        return permutation;
    }
    Integer getScore() {
        return score;
    }
    static Integer getSize(){
        return alphabet.length();
    }
    Chromosome(String permutation) {
        score = 0;
        if(permutation.length() == getSize())
            this.permutation = permutation;
        else
            throw new Error();
    }
    Map<Character,Character> getMap(){
        Map<Character,Character> map = new HashMap<Character, Character>();
        for (int i = 0; i < permutation.length(); i++)
            map.put(new Character(permutation.charAt(i)),new Character(alphabet.charAt(i)));
        return map;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    void mutate(Integer index1, Integer index2){
        StringBuilder stringBuilder = new StringBuilder(permutation);
        stringBuilder.setCharAt(index1,permutation.charAt(index2));
        stringBuilder.setCharAt(index2,permutation.charAt(index1));
        permutation = stringBuilder.toString();
    }
}
