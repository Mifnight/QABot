package etc;

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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by yisic on 2016/9/14.
 */
class QAD {
    public String q;
    public String a;

    QAD(String q, String a) {
        this.q = q;
        this.a = a;
    }
}

public class QAtxtToXML {
    public static void saveXML(ArrayList<QAD> qadArrayList) throws Exception {
        /*****/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();


        Element root = document.createElement("QABANK");
        document.appendChild(root);
        for (QAD node : qadArrayList) {
            Element qa = document.createElement("QA");
            Element question = document.createElement("Question");
            question.appendChild(document.createTextNode(node.q));
            qa.appendChild(question);
            Element answer = document.createElement("Answer");
            answer.appendChild(document.createTextNode(node.a));
            qa.appendChild(answer);
            root.appendChild(qa);
        }


        /*****/
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        PrintWriter pw = new PrintWriter(new FileOutputStream("src/main/resources/qaSet.xml"));
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
        System.out.println("生成XML文件成功!");


    }

    public static void main(String[] args) throws Exception {
        File rFile = new File("src/main/resources/QAs.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(rFile), "UTF-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String txtLine;
        String qStr;
        String aStr;
        ArrayList<QAD> qadArrayList = new ArrayList<QAD>();


        /*****/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();


        Element root = document.createElement("QABANK");
        document.appendChild(root);


        /*****/
        txtLine = bufferedReader.readLine();
        while (true) {
//            txtLine = bufferedReader.readLine();
            txtLine = txtLine.substring(2, txtLine.length());
            qStr = txtLine;
            txtLine = bufferedReader.readLine();
            txtLine = txtLine.substring(2, txtLine.length());
            aStr = txtLine;
            while (true) {
                txtLine = bufferedReader.readLine();
                if (txtLine == null) {
                    qadArrayList.add(new QAD(qStr, aStr));
                    saveXML(qadArrayList);
                    return;
                }
                if (txtLine.contains("Q:"))
                    break;
                aStr += "\n" + txtLine;
            }
            qadArrayList.add(new QAD(qStr, aStr));
        }

    }
}
