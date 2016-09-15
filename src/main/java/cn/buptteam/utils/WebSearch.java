package cn.buptteam.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bitholic on 16/9/14.
 */
public class WebSearch {
    public static void main(String[] args) throws  Exception{
        String question = "高速公路最高时速是多少?";
        //getContentFromGoogle(question, 10);
        getContentFromBaidu(question,2);
    }


    public static String getContentFromGoogle(String question, int limit){
        String url = "https://www.google.com/#&q=" + question;
        String content = "";
        System.out.println(url);
        try{
            Document document = Jsoup.connect(url).timeout(10000).get();
            //System.out.println(document);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return content;
    }

    /**
     *
     * @param question
     * @param limit  - the max number of pages
     * @return
     */
    public static String getContentFromBaidu(String question, int limit){
        String url = "http://www.baidu.com/s?wd=" + question + "&fn=";
        String content = "";
        try{
            ArrayList<String> urlist = new ArrayList<String>();
            for (int i = 1; i <= limit; i++) {
                String nurl = url + i * 10;
                Document document = Jsoup.connect(nurl).timeout(10000).get();
                Elements elements = document.getElementsByTag("h3");
                for(Element e: elements){
                    urlist.add(e.getElementsByAttribute("href").first().attr("href"));
                }
            }
            for (String s : urlist) {
                Document document = Jsoup.connect(s).timeout(10000).get();
                System.out.println(s);
                Elements paraElements = document.body().getElementsByTag("p");
                for (Element e : paraElements) {
                    content += e.text() + "\n";
                }

            }
            System.out.println(content);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return content;
    }
}
