package Genetic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by amir on 4/11/17.
 */
public class Genetic  {
    Fitness fitnessFunction;
    ArrayList<Chromosome> generation;
    Integer currentGeneration;
    private Integer bestScore = -1;
    public Genetic(String encryptedTextPath,String fitnessWeightsPath,String alphabet,
                   Integer generationSize,Integer crossoverProbability,Integer mutationProbability){
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        fitnessFunction = new Fitness(encryptedTextPath,fitnessWeightsPath);
        Chromosome.alphabet = alphabet;
        currentGeneration = 0;
        generation = initializeGeneration(generationSize);
    }
    public Genetic(String encryptedTextPath,String fitnessWeightsPath,String alphabet){
        fitnessFunction = new Fitness(encryptedTextPath,fitnessWeightsPath);
        Chromosome.alphabet = alphabet;
    }
    public void testKeyString(String key){
        Chromosome c = new Chromosome(key);
        System.out.println(c.getPermutation()+": ");
        System.out.println("SCORE : " + fitnessFunction.evaluateChromosome(c));
        System.out.println("OUT: ");
        System.out.println(fitnessFunction.decrypt(c));

    }
    public void printGeneration(){
        for (Chromosome c:generation)
            System.out.println(c.getPermutation() +" : "+c.getScore());
    }
    public String decrypt(Integer index){
        Chromosome c = generation.get(index);
        return fitnessFunction.decrypt(c);
    }

    public String decrypt(){
//        Chromosome c = getBestChromosome();
//        if(c!=null)
//            return fitnessFunction.decrypt(c);
        return null;
    }
    public Integer getScore(){
        return  bestScore;
    }
    public void goToNextGeneration() {
        ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();
        Integer maxScore = -1;
        for (Chromosome c: generation) {
            Integer s = fitnessFunction.evaluateChromosome(c);
            if(maxScore <= s)
                maxScore = s;
        }
        bestScore = maxScore;
        for (int i = 0; i < generation.size()/2 ; i++) {
            Chromosome c1 = getNextChromosome();
            Chromosome c2 = getNextChromosome();
            Pair<Chromosome,Chromosome> newChromosomes = generateNewChromosomes(c1,c2);
            newGeneration.add(newChromosomes.getKey());
            newGeneration.add(newChromosomes.getValue());
        }
        generation = newGeneration;
        currentGeneration++;
    }
    private Chromosome generateChromosome(){
        ArrayList<Character> stringCharacters = new ArrayList<Character>();
        for (Character c:Chromosome.alphabet.toCharArray())
            stringCharacters.add(c);
        Collections.shuffle(stringCharacters);
        String alphabetPermutation = "";
        for (Character c:stringCharacters)
            alphabetPermutation += c;
        return new Chromosome(alphabetPermutation);
    }
    private ArrayList<Chromosome> initializeGeneration(Integer generationSize){
        ArrayList<Chromosome> firstGeneration = new ArrayList<Chromosome>();
        for (int i = 0; i < generationSize; i++)
            firstGeneration.add(generateChromosome());
        return firstGeneration;
    }
    private Integer mutationProbability;
    private Integer crossoverProbability;
    private Chromosome mutate(Chromosome c) {
        Random r = new Random();
        Integer index1 = r.nextInt(c.getSize());
        Integer index2 = r.nextInt(c.getSize());
        c.mutate(index1,index2);
        return c;
    }
    private String crossOverString(String c1, String c2, Integer randomIndex){
        String c3Part2 = "";
        String c1Part2 = c1.substring(randomIndex);
        for (int i = 0; i < c2.length(); i++) {
            if(c1Part2.indexOf(c2.charAt(i))!=-1)
                c3Part2 += c2.charAt(i);
        }
        String c3 = c1.substring(0,randomIndex) + c3Part2;
        return c3;
    }
    private Pair<Chromosome,Chromosome> crossOver(Chromosome c1, Chromosome c2){
        Random r = new Random();
        Integer randomIndex = r.nextInt(Chromosome.getSize());
        Chromosome c3 = new Chromosome(crossOverString(c1.getPermutation(),c2.getPermutation(),randomIndex));
        Chromosome c4 = new Chromosome(crossOverString(c2.getPermutation(),c1.getPermutation(),randomIndex));
        return  new Pair<Chromosome,Chromosome>(c3,c4);
    }
    private Chromosome getNextChromosome(){
        ArrayList<Integer> probabilities = new ArrayList<Integer>();
        Integer sumOfFitnessValues = 0;

        for (Chromosome c: generation) {
            probabilities.add(c.getScore());
            sumOfFitnessValues += c.getScore();
        }
        Integer min = Collections.min(probabilities)-1;

        Random r = new Random();
        Integer randomNumber = r.nextInt(sumOfFitnessValues-generation.size()*min+1);

        sumOfFitnessValues = 0;
        for(Chromosome c: generation){
            sumOfFitnessValues += c.getScore()-min;
            if(randomNumber <= sumOfFitnessValues)
                return c;
        }
        return null;
    }
    private static boolean evaluateProbability(Integer probability){
        Random r = new Random();
        Integer randomNumber = r.nextInt(100);
        return randomNumber < probability;
    }
    private boolean doMutate(){
        return  evaluateProbability(mutationProbability);
    }
    private boolean doCrossOver()
    {
        return evaluateProbability(crossoverProbability);
    }
    private Pair<Chromosome,Chromosome> generateNewChromosomes(Chromosome c1, Chromosome c2){
        Pair<Chromosome,Chromosome> newChromosomes = new Pair<Chromosome,Chromosome>(c1,c2);
        Chromosome newC1 = newChromosomes.getKey();
        Chromosome newC2 = newChromosomes.getValue();
        if(doCrossOver())
            newChromosomes = crossOver(c1,c2);
        if(doMutate())
            newC1= mutate(newC1);
        if(doMutate())
            newC2 = mutate(newC2);
        return newChromosomes;
    }
}
