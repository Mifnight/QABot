package wordSimilarity;

import java.util.ArrayList;

/**
 * Created by Luyao-Li on 2016/9/14.
 */
public class WordNode {
    private ArrayList<String> wordList;
    private int level;
    private WordNode nextWordNode;

    public WordNode(ArrayList<String> wordList, int level){
        this.wordList=wordList;
        this.level=level;
    }

    public void setWordList(ArrayList<String> wordList) {
        this.wordList=wordList;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setNextWordNode(WordNode nextWordNode) {
        this.nextWordNode = nextWordNode;
    }

    public WordNode getNextWordNode() {
        return nextWordNode;
    }
}
