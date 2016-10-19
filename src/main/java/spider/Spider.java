package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yisic on 2016/8/29.
 */
public class Spider {
    private static String getAnswerFromZhiDaoPage(String url) throws Exception {
        //只抽取有最佳答案的百度知道问题对
        Document document = Jsoup.connect(url).get();
        try {
            return document.getElementsByClass("best-text").get(0).text();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public static ArrayList<String> getAnswerFromZhiDao(String question) {
        ArrayList<String> answerSet = new ArrayList<String>();
        try {
            String url = "http://zhidao.baidu.com/search?ie=gbk&word=" + URLEncoder.encode(question, "GBK");
            Document document = Jsoup.connect(url).get();
            //最佳匹配
            String bestZhiDaoQAURL = "";
            try {
                bestZhiDaoQAURL = document.getElementsByClass("special-box-p").get(0).getElementsByClass("dt").get(0).getElementsByAttribute("href").get(0).attr("abs:href");
            } catch (IndexOutOfBoundsException e) {
                //answerSet.add("无");
            }
            if (!bestZhiDaoQAURL.equals(""))
                answerSet.add(getAnswerFromZhiDaoPage(bestZhiDaoQAURL));
            //第一页所有非最佳匹配问答对
            Elements answerURL = document.getElementsByClass("ti");
            for (Element ansURL : answerURL) {
                String urlTemp = ansURL.attr("abs:href");
                String ansTemp;
                if (!urlTemp.equals("") && !(ansTemp = getAnswerFromZhiDaoPage(urlTemp)).equals(""))
                    answerSet.add(ansTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return answerSet;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String question;
        ArrayList<String> answerSet;
        while (!(question = scanner.nextLine()).equals("end")) {
            answerSet = getAnswerFromZhiDao(question);
            for (int i = 0; i < answerSet.size(); i++) {
                System.out.println(i + ":");
                System.out.println(answerSet.get(i));
            }
        }
    }
}
