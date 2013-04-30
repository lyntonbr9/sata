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

@Path("/bs/{ehCall}/{precoAcao}/{precoExercicioOpcao}/{diasParaVencimento}/{volatilidade}/{taxaDeJuros}")
public class BlackScholesResource {

	@GET
	@Produces("text/plain")
	public String getVolatilidadeUtilizada(@PathParam("ehCall") String ehCall, @PathParam("precoAcao") String precoAcao,
			@PathParam("precoExercicioOpcao") String precoExercicioOpcao, 
			@PathParam("diasParaVencimento") String diasParaVencimento, 
			@PathParam("volatilidade") String volatilidade,
			@PathParam("taxaDeJuros") String taxaDeJuros) {

		// recupera os parametros
		boolean call = (ehCall.equals("0") ? false : true);
		double precoAc = Double.parseDouble(precoAcao.replace(",", "."));
		double precoExerOpc = Double.parseDouble(precoExercicioOpcao.replace(",", "."));
		double qtdDiasVencEmAnos = BlackScholes.getQtdDiasEmAnos(Integer.parseInt(diasParaVencimento));
		double volat = Double.parseDouble(volatilidade.replace(",", "."))/100;
		double taxaJur = Double.parseDouble(taxaDeJuros.replace(",", "."))/100;
		
		// calcula o blackscholes
		double precoOpc = BlackScholes.blackScholes(call, precoAc, precoExerOpc, qtdDiasVencEmAnos, taxaJur, volat);
		
		// formata o resultado
		BigDecimal precoOpcaoFormatado = new BigDecimal(precoOpc).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		String resultado = "ehCall: " + call + "\n" + "precoAcao: " + precoAc + "\n" +
				"precoExercicioOpcao: " + precoExerOpc + "\n" + 
				"diasParaVencimentoEmAnos: " + qtdDiasVencEmAnos + "\n" +
				"volatilidade: " + volat + "%\n" +
				"taxaDeJuros: " + taxaJur  + "\n\n\n" +
				"precoOpcao: " + precoOpcaoFormatado;
		
		return resultado;
		
	}
	
}
