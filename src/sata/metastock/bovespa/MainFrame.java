/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import sata.metastock.swing.SATAViewWindowListener;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MainFrame {
	
	public static SpaceView grafico = new SpaceView();
    public static JFrame  frame = null;
    private static Method metodoOpacidade;
    
    static{
    	try {
    		Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
    		metodoOpacidade = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
   // public static JTransFrame  frame = null;
	
	public static void main(String args[]){
		
	    // Create the frame
	    String title = "SATA - Sistema de Analise Tecnica Automatizada";
	    frame = new JFrame(title);
	   
	    //seta a opacidade da janela
	    setOpacidade(1.0f);
	    
	    //frame = new JTransFrame(0,0,0);
	    // Create a component to add to the frame
//	    JComponent comp = new JTextArea();
	    
	    // Add the component to the frame's content pane;
	    // by default, the content pane has a border layout
	    Menu menu = new Menu();
	    grafico.setAcao(menu.getAcao());
	    frame.getContentPane().add(menu,BorderLayout.NORTH);
	    frame.getContentPane().add(grafico, BorderLayout.CENTER);
	    
	    // Show the frame
	    int width = 1024;
	    int height = 730;
	    frame.setSize(width, height);
	    
	    Color color = new Color(frame.getBackground().getRed(),
	    						frame.getBackground().getGreen(),
	    						frame.getBackground().getBlue(),
	    						255);
	    
	    
	    frame.setBackground(color);
	    
	    frame.addWindowListener(new SATAViewWindowListener());
	    
	    frame.setVisible(true);

	}
	

	public static void setWaitCursor(boolean waitCursor){
		if(waitCursor){
			frame.setCursor(Cursor.WAIT_CURSOR);//Vira uma ampulheta
		}else{
			frame.setCursor(Cursor.DEFAULT_CURSOR);// volta ao estado normal. 
		}
	}
	
	public static void setAcao(String acao){
		
		grafico.setAcao(acao);
		grafico.repaint();
	}
	
	public static void setDiasCandle(int dias){
		
		grafico.setDiasCandle(dias);
		grafico.repaint();
	}
	
	public static void repaintSpaceView(){		
		grafico.repaint();
	}
	
	public static void showPreco(String preco){
		
		grafico.setToolTipText(preco);
	}
	
	public static void compra(){
		
		grafico.compra(10000,true);
	}
	
	public static void vende(){
		
		grafico.vende();
	}
	
	public static void exibePercentualMomento(){
		
		grafico.exibePercentualMomento();
	}
	
	public static void setIntervaloDia(String dia1, String dia2){
		grafico.setprimeiroDia(Integer.parseInt(dia1));
		grafico.setUltimoDia(Integer.parseInt(dia2));
		grafico.setRetroativo(false);
		grafico.repaint();
    }
    
   
	
	public static void setDias(String dias){
		grafico.setDias(Integer.parseInt(dias));
		grafico.setRetroativo(true);
		grafico.repaint();
	}
	
	public static void setOpacidade(float opacidade){
		try {
			metodoOpacidade.invoke(null, frame, new Float(opacidade));	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
