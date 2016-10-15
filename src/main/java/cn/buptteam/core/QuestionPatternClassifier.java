package cn.buptteam.core;

/**
 * Created by bitholic on 16/9/15.
 */

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dependency.CRFDependencyParser;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 疑问句句法分析器
 *
 * 1)语气助词表示疑问
 * 2)疑问代词表示疑问
 * 3)谓语的正反形式表示疑问
 * 4)用"是...还是..."表示疑问
 */

/** CoNLL依存句法分析结果
    1    ID      当前词在句子中的序号，１开始.
    2    FORM    当前词语或标点
    3    LEMMA   当前词语（或标点）的原型或词干，在中文中，此列与FORM相同
    4    POSTAG  当前词语的词性（粗粒度）
    5    CPOSTAG 当前词语的词性（细粒度）
    6    FEATS   句法特征，在本次评测中，此列未被使用，全部以下划线代替。
    7    HEAD    当前词语的中心词
    8    DEPREL  当前词语与中心词的依存关系
 */

public class QuestionPatternClassifier {
    public static void main(String[] args) {
        String s1 = "我是谁?";
        String s2 = "开车撞了人该怎么办?";
        String s3 = "是谁发现了南极大陆?";
        String s4 = "哪儿有非常好喝的水?";
        String s5 = "马云的爸爸是谁?";
        test(s5);
        System.out.println(CRFDependencyParser.compute(s5));
        //System.out.println(HanLP.parseDependency("我是谁?"));
    }

    public static void test(String question){
        Boolean verbFirst = false;

        CoNLLSentence sentence = CRFDependencyParser.compute(question);

        for (CoNLLWord coNLLWord : sentence) {
            System.out.println(coNLLWord.HEAD.ID);
            if(coNLLWord.HEAD.ID == 0 && coNLLWord.POSTAG.equals("v")){
                verbFirst = true;
            }
        }

        //(一)句子核心是动词
        if(verbFirst){
            System.out.println("String");
        }
        // 1) 找到疑问代词的位置
        int ryPos = 0;
        int corePos = 0;
        for (CoNLLWord coNLLWord : sentence.getWordArray()) {
            if(coNLLWord.CPOSTAG.equals("ry")){
                ryPos = coNLLWord.ID;
            }else if(coNLLWord.HEAD.equals(0)){
                System.out.println(coNLLWord.ID);
            }
        }
        if(ryPos != 0){

        }
    }

}
