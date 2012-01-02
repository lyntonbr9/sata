package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.Dia;
import sata.auto.to.Mes;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoAcao extends Preco implements IConstants {
	
	private static Map<Mes,List<CotacaoAtivoTO>> cache = new HashMap<Mes, List<CotacaoAtivoTO>>();
	
	Acao acao;
	
	public PrecoAcao() {}
	
	public PrecoAcao (Acao acao, Dia dia) {
		this.acao = acao;
		this.dia = dia;
	}
	
	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao();
		valor = new BigDecimal(Double.parseDouble(cotacaoAtivo.getFechamento())/100);
		volatilidade = new BigDecimal(cotacaoAtivo.getVolatilidadeAnual());
	}

	/*private CotacaoAtivoTO getCotacaoAcao() throws CotacaoInexistenteEX {
		int diaCotacao = -1;
		Mes mesCotacao = mes;
		if (momento == ABERTURA) {
			mesCotacao = mes.getMesAnterior();
			diaCotacao = getDiaAbertura(mesCotacao);
		}
		if (momento == FECHAMENTO) {
			diaCotacao = getDiaFechamento(mesCotacao);
		}
		CotacaoAtivoTO cotacao;
		int tentativas = 0;
		do {
			cotacao = getCotacaoAtivoNoDiaEspecifico(acao, mesCotacao, diaCotacao++);
		} while (cotacao == null && tentativas++<20);
		if (cotacao == null)
			throw new CotacaoInexistenteEX();
		return cotacao;
	}
	
	private CotacaoAtivoTO getCotacaoAtivoNoDiaEspecifico(Acao acao, Mes mes, int dia) {
		for (CotacaoAtivoTO cotacao : getListaCotacoesAcao(acao, mes)) {
			if(mes.getDataFormatada(dia,"dd/MM/yyyy").equals(cotacao.getPeriodo())){
				return cotacao;
			}
		}
		return null;
	}
	
	private List<CotacaoAtivoTO> getListaCotacoesAcao(Acao acao, Mes mes) {
		if (!cache.containsKey(mes)) {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			cache.put(mes,  caDAO.getCotacoesDoAtivo(acao.getNome(), mes.getDiaInicial(), mes.getDiaFinal()));
		}
		return cache.get(mes);
	}*/
	
	private CotacaoAtivoTO getCotacaoAcao() throws CotacaoInexistenteEX {
		CotacaoAtivoTO cotacao;
		int tentativas = 0;
		do {
			cotacao = getCotacaoAtivo(acao, dia);
			if (cotacao == null)
				dia = dia.getProximoDia();
		} while (cotacao == null && tentativas++<20);
		if (cotacao == null)
			throw new CotacaoInexistenteEX();
		return cotacao;
	}
	
	private CotacaoAtivoTO getCotacaoAtivo(Acao acao, Dia dia) {
		for (CotacaoAtivoTO cotacao : getListaCotacoesAcao(acao, dia.getMes())) {
			if(dia.formatoPadrao().equals(cotacao.getPeriodo())){
				return cotacao;
			}
		}
		return null;
	}
	
	private List<CotacaoAtivoTO> getListaCotacoesAcao(Acao acao, Mes mes) {
		if (!cache.containsKey(mes)) {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			cache.put(mes,  caDAO.getCotacoesDoAtivo(acao.getNome(), mes.getDiaInicial().formatoBanco(), mes.getDiaFinal().formatoBanco()));
		}
		return cache.get(mes);
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
