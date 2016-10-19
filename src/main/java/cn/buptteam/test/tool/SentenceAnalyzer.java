package cn.buptteam.test.tool;

import cn.buptteam.test.structure.KeyWord;
import cn.buptteam.test.structure.Question;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.util.List;

/**
 * Created by yisic on 2016/10/19.
 */
public class SentenceAnalyzer {
    public static final int MAX_LENGTH = 10;

    public static Question AnalyzeSentence(String sentence) {
        Question question = new Question(sentence);
        //获取关键词，拓展关键词
        for (String word : TextRankKeyword.getKeywordList(sentence, MAX_LENGTH)) {
            KeyWord keyWord = new KeyWord(word);
            WordExpander.ExpandWord(keyWord);
            question.keyWords.add(keyWord);
        }

        //for test
        System.out.print("关键词:\t\t");
        for (String kw : TextRankKeyword.getKeywordList(sentence, MAX_LENGTH))
            System.out.print(kw + "\t");
        System.out.println();
        return question;
    }
}
