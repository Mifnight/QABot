package cn.buptteam.vector;

import com.hankcs.hanlp.summary.TextRankKeyword;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luyao-Li on 2016/9/5.
 */
public class VectorSimilarity {
    private static final int MAX_KEYWROD_NUMBER = 20;

    public static double getVectorSimilarity(String sentence1, String sentence2) {
        double similarity = 0.0;
        CalVector calVector = new CalVector();
        List<String> keywordList1 = TextRankKeyword.getKeywordList(sentence1, MAX_KEYWROD_NUMBER);
        List<String> keywordList2 = TextRankKeyword.getKeywordList(sentence2, MAX_KEYWROD_NUMBER);
        similarity = calVector.getSimilarity(new ArrayList<String>(keywordList1), new ArrayList<String>(keywordList2));
        return similarity;
    }

    public static void main(String args[]){

    }
}
