package cn.buptteam.test.structure;

import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */

public class Answer {
    public ArrayList<AnswerNode> answerNodeList;

    public Answer() {
        this.answerNodeList = new ArrayList<AnswerNode>();
    }

    public void addAnswerNode(AnswerNode answerNode) {
        this.answerNodeList.add(answerNode);
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


    public void sortAnswers() {
        qSort(answerNodeList, 0, answerNodeList.size() - 1);
    }

    public void printAnswer() {
        for (int i = 0; i < this.answerNodeList.size(); i++) {
            System.out.println("答案" + (i + 1) + ":   " + "score=" + this.answerNodeList.get(i).score);
            //System.out.println("段落:   " + answer.answerNodes.get(i).answerStr);
            //System.out.println("过滤后:");
            for (String sentence : this.answerNodeList.get(i).answerSentence)
                System.out.println(sentence);
        }
    }
}
