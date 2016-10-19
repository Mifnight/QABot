package cn.buptteam.test.tool;

import cn.buptteam.test.structure.AnswerNode;
import cn.buptteam.test.structure.Answer;
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


public class AnswerExplorer {
    public static Answer exploreAnswer(Question question) throws Exception {
        Answer answer = new Answer();
        QADataBaseSearcher.SearchAnswerFromQA(question, answer);
        WebSearcher.getAnswerFromWeb(question, answer);
        answer.sortAnswers();
        return answer;
    }
}
