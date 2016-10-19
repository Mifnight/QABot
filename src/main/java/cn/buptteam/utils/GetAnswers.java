package cn.buptteam.utils;

import cn.buptteam.test.structure.Answer;
import cn.buptteam.test.structure.AnswerNode;
import cn.buptteam.test.structure.Question;
import cn.buptteam.test.tool.*;
import com.google.gson.Gson;
import com.hankcs.hanlp.summary.TextRankKeyword;

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
            //System.out.println(getAnswersByKeyword("在路口右转弯遇同车道前车等候放行信号时如何行驶?"));
            HashMap<String,Double> answers = (HashMap)getAnswersByKeyword(input);
            System.out.println(new Gson().toJson(answers));
        }
    }

    public static Map<String, Double> getAnswersByKeyword(String input) throws Exception{
        HashMap<String, String> QAsMap = getQAs();
        List<String> keywordList = TextRankKeyword.getKeywordList(input, MAX_NUMBER);
        Map<String, Double> output = new HashMap<String, Double>();
        Classifier.Init();
        QADataBaseSearcher.Init();
        //问句处理
        Question question = SentenceAnalyzer.AnalyzeSentence(input);

        //问题分类
        Classifier.Classify(question);

        //答案检索
        Answer answer1 = AnswerExplorer.exploreAnswer(question);

        //答案抽取
        Filter.filterAnswer(question, answer1);

        for (int i = 0; i < answer1.answerNodeList.size(); i++) {
            AnswerNode tmp = answer1.answerNodeList.get(i);
            String tmpAnswer = "";
            for (int i1 = 0; i1 < tmp.answerSentence.size(); i1++) {
                tmpAnswer += tmp.answerSentence.get(i1);
            }
            output.put(tmpAnswer,tmp.score);
        }
//        for (Map.Entry<String, String> QA : QAsMap.entrySet()) {
//            List<String> compList = TextRankKeyword.getKeywordList(QA.getKey(), MAX_NUMBER);
//            int counter = compareList(keywordList, compList);
//            if (counter >= 2) {
//                String answer = QA.getValue();
//                Double score = counter * 1.0 / compList.size();
//                if (output.containsKey(answer)) {
//                    if (output.get(answer) < score) {
//                        output.put(answer, score);
//                    }
//                } else {
//                    output.put(answer, score);
//                }
//            }
//        }
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
