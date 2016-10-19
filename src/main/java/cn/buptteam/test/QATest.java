package cn.buptteam.test;

import cn.buptteam.test.structure.Answer;
import cn.buptteam.test.structure.Question;
import cn.buptteam.test.tool.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yisic on 2016/10/19.
 */
public class QATest {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Classifier.Init();
        QADataBaseSearcher.Init();
        while (true) {
            ArrayList<String> output = new ArrayList<String>();
            System.out.println(">>请输入问题:");
            String questionString = scanner.nextLine();
            if (questionString.equals("exit"))
                break;

            //问句处理
            Question question = SentenceAnalyzer.AnalyzeSentence(questionString);

            //问题分类
            Classifier.Classify(question);

            //答案检索
            Answer answer = AnswerExplorer.exploreAnswer(question);

            //答案抽取
            Filter.filterAnswer(question, answer);

            //输出
            answer.printAnswer();
        }
        Classifier.Close();
        QADataBaseSearcher.Close();
    }


}
