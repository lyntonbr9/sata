package sata.auto.rest.resource;

import java.math.BigDecimal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import sata.metastock.util.BlackScholes;

//call true for call options, false for puts
//precoAcao the price of the underlying
//precoExercicioOpcao the strike of the option
//tempoParaVencimentoOpcaoEmAnos the time to expiration, in years
//taxaDeJuros the risk free interest rate
//volatilidade the volatility

@Path("/volatilidade/{ehCall}/{precoAcao}/{precoExercicioOpcao}/{precoOpcao}/{diasParaVencimento}/{taxaDeJuros}")
public class VolatilidadeUtilizadaResource {

	@GET
	@Produces("text/plain")
	public String getVolatilidadeUtilizada(@PathParam("ehCall") String ehCall, @PathParam("precoAcao") String precoAcao,
			@PathParam("precoExercicioOpcao") String precoExercicioOpcao, @PathParam("precoOpcao") String precoOpcao,
			@PathParam("diasParaVencimento") String diasParaVencimento, 
			@PathParam("taxaDeJuros") String taxaDeJuros) {

		// recupera os parametros
		boolean call = (ehCall.equals("0") ? false : true);
		double precoAc = Double.parseDouble(precoAcao.replace(",", "."));
		double precoExerOpc = Double.parseDouble(precoExercicioOpcao.replace(",", "."));
		double precoOpc = Double.parseDouble(precoOpcao.replace(",", "."));
		double qtdDiasVencEmAnos = BlackScholes.getQtdDiasEmAnos(Integer.parseInt(diasParaVencimento));
		double taxaJur = Double.parseDouble(taxaDeJuros.replace(",", "."))/100;
		double volatilidade = 0.0;
		
		// calcula a volatilidade que está sendo utilizada
		double menorDiferenca = Double.POSITIVE_INFINITY;
		for (double volat = 0.01; volat <= 1.0; volat += 0.01) {
			double precoOpcTemp = BlackScholes.blackScholes(call, precoAc, precoExerOpc, 
								qtdDiasVencEmAnos, taxaJur, volat);
			double diferenca = Math.abs(precoOpc - precoOpcTemp);
			if (diferenca < menorDiferenca) {
				menorDiferenca = diferenca;
				volatilidade = volat*100;
			}
		}
		
		// formata o resultado
		BigDecimal volatilidadeFormatada = new BigDecimal(volatilidade).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		String resultado = "ehCall: " + call + "\n" + "precoAcao: " + precoAc + "\n" +
				"precoExercicioOpcao: " + precoExerOpc + "\n" + 
				"precoOpcao: " + precoOpc + "\n" + 
				"diasParaVencimentoEmAnos: " + qtdDiasVencEmAnos + "\n" +
				"taxaDeJuros: " + taxaJur  + "\n\n\n" +
				"volatilidade em uso: " + volatilidadeFormatada + "%";
		
		return resultado;
		
	}
	
}
