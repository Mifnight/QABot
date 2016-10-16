package dependency;

import wordSimilarity.Similarity;

import java.util.Map;

/**
 * Created by Luyao-Li on 2016/10/16.
 */
public class DepenSimilarity {
    private double pairWeightSum = 0.0;
    private int maxMatchPair;
    private Dependency dependency1;
    private Dependency dependency2;

    public DepenSimilarity(String sentence1, String sentence2){
        dependency1 = new Dependency(sentence1);
        dependency2 = new Dependency(sentence2);
        maxMatchPair = Math.max(dependency1.getValidMatchPair(), dependency2.getValidMatchPair());
    }

    public double getDepenSimilarity(){
        double coreSimilarity = Similarity.getMaxSimilarity(dependency1.getCoreName(), dependency2.getCoreName());
        for(Map.Entry<String, String> entry1: dependency1.getPairMap().entrySet()){
            for(Map.Entry<String, String> entry2: dependency2.getPairMap().entrySet()){
                pairWeightSum += 0.35* Similarity.getMaxSimilarity(entry1.getKey(), entry2.getKey())+0.65*coreSimilarity;
            }
        }
        return pairWeightSum / maxMatchPair;
    }

    public static void  main(String[] args){
        System.out.println(new DepenSimilarity("高速公路正确超车方式","车辆在高速公路行驶时，可以仅凭感觉确认车速是否正确？").getDepenSimilarity());
    }
}