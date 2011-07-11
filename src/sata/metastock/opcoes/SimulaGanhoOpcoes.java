package sata.metastock.opcoes;

import java.math.BigDecimal;

import sata.metastock.robos.CotacaoLopesFilho;

public class SimulaGanhoOpcoes {
     public static void main(String[] args){
        CotacaoLopesFilho.get("petr4");
        
        System.out.println("Cotação petrobras:" + CotacaoLopesFilho.getCotacao().replace(',','.') + "\n");       
        BigDecimal precoCompra = new BigDecimal(CotacaoLopesFilho.getCotacao().replace(',','.'));
        for(int i=0;i<22;i=i+2){
         
        
           System.out.print("petrk" + (14+i)); 
      
           CotacaoLopesFilho.get("petrk" + (14+i));
           System.out.println (": " + CotacaoLopesFilho.getCotacao());
           
           BigDecimal precoOpcao = new BigDecimal(CotacaoLopesFilho.getCotacao().replace(',','.'));
           BigDecimal percOpcao =
precoOpcao.divide(precoCompra,BigDecimal.ROUND_HALF_EVEN,6);
           System.out.println("Percentual opção: " + percOpcao);
       
           
           
           BigDecimal opcao = new BigDecimal((14+i));
           BigDecimal percPerda =
precoCompra.subtract(opcao).divide(precoCompra,BigDecimal.ROUND_HALF_EVEN,6);

           BigDecimal ganhoPerda = percOpcao.subtract(percPerda);
           System.out.println("petrk" + (14+i) + " " + ganhoPerda + "\n");
        }
     }
}