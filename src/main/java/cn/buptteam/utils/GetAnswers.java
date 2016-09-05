package cn.buptteam.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.summary.TextRankKeyword;
import com.sun.org.apache.bcel.internal.generic.FLOAD;

import java.io.*;
import java.util.*;

/**
 * Created by bitholic on 16/8/10.
 */
public class GetAnswers {
    private static final int MAX_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            ArrayList<String> output = new ArrayList<String>();
            System.out.println(">>请输入问题:");
            String input = br.readLine();
            getAnswersByKeyword(input);
        }
    }

    public static Map<String, Double> getAnswersByKeyword(String input) {
        HashMap<String, String> QAsMap = getQAs();
        List<String> keywordList = TextRankKeyword.getKeywordList(input, MAX_NUMBER);
        Map<String, Double> output = new HashMap<String, Double>();
        for (Map.Entry<String, String> QA : QAsMap.entrySet()) {
            List<String> compList = TextRankKeyword.getKeywordList(QA.getKey(), MAX_NUMBER);
            int counter = compareList(keywordList, compList);
            if (counter >= 2) {
                String answer = QA.getValue();
                Double score = counter * 1.0 / compList.size();
                if (output.containsKey(answer)) {
                    if (output.get(answer) < score) {
                        output.put(answer, score);
                    }
                } else {
                    output.put(answer, score);
                }
            }
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
        System.out.println(list_Data);
        return output;
    }

    private static HashMap<String, String> getQAs() {
        HashMap<String, String> QAsMap = new HashMap<String, String>();
        String allContent = "";
        try {
            File file = new File("src/main/resources/QAs.txt");
            BufferedReader bw = new BufferedReader(new FileReader(file));
            String tmp = null;
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
