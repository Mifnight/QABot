package cn.buptteam.test.structure;

import java.util.ArrayList;

/**
 * Created by yisic on 2016/10/19.
 */
public class KeyWord {
    public ArrayList<String> wordList;

    public KeyWord(String word) {
        this.wordList = new ArrayList<String>();
        this.wordList.add(word);
    }

    public String getProtoWord() {
        return wordList.get(0);
    }

    public ArrayList<String> getWordSet() {
        return wordList;
    }
}
