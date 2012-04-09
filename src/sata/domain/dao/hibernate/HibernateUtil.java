package sata.domain.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class HibernateUtil implements IConstants {
	private static final SessionFactory sessionFactory;  
    
    private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();  
  
    static {  
    	String ambiente = SATAPropertyLoader.getProperty(PROP_SATA_AMBIENTE);
        sessionFactory = new Configuration().configure("conf/hibernate.cfg."+ambiente+".xml").buildSessionFactory();  
    }  
  
    public static Session getSession() {  
          
        if (sessions.get() != null) {  
        }  
        sessions.set(sessionFactory.openSession());  
        return sessions.get();  
    }  
  
    public static void closeCurrentSession() {  
        sessions.get().close();  
        sessions.set(null);  
    }  
  
    public static Session currentSession() {  
        return sessions.get();  
    }  
      
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    }  
}
