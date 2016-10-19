package cn.buptteam.test.tool;

import cn.buptteam.test.structure.Answer;
import cn.buptteam.test.structure.AnswerNode;
import cn.buptteam.test.structure.KeyWord;
import cn.buptteam.test.structure.Question;
import spider.Spider;

import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */
public class WebSearcher {
    private static int getWordNumFromParagraph(String word, String paragraph) {
        int start = 0;
        int num = 0;
        while (paragraph.indexOf(word, start) >= 0 && start < paragraph.length()) {
            num++;
            start = paragraph.indexOf(word, start) + word.length();
        }
        return num;
    }

    private static int getWordSetNumFromParagraph(KeyWord keyWord, String paragraph) {
        int num = 0;
        for (String w : keyWord.getWordSet())
            num += getWordNumFromParagraph(w, paragraph);
        return num;
    }

    private static double getCos(int[] v1, int[] v2) {
        if (v1.length != v2.length)
            return 0;
        double mul = 0;
        double dis1 = 0, dis2 = 0;
        for (int i = 0; i < v1.length; i++) {
            mul += v1[i] * v2[i];
            dis1 += v1[i] * v1[i];
            dis2 += v2[i] * v2[i];
        }
        if (dis1 == 0 || dis2 == 0)
            return 0;
        return mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
    }

    private static void qSort(ArrayList<AnswerNode> data, int start, int end) {
        if (start >= end)
            return;
        int tstart = start, tend = end;
        AnswerNode temp;
        while (tstart < tend) {
            while (tstart < tend && data.get(tend).score <= data.get(tstart).score)
                tend--;
            temp = data.get(tstart);
            data.set(tstart, data.get(tend));
            data.set(tend, temp);
            while (tstart < tend && data.get(tstart).score >= data.get(tend).score)
                tstart++;
            temp = data.get(tstart);
            data.set(tstart, data.get(tend));
            data.set(tend, temp);
        }
        qSort(data, start, tstart - 1);
        qSort(data, tstart + 1, end);
    }

    private static void sortAnswer(ArrayList<AnswerNode> data) {
        qSort(data, 0, data.size() - 1);
    }

    public static void getAnswerFromWeb(Question question, Answer answer) throws Exception {
        ArrayList<String> zhiDaoData = Spider.getAnswerFromZhiDao(question.questionSentence);
        ArrayList<AnswerNode> tempAnswerSet = new ArrayList<AnswerNode>();
        int[] questionVector = new int[question.keyWords.size()];
        for (int i = 0; i < questionVector.length; i++)
            questionVector[i] = getWordNumFromParagraph(question.keyWords.get(i).getProtoWord(), question.questionSentence);
        for (String zhiDaoStr : zhiDaoData) {
            int[] dataVector = new int[question.keyWords.size()];
            for (int i = 0; i < dataVector.length; i++)
                dataVector[i] = getWordSetNumFromParagraph(question.keyWords.get(i), zhiDaoStr);
            tempAnswerSet.add(new AnswerNode(zhiDaoStr, getCos(questionVector, dataVector)));
        }
        sortAnswer(tempAnswerSet);
        for (int i = 0; i < 5 && i < tempAnswerSet.size(); i++)
            if (tempAnswerSet.get(i).score != 0)
                answer.addAnswerNode(tempAnswerSet.get(i));

    }

}
