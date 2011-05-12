package sata.domain.dao.arquivo;

import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.DAOFactory;

@Deprecated
public class ArquivoDAOFactory extends DAOFactory {

	public IAtivoDAO getAtivoDAO(){
		//return new ArquivoAtivoDAO();
		return null;
	} 
	
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		//return new ArquivoCotacaoAtivoDAO();
		return null;
	}
}
