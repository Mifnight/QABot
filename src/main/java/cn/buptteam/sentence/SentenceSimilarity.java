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
        sentenceSimilarity = vectorSimilarity*0.55+depenSimilarity*0.45;
        return sentenceSimilarity;
    }

    public static void  main(String[] args){
        System.out.println(new SentenceSimilarity("机动车应该如何超车", "机动车超车规定").getSentenceSimilarity());
    }

}
