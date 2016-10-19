package cn.buptteam.test.tool;

import cn.buptteam.test.structure.Answer;
import cn.buptteam.test.structure.AnswerNode;
import cn.buptteam.test.structure.KeyWord;
import cn.buptteam.test.structure.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */
class QATuple {
    String q;
    String a;

    QATuple(String q, String a) {
        this.q = q;
        this.a = a;
    }
}

public class QADataBaseSearcher {
    public static ArrayList<QATuple> qaData;

    public static void Init() throws Exception {
        qaData = new ArrayList<QATuple>();
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
                qaData.add(new QATuple(qString, aString));
        }
    }

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

    public static void SearchAnswerFromQA(Question question, Answer answer) {
        ArrayList<AnswerNode> tempAnswerSet = new ArrayList<AnswerNode>();
        int[] questionVector = new int[question.keyWords.size()];
        for (int i = 0; i < questionVector.length; i++)
            questionVector[i] = getWordNumFromParagraph(question.keyWords.get(i).getProtoWord(), question.questionSentence);
        for (QATuple qa : qaData) {
            int[] dataVector = new int[question.keyWords.size()];
            for (int i = 0; i < dataVector.length; i++)
                dataVector[i] = getWordSetNumFromParagraph(question.keyWords.get(i), qa.q);
            tempAnswerSet.add(new AnswerNode(qa.a, getCos(questionVector, dataVector)));
        }
        sortAnswer(tempAnswerSet);
        for (int i = 0; i < 5; i++)
            if (tempAnswerSet.get(i).score != 0)
                answer.addAnswerNode(tempAnswerSet.get(i));
    }

    public static void Close() {
        qaData = null;
    }
}
