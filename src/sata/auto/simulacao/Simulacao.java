package sata.auto.simulacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Stop;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.Dia;
import sata.auto.to.Mes;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class Simulacao implements IConstants {
	
	private List<Operacao> operacoes = new ArrayList<Operacao>();
	private Integer anoInicial;
	private Integer anoFinal;
	private TipoCalculoValorInvestido tipoCalculoValorInvestido;
	private int percValorInvestido = 100;
	private Stop stop;
	
	public Simulacao(Operacao... operacoes) {
		this.operacoes = Arrays.asList(operacoes);
	}
	
	public Resultado getResultado() throws SATAEX {
		System.gc();
		Resultado resultado = new Resultado();
		resultado.setAnoInicial(anoInicial);
		resultado.setAnoFinal(anoFinal);
		resultado.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
		resultado.setPercValorInvestido(percValorInvestido);
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			if (possuiCotacaoNoAno(ano)) {
				for (int iMes=1; iMes <= 12; iMes++) {
					Mes mes = new Mes(iMes,ano);
					try {
						executaOperacoes(resultado, operacoes, mes, getDiaAbertura(mes));

						Dia diaFechamento;
						if (stop != null) diaFechamento = getDiaAbertura(mes).getProximoDia();
						else diaFechamento = getDiaFechamento(mes);
						while (!stop(resultado, mes)) {
							executaOperacoesReversas(resultado, operacoes, mes, diaFechamento);
							if (diaFechamento.equals(getDiaFechamento(mes)))
								break;
							diaFechamento = diaFechamento.getProximoDia();
						}

					} catch (CotacaoInexistenteEX e) {
						resultado.remove(mes);
					}
				}
			}
		}
		limpaCaches();
		return resultado;
	}
	
	private void executaOperacoes(Resultado resultado, List<Operacao> operacoes, Mes mes, Dia dia) throws SATAEX {
		for (Operacao operacao : operacoes) {
			executaOperacao(resultado, operacao, mes, dia, false);
		}
	}
	
	private void executaOperacoesReversas(Resultado resultado, List<Operacao> operacoes, Mes mes, Dia dia) throws SATAEX {
		for (Operacao operacao : operacoes) {
			if (operacao.isReversivel() && operacao.isExecutada()) {
				if (resultado.possui(operacao, mes)) {
					Dia diaReversao = dia;
					Mes mesReversao = mes;
					int mesesParaVencimento = operacao.getMesesParaVencimento();
					for (int i=1; i<operacao.getMesesParaReversao(); i++) {
						mesReversao = dia.getMes().getMesPosterior();
						diaReversao = getDiaFechamento(mesReversao);
						operacao.setMesesParaVencimento(mesesParaVencimento-1);
					}
					
					if (operacao.getDiasParaFechamento() > 1) {
						diaReversao = diaReversao.getDiaAnterior(operacao.getDiasParaFechamento());
					}
					
					executaOperacao(resultado, operacao.getReversa(), mesReversao, diaReversao, true);
					operacao.setMesesParaVencimento(mesesParaVencimento);
				}
			}
		}
	}
	
	private void executaOperacao(Resultado resultado, Operacao operacao, Mes mes, Dia dia, boolean reversa) throws SATAEX {
		Preco preco = operacao.getPreco(dia);
		boolean condicaoVerdadeira = operacao.condicaoVerdadeira(preco);
		if (reversa) condicaoVerdadeira = true;
		if (condicaoVerdadeira) {
			resultado.setResultadoMensal(operacao, mes, preco);
			operacao.setExecutada(true);
		}
	}
	
	private boolean stop(Resultado resultado, Mes mes) {
		if (stop == null) return false;
		return stop.stop(mes, resultado);
	}
	
	public static Dia getDiaAbertura(Mes mes) {
		Mes mesCotacao = mes.getMesAnterior();
		return new Dia(SATAUtil.getDiaMes(mesCotacao.getAno(), mesCotacao.getMes(), Calendar.MONDAY, 3), mesCotacao);
	}
	
	public static Dia getDiaFechamento(Mes mes) {
		return new Dia(SATAUtil.getDiaMes(mes.getAno(), mes.getMes(), Calendar.MONDAY, 3)-3, mes);
	}
	
	private boolean possuiCotacaoNoAno(int ano) {
		try {
			String codigoAcao = operacoes.get(0).getAtivo().getAcao().getNome();
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			return caDAO.possuiCotacaoNoAno(codigoAcao, ano);
		} catch (Exception e) {
			return false;
		}
	}
	
	private void limpaCaches() {
		for (Operacao operacao: operacoes) {
			operacao.getAtivo().limpaPrecos();
		}
	}
	
	public List<Operacao> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
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
	public Stop getStop() {
		return stop;
	}
	public void setStop(Stop stop) {
		this.stop = stop;
	}
	public TipoCalculoValorInvestido getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}
	public void setTipoCalculoValorInvestido(
			TipoCalculoValorInvestido tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}
	public int getPercValorInvestido() {
		return percValorInvestido;
	}
	public void setPercValorInvestido(int percValorInvestido) {
		this.percValorInvestido = percValorInvestido;
	}
}
