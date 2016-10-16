package cn.buptteam.utils;

import cn.buptteam.sentence.SentenceSimilarity;
import com.google.gson.Gson;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.io.*;
import java.util.*;

/**
 * Created by bitholic on 16/8/10.
 */
public class GetAnswers {
    //private static final int MAX_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            ArrayList<String> output = new ArrayList<String>();
            System.out.println(">>请输入问题:");
            String input = br.readLine();
            System.out.println(getAnswersByKeyword(input));
            //HashMap<String,Double> answers = (HashMap)getAnswersByKeyword(input);
           // System.out.println(new Gson().toJson(answers));
        }
    }

    public static Map<String, Double> getAnswersByKeyword(String input) {
        HashMap<String, String> QAsMap = getQAs();
        Map<String, Double> output = new HashMap<String, Double>();
        for (Map.Entry<String, String> QA : QAsMap.entrySet()) {
            double score = new SentenceSimilarity(input, QA.getKey()).getSentenceSimilarity();
            if(score >= 0.375){
                output.put(QA.getKey(), score);
            }
            //List<String> compList = TextRankKeyword.getKeywordList(QA.getKey(), MAX_NUMBER);
            /*int counter = compareList(keywordList, compList);
            if (counter >= 2) {
                String answer = QA.getValue();
                Double score = counter * 1.0 / compList.size();
                if (output.containsKey(answer)) {
                    if (output.get(answer) < score) {
                        output.put(answer, score);
                    }
                } else {

                }
            }*/
        }
       List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(output.entrySet());
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>() {
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        if ((o2.getValue() - o1.getValue()) > 0)
                            return 1;
                        else if ((o2.getValue() - o1.getValue()) == 0)
                            return 0;
                        else
                            return -1;
                    }
                }
        );
        return output;
    }

    public static HashMap<String, String> getQAs() {
        HashMap<String, String> QAsMap = new HashMap<String, String>();
        String allContent = "";
        try {
            File file = new File(GetAnswers.class.getResource("/QAs.txt").getFile());
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bw = new BufferedReader(isr);
            String tmp = "";
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
                QAsMap.put(QA[0], QA[1]);
            }
        }
        return QAsMap;
    }

    private static Integer compareList(List<String> list1, List<String> list2) {
        Integer count = 0;
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
