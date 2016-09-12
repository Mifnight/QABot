package cn.buptteam.utils;


import classifier.BayesClassifier;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dependency.CRFDependencyParser;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by yisic on 2016/9/5.
 */
class Question {
    private static final int MAX_NUMBER = 10;
    private String questionString;
    private List<String> keyWord;
    private String type;

    private void analyzeType() throws Exception {
        CoNLLSentence dependency = HanLP.parseDependency(questionString);
        ArrayList<String> feature = new ArrayList<String>();
        boolean[] isFeature = new boolean[dependency.getWordArray().length];
        for (int i = 0; i < dependency.getWordArray().length; i++) {
            CoNLLWord temp = dependency.getWordArray()[i];
            if (temp.POSTAG.contains("ry")) {
                isFeature[i] = true;
                isFeature[temp.HEAD.ID - 1] = true;
            }
            if (temp.DEPREL.equals("核心关系") || temp.DEPREL.equals("主谓关系") || temp.DEPREL.equals("动宾关系"))
                isFeature[i] = true;
        }
        for (int i = 0; i < isFeature.length; i++)
            if (isFeature[i])
                feature.add(dependency.getWordArray()[i].LEMMA);
        BayesClassifier bc = new BayesClassifier();
        bc.loadJudgeData();
        for (String word : feature) {
            System.out.print(word + "   ");
        }
        System.out.println();
        System.out.println(bc.getQuestionClass(feature));
    }

    public Question(String questionString) throws Exception {
        this.questionString = questionString;
        this.keyWord = TextRankKeyword.getKeywordList(this.questionString, MAX_NUMBER);
        this.analyzeType();

    }
}

public class QASystem {
    private static void keywordProcess(Question question) {

    }

    private static void questionClassify(Question question) {

    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        //new Question("酒后驾车罚多少钱");


        while (true) {
            ArrayList<String> output = new ArrayList<String>();
            System.out.println(">>请输入问题:");
            String questionString = scanner.nextLine();
            if (questionString.equals("exit"))
                break;

            //问题分析
            Question question = new Question(questionString);
            keywordProcess(question);
            questionClassify(question);

            //答案检索
            //TODO


            //答案抽取
            //TODO
        }
    }
}
