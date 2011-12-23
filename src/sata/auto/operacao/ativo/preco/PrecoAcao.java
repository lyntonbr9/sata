package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.DataTO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoAcao extends Preco implements IConstants {
	
	private static Map<DataTO,List<CotacaoAtivoTO>> cache = new HashMap<DataTO, List<CotacaoAtivoTO>>();
	
	Acao acao;
	DataTO data;
	int momento;
	BigDecimal volatilidade;
	String periodo;
	
	public PrecoAcao() {}
	
	public PrecoAcao (Acao acao, DataTO data, int momento) {
		this.acao = acao;
		this.data = data;
		this.momento = momento;
	}
	
	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao();
		valor = new BigDecimal(Double.parseDouble(cotacaoAtivo.getFechamento())/100);
		volatilidade = new BigDecimal(cotacaoAtivo.getVolatilidadeAnual());
		periodo = getCotacaoAcao().getPeriodo();
	}

	private CotacaoAtivoTO getCotacaoAcao() throws CotacaoInexistenteEX {
		int diaCotacao = -1;
		DataTO dataCotacao = data;
		if (momento == ABERTURA) {
			dataCotacao = data.getMesAnterior();
			diaCotacao = SATAUtil.getDiaMes(dataCotacao.getAno(), dataCotacao.getMes(), Calendar.MONDAY, 3);
		}
		if (momento == FECHAMENTO) {
			diaCotacao = SATAUtil.getDiaMes(dataCotacao.getAno(), dataCotacao.getMes(), Calendar.MONDAY, 3)-3;
		}
		CotacaoAtivoTO cotacao;
		int tentativas = 0;
		do {
			cotacao = getCotacaoAtivoNoDiaEspecifico(acao, dataCotacao, diaCotacao++);
		} while (cotacao == null && tentativas++<20);
		if (cotacao == null)
			throw new CotacaoInexistenteEX();
		return cotacao;
	}
	
	private CotacaoAtivoTO getCotacaoAtivoNoDiaEspecifico(Acao acao, DataTO data, int dia) {
		for (CotacaoAtivoTO cotacao : getListaCotacoesAcao(acao, data)) {
			if(data.getDataFormatada(dia,"dd/MM/yyyy").equals(cotacao.getPeriodo())){
				return cotacao;
			}
		}
		return null;
	}
	
	private List<CotacaoAtivoTO> getListaCotacoesAcao(Acao acao, DataTO data) {
		if (!cache.containsKey(data)) {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			cache.put(data,  caDAO.getCotacoesDoAtivo(acao.getNome(), data.getDiaInicial(), data.getDiaFinal()));
		}
		return cache.get(data);
	}

	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public DataTO getData() {
		return data;
	}
	public void setData(DataTO data) {
		this.data = data;
	}
	public int getMomento() {
		return momento;
	}
	public void setMomento(int momento) {
		this.momento = momento;
	}
	public void setVolatilidade(BigDecimal volatilidade) {
		this.volatilidade = volatilidade;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public BigDecimal getVolatilidade() {
		return volatilidade;
	}
	public String getPeriodo() {
		return periodo;
	}

	@Override
	public String toString() {
		return acao + " " + periodo + " = " + SATAUtil.formataNumero(valor)
		+ "; Volatilidade = " + SATAUtil.formataNumero(volatilidade.multiply(new BigDecimal(100))) + "%";

	}
}
