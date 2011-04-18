package sata.domain.dao;

import sata.domain.to.AtivoTO;

public interface IAtivoDAO {
	
	public AtivoTO getAtivo(String codigo);

}
