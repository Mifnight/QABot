package cn.buptteam.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by bitholic on 16/9/17.
 */
public class QuestionSyntaxAnalysis {
    public static void main(String[] args) {
        getQuestionFromZhihu();
    }

    //由于知乎的问题偏向于描述类,这里爬取一定量的知乎问题来分析描述类问题特征
    public static ArrayList<String> getQuestionFromZhihu(){
        String url = "https://www.zhihu.com/topic/19550517/top-answers?page=";
        ArrayList<String> questions = new ArrayList<String>();
        try {
            for (int i = 1; i <= 10; i++) {
                String nurl = url + i;
                Document document = Jsoup.connect(nurl).timeout(5000).get();
                Element element = document.getElementById("zh-topic-top-page-list");
                Elements elements = element.getElementsByTag("h2");
                for (Element e : elements) {
                    questions.add(e.text());
                    System.out.println(e.text());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return questions;
    }
}
