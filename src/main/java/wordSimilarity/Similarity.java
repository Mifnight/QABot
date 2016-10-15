package wordSimilarity;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dependency.CRFDependencyParser;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Luyao-Li on 2016/9/14.
 */
public class Similarity {
    private String wordNo1;
    private String wordNo2;

    public Similarity(String wordNo1, String wordNo2){
        this.wordNo1 = wordNo1;
        this.wordNo2 = wordNo2;
    }

    public void setWordNo1(String wordNo1) {
        this.wordNo1 = wordNo1;
    }

    public void setWordNo2(String wordNo2) {
        this.wordNo2 = wordNo2;
    }

    public int sameCharNum(){
        int i=0;
        for(i=0;i<wordNo1.length()&&i<wordNo2.length();i++){
            if(i==2||i==5){
                int result1=wordNo1.charAt(i)*10+wordNo1.charAt(i+1);
                int result2=wordNo2.charAt(i)*10+wordNo2.charAt(i+1);
                if(result1!=result2)return i;
                else i++;
            }
            else if(wordNo1.charAt(i)!=wordNo2.charAt(i))return i;
        }
        return i;
    }

    public double getSimilarity(WordMap wordMap){
            int k =0;
            int num=sameCharNum();
            //System.out.println(num);
            if(num==0)return 0.1;
            if(num==8){
                if(wordNo1.charAt(7)=='@'||wordNo2.charAt(7)=='@')return 0.0; //@
                if(wordNo1.substring(0,7).equals(wordNo2.substring(0,7))){ //=，#
                    if(wordNo1.charAt(7)=='=')return 1.0;
                    else if(wordNo1.charAt(7)=='#')return 0.5;
                    else System.out.println("ERROR IN SIMILARITY");
                }
            }
            else {
                int n = wordMap.sameFirstWordsNum(wordNo1.substring(0, num));
                //System.out.println(n);
               if(num==2||num==5){
                    k = abs((wordNo1.charAt(num)-'0')*10+(wordNo1.charAt(num+1)-'0')-(wordNo2.charAt(num)-'0')*10-(wordNo2.charAt(num+1)-'0'));
                   //System.out.println(k);
                   if(num==2)return 0.8*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                    else return 0.96*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                }
                else k = abs(wordNo1.charAt(num)-wordNo2.charAt(num));
               // System.out.println(k);
                if(num==1){
                    return 0.65*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                }
                else if(num==4){
                    return 0.9*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                }
                else
                    System.out.println("ERROR IN SIMILARITY");
            }
        return 0.0;
    }

    public static double getMaxSimilarity(String word1, String word2){
        double result = 0.0;
        ArrayList<Double> valueList = new ArrayList<Double>();
        WordMap wordMap = new WordMap();
        ArrayList<String> wordNo1List = wordMap.getWordNoByValue(word1);
        ArrayList<String> wordNo2List = wordMap.getWordNoByValue(word2);
        for(int i=0;i<wordNo1List.size();i++){
            for(int j=0;j<wordNo2List.size();j++){
                valueList.add(new Similarity(wordNo1List.get(i), wordNo2List.get(j)).getSimilarity(wordMap));
            }
        }
        for(int i=0;i<valueList.size();i++){
            //System.out.println(valueList.get(i));
            if(valueList.get(i) > result)
                result = valueList.get(i);
        }
        //System.out.printf("%.10f\n",result);
        return result;
    }

    public static void main(String[] args){
       // System.out.println(getMaxSimilarity("鱼", "虫"));
        //System.out.println(getMaxSimilarity("番茄", "西红柿"));
        //System.out.println(HanLP.parseDependency("1"));
    }
}
