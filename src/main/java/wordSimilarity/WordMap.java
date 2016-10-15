package wordSimilarity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luyao-Li on 2016/9/14.
 */
public class WordMap {
    private HashMap<String,ArrayList<String>> wordMaps = new HashMap<String, ArrayList<String>>();

    public WordMap(){
        try{
            File file = new File(Object.class.getResource("/hgd.txt").getFile());
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            while(str!=null) {
                wordMaps.put(str.substring(0,str.indexOf(" ")),WordTree.getWordList(str));
                str=br.readLine();
            }
        }catch (IOException e){
            System.out.println("Fail to open hgd.txt.");
        }
    }

    public ArrayList<String> getWordNoByValue(String wordValue){
        ArrayList<String> wordNoList = new ArrayList<String>();
        for(Map.Entry<String,ArrayList<String>> word:wordMaps.entrySet()){
            if(word.getValue().contains(wordValue)){
                wordNoList.add(word.getKey());
            }
        }
        return wordNoList;
    }

    public int sameFirstWordsNum(String str){
        // System.out.println(str);
        int count=0;
        for(Map.Entry<String,ArrayList<String>> word:wordMaps.entrySet()){
            if(word.getKey().substring(0,str.length()).equals(str)){
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args)throws IOException{

    }
}
