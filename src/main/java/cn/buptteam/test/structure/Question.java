package cn.buptteam.test.structure;

import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */
public class Question {
    public String questionSentence;
    public String questionType;
    public ArrayList<KeyWord> keyWords;

    public Question(String questionSentence) {
        this.questionSentence = questionSentence;
        keyWords = new ArrayList<KeyWord>();
    }

}
