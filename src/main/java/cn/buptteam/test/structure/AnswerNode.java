package cn.buptteam.test.structure;

import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */
public class AnswerNode {
    public String answerParagraph;
    public ArrayList<String> answerSentence;
    public double score;

    public AnswerNode(String answerParagraph, double score) {
        this.answerParagraph = answerParagraph;
        this.score = score;
        this.answerSentence = new ArrayList<String>();
    }

}
