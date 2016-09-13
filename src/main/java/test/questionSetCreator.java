package test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;

/**
 * Created by yisic on 2016/9/12.
 */
public class questionSetCreator {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        File rFile = new File("src/main/resources/qSet.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(rFile), "GBK");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt;


        /*****/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();


        Element root = document.createElement("QUESTIONBANK");
        document.appendChild(root);


        /*****/


        int counter = 0;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            System.out.println(++counter + "/173");
            System.out.println(lineTxt);

            Element doc = document.createElement("DOC");
            Element question = document.createElement("Question");
            question.appendChild(document.createTextNode(lineTxt));
            doc.appendChild(question);
            Element questionStyle = document.createElement("QuestionStyle");
            String style = scanner.next();
            if (style.equals("exit"))
                break;
            questionStyle.appendChild(document.createTextNode(style));
            doc.appendChild(questionStyle);
            root.appendChild(doc);

        }


        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        PrintWriter pw = new PrintWriter(new FileOutputStream("src/main/resources/qSet.xml"));
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
        System.out.println("生成XML文件成功!");


    }
}
