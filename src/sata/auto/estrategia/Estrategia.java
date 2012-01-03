package sata.auto.estrategia;

import java.util.ArrayList;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;
import sata.domain.util.LoggerUtil;

public abstract class Estrategia {
	
	Integer anoInicial;
	Integer anoFinal;
	TipoCalculoValorInvestido tipoCalculoValorInvestido = TipoCalculoValorInvestido.TOTAL_COMPRADO;
	
	static List<Simulacao> simulacoes = new ArrayList<Simulacao>();
	
	public void executa(TipoRelatorio tipoRelatorio) {
		LoggerUtil.setup(this);
		Resultado resultado = new Resultado();
		prepara();
		
		for (Simulacao simulacao: simulacoes) {
			if (simulacao.getAnoInicial() == null && anoInicial != null)
				simulacao.setAnoInicial(anoInicial);
			if (simulacao.getAnoFinal() == null && anoFinal != null)
				simulacao.setAnoFinal(anoFinal);
			if (simulacao.getTipoCalculoValorInvestido() == null && tipoCalculoValorInvestido != null)
				simulacao.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
			resultado.addResultado(simulacao.getResultado());
		}
		
		try {
			LoggerUtil.log(resultado.imprime(tipoRelatorio));
		} catch (CotacaoInexistenteEX e) {
			LoggerUtil.log(e.getMessage());
		}
	}
	
	public abstract void prepara();

}
