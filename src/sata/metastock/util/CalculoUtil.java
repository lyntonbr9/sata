/*
 * Created on 24/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.util;

import java.math.BigDecimal;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalculoUtil {

	
	public static double getPerdaMedia(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double perda = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					perda += new BigDecimal(closes[i] - closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		return new BigDecimal(perda).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static double getGanhoMedio(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double ganho = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					ganho += new BigDecimal(closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).add(new BigDecimal(-1)).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		
		if(comprasPositivas==0){
			return 0.0;
		}
		
		return new BigDecimal(ganho).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static int getNumeroNegativas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasNegativas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					comprasNegativas++;
				}
				
			}
		}
				
		return comprasNegativas;
	} 
	
	public static int getNumeroPositivas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
				
		return comprasPositivas;
	} 
	
	
	
	
	public static double getEfecienciaUsandoMaximaAcimaDe(double ganhoMinimo,double high[],double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && high[i+days] > closes[i]){
					double alta = new BigDecimal(high[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() -1;
					if(alta>=ganhoMinimo){
						comprasPositivas++;
					}
					
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	
	public static double getEfecienciaUsandoMaxima(double high[],double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && high[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getEfeciencia12(double closes[],boolean[] ptosCompra){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+2) < (ptosCompra.length-1) && closes[i+1] > closes[i]){
					if(closes[i+2] > closes[i]){
						comprasPositivas++;
					}
					
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getEfeciencia(double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getTaxaAltasAcoes(double closes[],int days){
		
		int altas = 0;
		for(int i=0;i<closes.length;i++){
			    
				if((i+days) < (closes.length-1) && closes[i+days] > closes[i]){
					altas++;
				}
				
			
		}
		
		double result = new BigDecimal(altas).divide(new BigDecimal(closes.length),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
}
