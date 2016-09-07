package cn.buptteam.dao;

import cn.buptteam.utils.GetAnswers;
import cn.buptteam.utils.HibernateUtil;
import com.google.gson.Gson;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bitholic on 16/9/6.
 */
public class QAsAccess {
    public static void main(String[] args) {
        textToDB();
    }

    public static void getQAs(){
        try{
            Session session = HibernateUtil.getSession();
            Query query = session.createQuery("from QAs");

            List<QAs> qas = query.list();
            System.out.println(new Gson().toJson(qas));
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            HibernateUtil.closeSession();
        }
    }

    public static void addQAs(QAs qas){
        try{
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.save(qas);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public static void textToDB(){
        try {
            HashMap<String, String> textQAs = GetAnswers.getQAs();
            for(Map.Entry<String, String> entry: textQAs.entrySet()){
                addQAs(new QAs(entry.getKey(),entry.getValue()));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
