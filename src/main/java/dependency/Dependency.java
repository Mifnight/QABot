package dependency;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Luyao-Li on 2016/10/16.
 */
public class Dependency {
    private int coreHead;
    private String coreName;
    private int validMatchPair;
    private HashMap<String, String> pairMap = new HashMap<String, String>();

    public Dependency(){}

    public Dependency(String sentence){
        int count = 0;
        CoNLLSentence coNLLSentence = HanLP.parseDependency(sentence);
        for(CoNLLWord e: coNLLSentence.getWordArrayWithRoot()){
            if(e.ID!=0){
                if(e.HEAD.ID==0) {
                    coreHead = e.ID;
                    coreName = e.NAME;
                }
            }
        }
        for(CoNLLWord e: coNLLSentence.getWordArrayWithRoot()){
            if(e.ID!=0){
                if(e.HEAD.ID == coreHead){
                    Pattern pattern1 = Pattern.compile("^v.*");
                    Pattern pattern2 = Pattern.compile("^n.*");
                    Pattern pattern3 = Pattern.compile("^a.*");
                    if(pattern1.matcher(e.POSTAG).matches()||pattern2.matcher(e.POSTAG).matches()||pattern3.matcher(e.POSTAG).matches()){
                        count++;
                        pairMap.put(e.NAME, coreName);
                    }
                }
            }
        }
        validMatchPair = count;
    }

    public int getCoreHead(){
        return coreHead;
    }

    public String getCoreName(){
        return coreName;
    }

    public int getValidMatchPair(){
        return validMatchPair;
    }

    public HashMap<String, String> getPairMap(){
        return pairMap;
    }

    public static void main(String[] args){
        Dependency dependency = new Dependency("人应该如何过复杂的马路");
        System.out.println(dependency.getCoreHead());
        System.out.println(dependency.getValidMatchPair());
        System.out.println(dependency.getPairMap());
        System.out.println(HanLP.parseDependency("人应该如何过复杂的马路").toString());
    }

}
