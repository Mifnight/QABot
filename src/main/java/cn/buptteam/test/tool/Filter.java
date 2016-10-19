package cn.buptteam.test.tool;

import cn.buptteam.test.structure.Answer;
import cn.buptteam.test.structure.AnswerNode;
import cn.buptteam.test.structure.Question;

import java.util.regex.Pattern;

/**
 * Created by yisic on 2016/10/19.
 */
public class Filter {
    public static void filterAnswer(Question question, Answer answer) {
        for (AnswerNode ansNode : answer.answerNodeList) {
            if (question.questionType.equals("数字")) {
                String[] sentenceList = ansNode.answerParagraph.split("。");
                for (String str : sentenceList)
                    if (Pattern.compile(".*(\\d|一|二|三|四|五|六|七|八|九|十)+.*").matcher(str).matches())
                        ansNode.answerSentence.add(str);
            } else if (question.questionType.equals("时间")) {
                String[] sentenceList = ansNode.answerParagraph.split("。");
                for (String str : sentenceList)
                    if (Pattern.compile(".*(年|月|日|星期|小时|分钟|秒)+.*").matcher(str).matches())
                        ansNode.answerSentence.add(str);
            } else {
                ansNode.answerSentence.add(ansNode.answerParagraph);
            }
        }
    }
}
