package sata.domain.dao.arquivo;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;

public class ArquivoCotacaoAtivoDAO implements ICotacaoAtivoDAO {

	public CotacaoAtivoTO getCotacaoAtivo(String codigo){
		return new CotacaoAtivoTO(codigo);
	}
}
