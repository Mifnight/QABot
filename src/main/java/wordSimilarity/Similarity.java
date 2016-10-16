package wordSimilarity;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Luyao-Li on 2016/9/14.
 */
public class Similarity {
    private String word1;
    private String word2;

    public Similarity(String word1, String word2){
        this.word1 = word1;
        this.word2 = word2;
    }

    public int sameCharNum(){
        int i=0;
        for(i=0; i<word1.length()&&i< word2.length(); i++){
            if(i==2||i==5){
                int result1=word1.charAt(i)*10+word1.charAt(i+1);
                int result2= word2.charAt(i)*10+ word2.charAt(i+1);
                if(result1!=result2)return i;
                else i++;
            }
            else if(word1.charAt(i)!= word2.charAt(i))return i;
        }
        return i;
    }

    public double getSimilarity(WordMap wordMap){
            int k =0;
            int num=sameCharNum();
            if(num==0)return 0.1;
            if(num==8){
                if(word1.charAt(7)=='@'|| word2.charAt(7)=='@')return 0.0; //@
                if(word1.substring(0,7).equals(word2.substring(0,7))){ //=，#
                    if(word1.charAt(7)=='=')return 1.0;
                    else if(word1.charAt(7)=='#')return 0.5;
                    else System.out.println("ERROR IN SIMILARITY");
                }
            }
            else {
                int n = wordMap.sameFirstWordsNum(word1.substring(0, num));
               if(num==2||num==5){
                    k = abs((word1.charAt(num)-'0')*10+(word1.charAt(num+1)-'0')-(word2.charAt(num)-'0')*10-(word2.charAt(num+1)-'0'));
                   if(num==2)return 0.8*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                    else return 0.96*Math.cos(n*Math.PI/180)*(n-k+1)/n;
                }
                else k = abs(word1.charAt(num)- word2.charAt(num));
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
            if(valueList.get(i) > result)
                result = valueList.get(i);
        }
        return result;
    }

    public static void main(String[] args){
        System.out.println(getMaxSimilarity("迅速的", "正确的" ));
        //List<Term> list = HanLP.newSegment().enableAllNamedEntityRecognize(true).seg("姚明应该如何超车");
        //System.out.println(list);
    }
}
