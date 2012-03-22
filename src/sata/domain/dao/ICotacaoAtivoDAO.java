package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.CotacaoAtivoTO;

public interface ICotacaoAtivoDAO {

	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo);
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String ano) throws SQLException;
	public boolean possuiCotacaoNoAno(String codigoAtivo, String ano) throws SQLException;
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String dataInicial, String dataFinal);
	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO);
	public boolean existeCotacao(String codigoAtivo, String periodo);
	public String getDataUltimoCadastro(String codigoAtivo);
	public int updateCotacaoDoAtivo(CotacaoAtivoTO caTO);
}
