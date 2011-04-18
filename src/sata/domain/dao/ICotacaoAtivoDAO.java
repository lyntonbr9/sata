package sata.domain.dao;

import sata.domain.to.CotacaoAtivoTO;

public interface ICotacaoAtivoDAO {

	public CotacaoAtivoTO getCotacaoAtivo(String codigo);
	
}
