package sata.auto.rest.resource;

import java.math.BigDecimal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import sata.metastock.util.BlackScholes;

@Path("/bsg/{precoExercicioOpcao}/{inicioAcao}/{fimAcao}/{intervaloAcao}/{volatilidade}/{taxaDeJuros}")
public class BSGraphicResource {
	
	static int duracao = 30;
	static int ultimoDia = 3;
	static int intervaloDia = 3;

	@GET
	@Produces("text/plain")
	public String getBSG(@PathParam("precoExercicioOpcao") String precoExercicioOpcao,
						@PathParam("inicioAcao") String inicioAcao,
						@PathParam("fimAcao") String fimAcao,
						@PathParam("intervaloAcao") String intervaloAcao,
						@PathParam("volatilidade") String volatilidade,
						@PathParam("taxaDeJuros") String taxaDeJuros) {
		
		double precoExerOpc = Double.parseDouble(precoExercicioOpcao.replace(",", "."));
		double inicioAc = Double.parseDouble(inicioAcao.replace(",", "."));
		double fimAc = Double.parseDouble(fimAcao.replace(",", "."));
		double intervaloAc = Double.parseDouble(intervaloAcao.replace(",", "."));
		double volat = Double.parseDouble(volatilidade.replace(",", "."))/100;
		double taxaJur = Double.parseDouble(taxaDeJuros.replace(",", "."))/100;

		return calculaBS(precoExerOpc, inicioAc, fimAc, intervaloAc, duracao, ultimoDia, intervaloDia, volat, taxaJur);
		
	}
	
//	public static void main(String[] args) {
//		
//		calculaBS(20.0, 19.0, 22.1, 0.5, duracao, ultimoDia, intervaloDia, 0.26, 0.075);
//		
//	}
	
	//return "19.00,-3,9;19.50,-4,10;20.00,4,20";
	public static String calculaBS(double precoExerc, double inicioAcao, double fimAcao, 
								double intervaloAcao, int duracao, int ultimoDia, int intervaloDia,
								double volatilidade, double taxaDeJuros) {
		String res = "";
		for (double acao= inicioAcao; acao <= fimAcao; acao+=intervaloAcao) {
			BigDecimal bd = new BigDecimal(acao).setScale(2, BigDecimal.ROUND_HALF_UP);
			String acaoStr = bd.toString();
			res += acaoStr + ",";
			for (int diaTmp = duracao; diaTmp >= ultimoDia; diaTmp-=intervaloDia) {
				double qtdDiasVencEmAnos = BlackScholes.getQtdDiasEmAnos(diaTmp);
				double precoOpc = BlackScholes.blackScholes(true, acao, precoExerc, qtdDiasVencEmAnos, taxaDeJuros, volatilidade);
				BigDecimal opcao = new BigDecimal(precoOpc).setScale(2, BigDecimal.ROUND_HALF_UP);
				res += opcao + ",";
			}
			// substitui o ultimo ,
			res = res.substring(0, res.lastIndexOf(","));
			// concatena o fim da linha
			res += ";";
		}
		// substitui o ultimo ;
		res = res.substring(0, res.lastIndexOf(";"));
		System.out.println(res);
		return res;
	}
	
}
