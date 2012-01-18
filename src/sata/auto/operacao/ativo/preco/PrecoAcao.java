package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.AnoCotacao;
import sata.auto.to.Dia;
import sata.auto.to.DiaCotacao;
import sata.auto.to.Periodo;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoAcao extends Preco implements IConstants {
	
	private static Map<AnoCotacao,List<CotacaoAtivoTO>> cacheCotacoes = new HashMap<AnoCotacao, List<CotacaoAtivoTO>>();
	private static Map<DiaCotacao,BigDecimal> cacheMM = new HashMap<DiaCotacao,BigDecimal>();
	
	private Acao acao;
	
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
			if(dia.formatoBrasileiro().equals(cotacao.getPeriodo())){
				return cotacao;
			}
		}
		return null;
	}
	
	private List<CotacaoAtivoTO> getListaCotacoesAcao(Integer ano) {
		return getListaCotacoesAcao(new AnoCotacao(ano, acao));
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcao(AnoCotacao anoCotacao) {
		if (!cacheCotacoes.containsKey(anoCotacao)) {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			cacheCotacoes.put(anoCotacao, caDAO.getCotacoesDoAtivo(anoCotacao.getAcao().getNome(), anoCotacao.getAno().toString()));
		}
		return cacheCotacoes.get(anoCotacao);
	}
	
	private static BigDecimal calculaMediaMovel(Dia dia, int periodo, Acao acao) {
		if (periodo == 0) return BigDecimal.ZERO;
		DiaCotacao diaCotacao = new DiaCotacao(dia, acao);
		if (!cacheMM.containsKey(diaCotacao)) {
				List<CotacaoAtivoTO> cotacoes = getListaCotacoesAteAData(dia, periodo, acao);
				BigDecimal mm = BigDecimal.ZERO;
				if (cotacoes.size() == periodo) {
					BigDecimal soma = BigDecimal.ZERO;
					for (CotacaoAtivoTO cotacao: cotacoes) {
						soma = soma.add(cotacao.getValorFechamento());
					}
					mm = soma.divide(new BigDecimal(periodo), RoundingMode.HALF_EVEN);
				}
				cacheMM.put(diaCotacao, mm);
		}
		return cacheMM.get(diaCotacao);
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
		return SATAUtil.getMessage(MSG_PATTERN_PRECO_ACAO, 
				dia.toString(), 
				SATAUtil.formataNumero(valor), 
				SATAUtil.formataNumero(volatilidade.multiply(CEM)));
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(10,27).
		   appendSuper(8).
	       append(acao).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
