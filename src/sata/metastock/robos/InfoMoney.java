/*
 * Created on 10/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InfoMoney {
	
	public static void main(String args[]){
		InfoMoney.getTodasCotacoes();
	}
	
	public static ArrayList getTodasCotacoes(){
		
		ArrayList cotacoes = new ArrayList();
	
		try {
			
			System.out.println("Conectando...");
		    String link = "http://www.infomoney.com.br/cotacoes/acoes_indices.asp?indice=IBOVESPA";

			URL url = new URL(link);
			//System.out.println();
			String thisLine;
	      //  DataInputStream myInput = new DataInputStream(url.openStream());
	        System.out.println("Recuperou página....");
	        StringBuffer html = new StringBuffer();
	        String line = "";
	       
	        BufferedReader d = new BufferedReader(new InputStreamReader(url.openStream()));

	        System.out.println("concatenando HTML...");
	        while ((line=d.readLine())!=null ){ 
	       	    System.out.println(line);
	       	    html.append(line + "\n");
	        }    
	        
	        System.out.println("Extraindo valores...");
	        //extraiValores(html.toString());
	        
	  //      System.out.println(html);      
	                  
	                
	} catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	
		return null;
		
	}

}
