package cn.buptteam.calSimilarityByVector;

import com.hankcs.hanlp.summary.TextRankKeyword;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luyao-Li on 2016/9/5.
 */
public class GetSimilarityValue {
    private static final int MAX_KEYWROD_NUMBER = 20;
    private HashMap<String, String> QAsMap = getQAsMap();

    public static void main(String args[]){//
        String s = "在路口右转弯遇同车道前车等候放行信号时如何行驶？";
        GetSimilarityValue getSimilarityValue = new GetSimilarityValue();
        getSimilarityValue.getSimilarityValueByKeyword(s);
    }

    public Map<String, Double> getSimilarityValueByKeyword(String userQuestion) {
        int count=0;
        double similarity=0;
        GetSimilarityValue getSimilarityValue = new GetSimilarityValue();
        CalVector calVector = new CalVector();
        List<String> keywordList = TextRankKeyword.getKeywordList(userQuestion, MAX_KEYWROD_NUMBER);
        HashMap<String, Double> resultWithValue = new HashMap<String, Double>();
        for (Map.Entry<String, String> QA : getSimilarityValue.QAsMap.entrySet()) {
            count++;
            List<String> questionKeywordList = TextRankKeyword.getKeywordList(QA.getKey(), MAX_KEYWROD_NUMBER);
            similarity = calVector.getSimilarity(new ArrayList<String>(keywordList), new ArrayList<String>(questionKeywordList));
            resultWithValue.put(QA.getValue(), similarity);
            System.out.print(count+"  ");//
            System.out.println(questionKeywordList);//
            System.out.println(similarity);//
        }
        return resultWithValue;
    }

    public HashMap<String, String> getQAsMap() {
        HashMap<String, String> tmpQAsMap = new HashMap<String, String>();
        String allContent = "";
        String tmp = "";
        try {
            File file = new File("src/main/resources/QAs.txt");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bw = new BufferedReader(isr);
            while ((tmp = bw.readLine()) != null) {
                allContent += tmp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] QAs = allContent.split("Q:");
        for (int i = 0; i < QAs.length; i++) {
            String[] QA = QAs[i].split("A:");
            if (QA.length > 1) {
                tmpQAsMap.put(QA[0], QA[1]);
            }
        }
        return tmpQAsMap;
    }

}
