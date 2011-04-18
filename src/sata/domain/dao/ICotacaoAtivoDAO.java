package sata.domain.dao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;

public interface ICotacaoAtivoDAO {

	public CotacaoAtivoTO getCotacaoAtivo(String codigo);
	public List<CotacaoAtivoTO> getCotacoesDosAtivos();
	
}
