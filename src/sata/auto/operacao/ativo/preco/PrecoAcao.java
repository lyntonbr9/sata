package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.Dia;
import sata.auto.to.Periodo;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoAcao extends Preco implements IConstants {
	
	private static Map<Integer,List<CotacaoAtivoTO>> cacheCotacoes = new HashMap<Integer, List<CotacaoAtivoTO>>();
	private static Map<Dia,BigDecimal> cacheMM = new HashMap<Dia,BigDecimal>();
	
	Acao acao;
	
	public PrecoAcao() {}
	
	public PrecoAcao (Acao acao, Dia dia) {
		this.acao = acao;
		this.dia = dia;
	}
	
	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao(dia);
		valor = cotacaoAtivo.getValorFechamento();
		volatilidade = cotacaoAtivo.getValorVolatilidadeAnual();
	}
	
	@Override
	public BigDecimal calculaMediaMovel(Integer periodo) {
		return calculaMediaMovel(dia, periodo, acao);
	}
	
	private CotacaoAtivoTO getCotacaoAcao(Dia dia) throws CotacaoInexistenteEX {
		CotacaoAtivoTO cotacao;
		int tentativas = 0;
		do {
			cotacao = getCotacaoAtivo(dia);
			if (cotacao == null)
				dia = dia.getProximoDia();
		} while (cotacao == null && tentativas++<20);
		if (cotacao == null)
			throw new CotacaoInexistenteEX();
		return cotacao;
	}
	
	private CotacaoAtivoTO getCotacaoAtivo(Dia dia) {
		for (CotacaoAtivoTO cotacao : getListaCotacoesAcao(dia.getAno())) {
			if(dia.formatoPadrao().equals(cotacao.getPeriodo())){
				return cotacao;
			}
		}
		return null;
	}
	
	private List<CotacaoAtivoTO> getListaCotacoesAcao(Integer ano) {
		return getListaCotacoesAcao(acao, ano);
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcao(Acao acao, Integer ano) {
		if (!cacheCotacoes.containsKey(ano)) {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			cacheCotacoes.put(ano, caDAO.getCotacoesDoAtivo(acao.getNome(), ano.toString()));
		}
		return cacheCotacoes.get(ano);
	}
	
	private static BigDecimal calculaMediaMovel(Dia dia, int periodo, Acao acao) {
		if (periodo == 0) return BigDecimal.ZERO;
		if (!cacheMM.containsKey(dia)) {
				List<CotacaoAtivoTO> cotacoes = getListaCotacoesAteAData(dia, periodo, acao);
				BigDecimal mm = BigDecimal.ZERO;
				if (cotacoes.size() == periodo) {
					BigDecimal soma = BigDecimal.ZERO;
					for (CotacaoAtivoTO cotacao: cotacoes) {
						soma = soma.add(cotacao.getValorFechamento());
					}
					mm = soma.divide(new BigDecimal(periodo), RoundingMode.HALF_EVEN);
				}
				cacheMM.put(dia, mm);
		}
		return cacheMM.get(dia);
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAteAData (Dia diaFinal, int qtdDias, Acao acao) {
		List<CotacaoAtivoTO> cotacoes = getListaCotacoesAcaoPeriodo(acao, diaFinal.getDiaAnterior(qtdDias*2), diaFinal);
		while(cotacoes.size() > qtdDias) {
			cotacoes.remove(0);
		}
		return cotacoes;
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcaoPeriodo(Acao acao, Dia diaInicial, Dia diaFinal) {
		return getListaCotacoesAcaoPeriodo(acao, new Periodo(diaInicial, diaFinal));
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcaoPeriodo(Acao acao, Periodo periodo) {
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		return caDAO.getCotacoesDoAtivo(acao.getNome(), periodo.getDiaInicial().formatoBanco(), periodo.getDiaFinal().formatoBanco());
	}
	
	@Override
	public String toString() {
		return acao + " " + dia + " = " + SATAUtil.formataNumero(valor)
		+ "; Volatilidade = " + SATAUtil.formataNumero(volatilidade.multiply(new BigDecimal(100))) + "%";
	}

	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
