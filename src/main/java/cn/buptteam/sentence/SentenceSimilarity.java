package cn.buptteam.sentence;


import cn.buptteam.vector.VectorSimilarity;
import dependency.DepenSimilarity;

/**
 * Created by Luyao-Li on 2016/10/16.
 */
public class SentenceSimilarity {
    private double vectorSimilarity;
    private double depenSimilarity;

    public SentenceSimilarity(String sentence1, String sentence2){
        vectorSimilarity = VectorSimilarity.getVectorSimilarity(sentence1, sentence2);
        depenSimilarity = new DepenSimilarity(sentence1, sentence2).getDepenSimilarity();
    }

    public double getSentenceSimilarity(){
        double sentenceSimilarity;
        if(vectorSimilarity < 0.1 || depenSimilarity < 0.1)
            sentenceSimilarity = 0.0;
        else
            sentenceSimilarity = vectorSimilarity*0.45+depenSimilarity+0.55;
        return sentenceSimilarity;
    }

    public static void  main(String[] args){
        System.out.println(new SentenceSimilarity("机动车应该如何超车", "不得超车的情况").getSentenceSimilarity());
    }

}
