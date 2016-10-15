package wordSimilarity;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Luyao-Li on 2016/9/14.
 */
//public class WordTree {
//    private ArrayList<ArrayList<WordNode>> rootNodeList = new ArrayList<ArrayList<WordNode>>();
//
//    public static ArrayList<String> getWordList(String str){
//        String word;
//        ArrayList<String> wordList = new ArrayList<String>();
//        int end=0, begin=0,i=0;
//        int flag=0;
//        while(end!=-1&&flag==0){
//            begin = str.indexOf(" ",end);
//            end = str.indexOf(" ",begin+1);
//            if(end==-1){
//                end=str.length();
//                flag=1;
//            }
//            word=str.substring(begin+1,end);
//            wordList.add(word);
//        }
//      //  System.out.println(wordList);
//        return wordList;
//    }
//
//    public static void getWordNode(WordTree wordTree,BufferedReader br)throws IOException{
//        int index, nextIndex;
//        int i=0;
//        ArrayList<WordNode> wordNodeList = new ArrayList<WordNode>();
//        String str=br.readLine();
//        while(str!=null){
//            index = str.charAt(0)-'A';
//            WordNode node = new WordNode(getWordList(str),1);
//            wordNodeList.add(node);
//            str = br.readLine();
//            if(str==null){
//                wordTree.rootNodeList.add(wordNodeList);
//                break;
//            }
//            nextIndex=str.charAt(0)-'A';
//            if(nextIndex!=index){
//                wordTree.rootNodeList.add(wordNodeList);
//                wordNodeList = new ArrayList<WordNode>();
//            }
//        }
//        System.out.println(wordTree.rootNodeList);
//        System.out.println(wordTree.rootNodeList.size());
//    }
//
//    public static void main(String[] args)throws IOException{
//        File file = new File(Object.class.getResource("/hgd.txt").getFile());
//        InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
//        BufferedReader br = new BufferedReader(isr);
//        WordTree wordTree = new WordTree();
//        getWordNode(wordTree,br);
//    }
//
//}
