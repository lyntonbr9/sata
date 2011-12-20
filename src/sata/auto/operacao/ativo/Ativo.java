package sata.auto.operacao.ativo;

import static sata.auto.operacao.Operacao.PRIMEIRO_DIA;
import static sata.auto.operacao.Operacao.ULTIMO_DIA;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sata.auto.operacao.Operacao;
import sata.auto.to.DataTO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;

public abstract class Ativo {
	
//	private static Map<Integer,List<CotacaoAtivoTO>> cache = new HashMap<Integer, List<CotacaoAtivoTO>>();
	private static Map<DataTO,List<CotacaoAtivoTO>> cache = new HashMap<DataTO, List<CotacaoAtivoTO>>();
	
	public abstract BigDecimal getValor(DataTO data, Operacao operacao);
	
	BigDecimal getPrecoAcao(Acao acao, DataTO data, int dia) {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao(acao, data, dia);
		if (cotacaoAtivo != null) {
			return new BigDecimal(Double.parseDouble(cotacaoAtivo.getFechamento())/100);
		}
		return BigDecimal.ZERO;
	}
	
	BigDecimal getVolatilidade(Acao acao, DataTO data, int dia) {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao(acao, data, dia);
		if (cotacaoAtivo != null) {
			return new BigDecimal(cotacaoAtivo.getVolatilidadeAnual());
		}
		return BigDecimal.ZERO;
	}

	private CotacaoAtivoTO getCotacaoAcao(Acao acao, DataTO data, int dia) {
		List<CotacaoAtivoTO> cotacoesAtivo = getListaCotacoesAcao(acao, data);
		if (!cotacoesAtivo.isEmpty()) {
			int i = 0;
			if (dia == ULTIMO_DIA) i = cotacoesAtivo.size()-1;
			if (dia == ULTIMO_DIA) System.out.println("data " + data + " último dia = " + i);
			if (dia == PRIMEIRO_DIA) System.out.println("data " + data + " primeiro dia = " + i);
			return cotacoesAtivo.get(i);
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
