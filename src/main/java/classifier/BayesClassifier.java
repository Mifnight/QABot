package classifier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yisic on 2016/9/7.
 */
public class BayesClassifier {
    /*
    * 推荐类
    * 表示类
    * 比较类
    * 描述类
    * 关系类
    * 方法类
    * 事实类
    * 枚举类
    * 不作处理
    * 需求类
    * 评价类
    * 是非类
    * 原因类
    * */
    private Map<String, ArrayList<String>> judgeData;
    private int judgeDataNum;
    private ArrayList<String> questionTypeList;

    public void loadJudgeData() throws Exception {
        questionTypeList = new ArrayList<String>();
        questionTypeList.add("事实类");
        questionTypeList.add("表示类");
        questionTypeList.add("比较类");
        questionTypeList.add("描述类");
        questionTypeList.add("关系类");
        questionTypeList.add("方法类");
        questionTypeList.add("推荐类");
        questionTypeList.add("枚举类");
        questionTypeList.add("需求类");
        questionTypeList.add("评价类");
        questionTypeList.add("是非类");
        questionTypeList.add("原因类");
        questionTypeList.add("不作处理");
        judgeData = new HashMap<String, ArrayList<String>>();
        for (String str : questionTypeList)
            judgeData.put(str, new ArrayList<String>());
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
            this.judgeDataNum = childNodes.getLength();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node1 = childNodes.item(i);
                NodeList nodeDetail = node1.getChildNodes();
                String questionString = node1.getChildNodes().item(1).getTextContent();
                String questionStyle = node1.getChildNodes().item(3).getTextContent();
                if (questionString != null && questionStyle != null) {
                    if (questionStyle.contains("\uFEFF"))
                        questionStyle = questionStyle.substring(1, questionStyle.length());
                    ArrayList<String> aaa = judgeData.get(questionStyle);
                    aaa.add(questionString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("训练集读取完毕");
    }

    public String getQuestionClass(ArrayList<String> feature) {
        //各个类在总训练集中的比例
        double[] typeProportion = new double[this.questionTypeList.size()];
        for (int i = 0; i < typeProportion.length; i++)
            typeProportion[i] = (double) this.judgeData.get(this.questionTypeList.get(i)).size() / this.judgeDataNum;
        //每个特征词在每个问题类中的比例
        double[][] wordProportion = new double[this.questionTypeList.size()][feature.size()];
        for (int i = 0; i < this.questionTypeList.size(); i++) {
            ArrayList<String> sourceQuestion = this.judgeData.get(this.questionTypeList.get(i));
            for (String sStr : sourceQuestion)
                for (int j = 0; j < feature.size(); j++)//每个特征词
                    if (sStr.contains(feature.get(j)))
                        wordProportion[i][j] += 1.0 / sourceQuestion.size();
        }

        for (int i = 0; i < wordProportion.length; i++)
            for (int j = 0; j < wordProportion[i].length; j++)
                wordProportion[i][j] *= typeProportion[i];

        double[] featureTypeProportion = new double[this.questionTypeList.size()];
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
        return this.questionTypeList.get(maxIndex);
    }

    public static void main(String[] args) throws Exception {
        new BayesClassifier().loadJudgeData();
    }

}
