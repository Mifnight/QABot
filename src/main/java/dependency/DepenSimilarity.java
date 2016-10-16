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
                pairWeightSum += 0.45* Similarity.getMaxSimilarity(entry1.getKey(), entry2.getKey())+0.55*coreSimilarity;
            }
        }
        if(pairWeightSum >= maxMatchPair)
            return 1.0;
        return pairWeightSum / maxMatchPair;
    }

    public static void  main(String[] args){
        System.out.println(new DepenSimilarity("行车中遇抢救伤员的救护车从本车道逆向驶来时，应","行车中遇抢救伤员的救护车从本车道逆向驶来时，应").getDepenSimilarity());
    }
}