/*
 * Created on 02/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CotacaoLopesFilho {

	private static String cotacao = "43,80";
	private static String hora = "07:00:00";
	
    public static void get(String codigo){ 
       // Populate the hashtable with key value pairs of 
       // the parameter name and 
       // value. In this case, we only have the parameter 
       // named "CONTENT" and the 
       // value of CONTENT will be "HELLO JSP !" 
        
       Hashtable h = new Hashtable(); 
       h.put("papel", codigo); 
       //h.put("ONEMORECONTENT", "HELLO POST !"); 
        
       // POST it ! 
       					  
       String html = POST("https://www.ondeinvestirbylopesfilho.com.br/cli/agr/cot/cotacao.asp", 
                            h); 
        
       System.out.println(html);
       
       int corte = html.indexOf(codigo.toUpperCase());
       if(corte < 0){
      
       	cotacao = "61,11";
       }else{
	        html = html.substring(corte);
	    	        
	        int inicio = html.indexOf("align=\"center\">");
	        int fim = html.indexOf("</td>",inicio);
	        
	        cotacao = html.substring(inicio + "align=\"center\">".length(),fim);
       }
       
       Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-03:00"));
       
       String hour24 = "" + cal.get(Calendar.HOUR_OF_DAY);     // 0..23
       String min = "" + cal.get(Calendar.MINUTE);             // 0..59
       String sec = "" + cal.get(Calendar.SECOND);             // 0..59
       
       if(hour24.length() == 1){
       	 hour24 = "0" + hour24;
       }
       
       if(min.length() == 1){
       	min = "0" + min;
      }
       
      if(sec.length() == 1){
      	sec = "0" + sec;
      }
       
       hora = hour24 + ":" + min + ":" + sec;
       
       
    } 
    
   /** 
    * The POST method. Accepts 2 parameters 
    * @param targetURL : The URL to POST to. 
    * @param contentHash : The hashtable of the paramters to be posted. 
    *  
    * @return The String returned as a result of POSTing. 
    */ 
   public static String POST(String targetURL, Hashtable contentHash)    {     
		      
   		try{		
   			
   			System.setProperty("http.proxyHost", "172.31.2.218");
	   	    System.setProperty("http.proxyPort", "8080"); 

   			   URL url; 
		       URLConnection conn; 
		        
		       // The data streams used to read from and write to the URL connection. 
		       DataOutputStream out; 
		       DataInputStream in; 
		        
		       // String returned as the result of the POST. 
		       String returnString = ""; 
		                
		       // Create the URL object and make a connection to it. 
		       url = new URL (targetURL); 
		       conn = url.openConnection(); 
		                
		       // Set connection parameters. We need to perform input and output, 
		       // so set both as true. 
		       conn.setDoInput (true); 
		       conn.setDoOutput (true); 
		        
		       // Disable use of caches. 
		       conn.setUseCaches (false); 
		                
		       // Set the content type we are POSTing. We impersonate it as 
		       // encoded form data 
		       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		        
		       // get the output stream to POST to. 
		       out = new DataOutputStream (conn.getOutputStream ()); 
		       String content = ""; 
		        
		       // Create a single String value to be POSTED from the parameters passed 
		       // to us. This is done by making "name"="value" pairs for all the keys 
		       // in the Hashtable passed to us. 
		       Enumeration e = contentHash.keys(); 
		       boolean first = true; 
		       while(e.hasMoreElements()) 
		       {             
		           // For each key and value pair in the hashtable 
		           Object key = e.nextElement(); 
		           Object value = contentHash.get(key); 
		            
		           // If this is not the first key-value pair in the hashtable, 
		           // concantenate an "&" sign to the constructed String 
		           if(!first)  
		               content += "&"; 
		                                    
		           // append to a single string. Encode the value portion 
		           content += (String)key + "=" + URLEncoder.encode((String)value); 
		            
		           first = false; 
		       } 
		        
		       // Write out the bytes of the content string to the stream. 
		       out.writeBytes (content); 
		       out.flush (); 
		       out.close (); 
		        
		       // Read input from the input stream. 
		       in = new DataInputStream (conn.getInputStream ()); 
		        
		       String str;         
		       while (null != ((str = in.readLine()))) 
		       { 
		           returnString += str + "\n"; 
		       } 
		        
		       in.close (); 
		        
		       // return the string that was read. 
		       return returnString; 
   		}catch(Exception e){
   			e.printStackTrace();
   		}
		       
   		return "";
		       
   } 
   
	/**
	 * @return Returns the cotacao.
	 */
	public static String getCotacao() {
		return cotacao;
	}
	/**
	 * @param cotacao The cotacao to set.
	 */
	public static void setCotacao(String cotacao) {
		CotacaoLopesFilho.cotacao = cotacao;
	}
	/**
	 * @return Returns the hora.
	 */
	public static String getHora() {
		return hora;
	}
	/**
	 * @param hora The hora to set.
	 */
	public static void setHora(String hora) {
		CotacaoLopesFilho.hora = hora;
	}
}
