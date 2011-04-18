package sata.domain.dao.arquivo;

import sata.domain.dao.IAtivoDAO;
import sata.domain.to.AtivoTO;

public class ArquivoAtivoDAO implements IAtivoDAO{
	
	public AtivoTO getAtivo(String codigo){
		return new AtivoTO(codigo);
	}

}
