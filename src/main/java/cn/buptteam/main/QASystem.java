package cn.buptteam.main;


import classifier.BayesClassifier;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.summary.TextRankKeyword;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spider.Spider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by yisic on 2016/9/5.
 */
class Question {
    private static final int MAX_NUMBER = 10;
    private String questionString;
    private ArrayList<String> keyWord = new ArrayList<String>();
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
        System.out.print("关键字:    ");
        for (String word : feature) {
            System.out.print(word + "   ");
        }
        System.out.println();
        this.type = bc.getQuestionClass(feature);
        System.out.println("问题类型:" + this.type);
    }

    private void keyWordExpand() {

    }

    public Question(String questionString) throws Exception {
        this.questionString = questionString;
        for (String word : TextRankKeyword.getKeywordList(this.questionString, MAX_NUMBER))
            this.keyWord.add(word);
        this.analyzeType();
        this.keyWordExpand();
    }

    public List<String> getKeyWord() {
        return keyWord;
    }

    public String getType() {
        return type;
    }

    public String getQuestionString() {
        return questionString;
    }
}

class AnswerNode {
    public String answerStr;
    public double score;

    AnswerNode(String answerStr, double score) {
        this.answerStr = answerStr;
        this.score = score;
    }
}

class QACouple {
    String q;
    String a;

    QACouple(String q, String a) {
        this.q = q;
        this.a = a;
    }
}

class Answer {
    ArrayList<AnswerNode> answerNodes = new ArrayList<AnswerNode>();
    private ArrayList<QACouple> qaData = new ArrayList<QACouple>();

    private void loadQAData() throws Exception {
        File qaFile = new File("src/main/resources/qaSet.xml");
        Element element = null;
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        Document dt = db.parse(qaFile);
        element = dt.getDocumentElement();
        NodeList childNodes = element.getElementsByTagName("QA");
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node1 = childNodes.item(i);
            String qString = node1.getChildNodes().item(1).getTextContent();
            String aString = node1.getChildNodes().item(3).getTextContent();
            if (qString != null && aString != null)
                this.qaData.add(new QACouple(qString, aString));
        }
    }

    private static int getWordNum(String word, String passage) {
        int start = 0;
        int num = 0;
        while (passage.indexOf(word, start) >= 0 && start < passage.length()) {
            num++;
            start = passage.indexOf(word, start) + word.length();
        }
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

    private void getAnswerFromQA(Question question) throws Exception {
        if (this.qaData.size() == 0)
            loadQAData();
        ArrayList<AnswerNode> tempAnswerSet = new ArrayList<AnswerNode>();
        int[] questionVector = new int[question.getKeyWord().size()];
        for (int i = 0; i < questionVector.length; i++)
            questionVector[i] = getWordNum(question.getKeyWord().get(i), question.getQuestionString());
        for (QACouple qa : qaData) {
            int[] dataVector = new int[question.getKeyWord().size()];
            for (int i = 0; i < dataVector.length; i++)
                dataVector[i] = getWordNum(question.getKeyWord().get(i), qa.q);
            tempAnswerSet.add(new AnswerNode(qa.a, getCos(questionVector, dataVector)));
        }
        sortAnswer(tempAnswerSet);
        for (int i = 0; i < 5; i++)
            if (tempAnswerSet.get(i).score != 0)
                this.answerNodes.add(tempAnswerSet.get(i));

    }

    private void getAnswerFromWeb(Question question) throws Exception {
        ArrayList<String> zhiDaoData = Spider.getAnswerFromZhiDao(question.getQuestionString());
        ArrayList<AnswerNode> tempAnswerSet = new ArrayList<AnswerNode>();
        int[] questionVector = new int[question.getKeyWord().size()];
        for (int i = 0; i < questionVector.length; i++)
            questionVector[i] = getWordNum(question.getKeyWord().get(i), question.getQuestionString());
        for (String zhiDaoStr : zhiDaoData) {
            int[] dataVector = new int[question.getKeyWord().size()];
            for (int i = 0; i < dataVector.length; i++)
                dataVector[i] = getWordNum(question.getKeyWord().get(i), zhiDaoStr);
            tempAnswerSet.add(new AnswerNode(zhiDaoStr, getCos(questionVector, dataVector)));
        }
        sortAnswer(tempAnswerSet);
        for (int i = 0; i < 5; i++)
            if (tempAnswerSet.get(i).score != 0)
                this.answerNodes.add(tempAnswerSet.get(i));

    }

    public Answer(Question question) throws Exception {
        System.out.println("正在从QA库中搜索...");
        getAnswerFromQA(question);
        System.out.println("正在从网络中搜索...");
        getAnswerFromWeb(question);
        System.out.println("正在处理答案...");
        sortAnswer(this.answerNodes);
    }
}

public class QASystem {


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
/****/
        ArrayList<String> a = new ArrayList<String>();
        String b = "酒后驾车罚多少钱";
        System.out.println(b);
        Question c = new Question(b);
        Answer d = new Answer(c);
        for (int i = 0; i < d.answerNodes.size(); i++) {
            System.out.println(i + ":   " + "score=" + d.answerNodes.get(i).score);
            System.out.println(d.answerNodes.get(i).answerStr + "\n\n");
        }

/****/
        while (true) {
            ArrayList<String> output = new ArrayList<String>();
            System.out.println(">>请输入问题:");
            String questionString = scanner.nextLine();
            if (questionString.equals("exit"))
                break;

            //问题分析
            Question question = new Question(questionString);

            //答案检索
            Answer answer = new Answer(question);

            //答案抽取
            //TODO

            //输出
            for (int i = 0; i < answer.answerNodes.size(); i++) {
                System.out.println(i + ":   " + "score=" + answer.answerNodes.get(i).score);
                System.out.println(answer.answerNodes.get(i).answerStr + "\n\n");
            }
        }
    }
}
