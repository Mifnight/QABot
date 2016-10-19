package cn.buptteam.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by bitholic on 16/9/7.
 */
public class HibernateUtil {
    private static ThreadLocal THREAD_LOCAL = new ThreadLocal();
    private static SessionFactory sessionFactory;

    static{
        try {
            Configuration cfg = new Configuration().configure();
            sessionFactory = cfg.buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Session getSession(){
        Session session = (Session) THREAD_LOCAL.get();
        if(session == null){
            session = sessionFactory.openSession();
            THREAD_LOCAL.set(session);
        }
        return session;
    }

    public static void closeSession(){
        Session session = (Session) THREAD_LOCAL.get();
        if (session != null) {
            session.close(); //如果使用getCurrentSession则无需将Session关闭，因为该Session是保存在当前线程中的，线程执行完毕Session自然会销毁
            THREAD_LOCAL.set(null);
        }
    }

    public static String printException(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception ex) {

        }
        return ret;
    }

}
