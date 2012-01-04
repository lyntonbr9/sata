package sata.auto.estrategia;

import java.util.ArrayList;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;
import sata.domain.util.LoggerUtil;

public abstract class Estrategia {

	Acao acao;
	Integer anoInicial;
	Integer anoFinal;
	TipoCalculoValorInvestido tipoCalculoValorInvestido = TipoCalculoValorInvestido.TOTAL_COMPRADO;
	Resultado resultado;
	
	List<Simulacao> simulacoes = new ArrayList<Simulacao>();
	
	public abstract void prepara(Integer... parametros);
	public abstract String getNomeEstrategia(Integer... parametros);
	
	public void executa(TipoRelatorio tipoRelatorio, Integer... parametros) {
		LoggerUtil.setup(this.getClass());
		if (resultado == null) resultado = new Resultado();
		else resultado.limpa();
		prepara(parametros);
		
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

	public Resultado getResultado() {
		return resultado;
	}
	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public Integer getAnoInicial() {
		return anoInicial;
	}
	public void setAnoInicial(Integer anoInicial) {
		this.anoInicial = anoInicial;
	}
	public Integer getAnoFinal() {
		return anoFinal;
	}
	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}
	public TipoCalculoValorInvestido getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}
	public void setTipoCalculoValorInvestido(
			TipoCalculoValorInvestido tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}
	public List<Simulacao> getSimulacoes() {
		return simulacoes;
	}
	public void setSimulacoes(List<Simulacao> simulacoes) {
		this.simulacoes = simulacoes;
	}
}
