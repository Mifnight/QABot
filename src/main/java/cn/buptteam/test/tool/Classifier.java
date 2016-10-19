package cn.buptteam.test.tool;

import cn.buptteam.test.structure.Question;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yisic on 2016/10/19.
 */
public class Classifier {
    public static Map<String, ArrayList<String>> trainingSet = null;
    public static int trainingSetQuestionNum = -1;
    public static ArrayList<String> questionTypeList;

    public static void Init() throws Exception {
        //读取类别
        FileReader typeFileReader = new FileReader("src/main/resources/QuestionTypes.txt");
        BufferedReader typeFileBufferReader = new BufferedReader(typeFileReader);
        String typeTemp = null;
        questionTypeList = new ArrayList<String>();
        while ((typeTemp = typeFileBufferReader.readLine()) != null) {
            questionTypeList.add(typeTemp);
        }
        typeFileBufferReader.close();


        //读取训练集
        trainingSet = new HashMap<String, ArrayList<String>>();
        for (String str : questionTypeList)
            trainingSet.put(str, new ArrayList<String>());
        File dataFile = new File("src/main/resources/qSet.xml");
        Element element = null;
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            Document dt = db.parse(dataFile);
            //根
            element = dt.getDocumentElement();
            // 获得根元素下的子节点
            NodeList childNodes = element.getElementsByTagName("DOC");
            // 遍历这些子节点
            trainingSetQuestionNum = childNodes.getLength();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node1 = childNodes.item(i);
                NodeList nodeDetail = node1.getChildNodes();
                String questionString = node1.getChildNodes().item(1).getTextContent();
                String questionStyle = node1.getChildNodes().item(3).getTextContent();
                if (questionString != null && questionStyle != null) {
                    if (questionStyle.contains("\uFEFF"))
                        questionStyle = questionStyle.substring(1, questionStyle.length());
                    ArrayList<String> aaa = trainingSet.get(questionStyle);
                    aaa.add(questionString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void Close() {

    }

    private static ArrayList<String> getFeature(String sentence) {
        ArrayList<String> feature = new ArrayList<String>();
        CoNLLSentence dependency = HanLP.parseDependency(sentence);
        boolean[] isFeature = new boolean[dependency.getWordArray().length];
        //疑问词及疑问词依赖词，主谓宾为特征
        for (int i = 0; i < dependency.getWordArray().length; i++) {
            CoNLLWord temp = dependency.getWordArray()[i];
            if (temp.POSTAG.contains("ry")) {
                isFeature[i] = true;
                if (temp.HEAD.ID > 0)
                    isFeature[temp.HEAD.ID - 1] = true;
            }
            if (temp.DEPREL.equals("核心关系") || temp.DEPREL.equals("主谓关系") || temp.DEPREL.equals("动宾关系"))
                isFeature[i] = true;
        }
        for (int i = 0; i < isFeature.length; i++)
            if (isFeature[i])
                feature.add(dependency.getWordArray()[i].LEMMA);
        return feature;
    }

    private static String getFeatureClass(ArrayList<String> feature) {
        //各个类在总训练集中的比例
        double[] typeProportion = new double[questionTypeList.size()];
        for (int i = 0; i < typeProportion.length; i++)
            typeProportion[i] = (double) trainingSet.get(questionTypeList.get(i)).size() / trainingSetQuestionNum;
        //每个特征词在每个问题类中的比例
        double[][] wordProportion = new double[questionTypeList.size()][feature.size()];
        for (int i = 0; i < questionTypeList.size(); i++) {
            ArrayList<String> sourceQuestion = trainingSet.get(questionTypeList.get(i));
            for (String sStr : sourceQuestion)
                for (int j = 0; j < feature.size(); j++)//每个特征词
                    if (sStr.contains(feature.get(j)))
                        wordProportion[i][j] += 1.0 / sourceQuestion.size();
        }

        for (int i = 0; i < wordProportion.length; i++)
            for (int j = 0; j < wordProportion[i].length; j++)
                wordProportion[i][j] *= typeProportion[i];

        double[] featureTypeProportion = new double[questionTypeList.size()];
        for (int i = 0; i < featureTypeProportion.length; i++)
            featureTypeProportion[i] = 1;
        double max = -1;
        int maxIndex = -1;
        for (int i = 0; i < wordProportion.length; i++) {
            for (int j = 0; j < wordProportion[i].length; j++)
                featureTypeProportion[i] *= wordProportion[i][j];
            if (max < featureTypeProportion[i]) {
                max = featureTypeProportion[i];
                maxIndex = i;
            }
        }
        return questionTypeList.get(maxIndex);
    }

    public static void Classify(Question question) {
        ArrayList<String> feature = getFeature(question.questionSentence);
        question.questionType = getFeatureClass(feature);

        //for test
        System.out.print("分类特征:\t");
        for (String f : feature)
            System.out.print(f + "\t");
        System.out.println();
        System.out.println("问题类别:\t" + question.questionType);
    }


}
