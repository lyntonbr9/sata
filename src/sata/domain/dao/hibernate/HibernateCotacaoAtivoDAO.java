package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.SplitAtivoTO;

public class HibernateCotacaoAtivoDAO extends GenericDAOHibernate<CotacaoAtivoTO> implements ICotacaoAtivoDAO {
	
	@Override
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo) throws SQLException {
		return setSplit(super.listar("where codigo = '" + codigoAtivo + "' order by periodo"));
	}
	@Override
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, Integer ano) throws SQLException {
		return setSplit(super.listar("where codigo = '" + codigoAtivo + "' and ano = '" + ano + "' order by periodo"));
	}
	@Override
	public CotacaoAtivoTO getCotacaoDoAtivo(String codigoAtivo, String data) throws SQLException {
		try {
			return setSplit(super.listar("where codigo = '" + codigoAtivo + "' and periodo like '%" + data + "%'")).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		} 
	}
	@Override
	public boolean possuiCotacaoNoAno(String codigoAtivo, Integer ano) throws SQLException {
		return !getCotacoesDoAtivo(codigoAtivo, ano).isEmpty();
	}
	@Override
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String dataInicial, String dataFinal) throws SQLException {
		return setSplit(super.listar("where codigo = '" + codigoAtivo + " and periodo BETWEEN '" + dataInicial + "' and '" + dataFinal + "' order by periodo"));
	}
	@Override
	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO) throws SQLException {
		super.incluir(caTO);
	}
	@Override
	public boolean existeCotacao(String codigoAtivo, String periodo) throws SQLException {
		return getCotacaoDoAtivo(codigoAtivo, periodo) != null;
	}
	@Override
	public String getDataUltimoCadastro(String codigoAtivo) {
		return null; //TODO implementar
	}
	@Override
	public int updateCotacaoDoAtivo(CotacaoAtivoTO caTO) throws SQLException {
		super.alterar(caTO);
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private List<CotacaoAtivoTO> setSplit(List<CotacaoAtivoTO> list) throws SQLException {
		for (CotacaoAtivoTO cotacao: list) {
			String query = "from SplitAtivoTO where codigoAtivo = '" + cotacao.getCodigo()
				+ "' and periodo >= '" + cotacao.getPeriodo() + "'";
			List<SplitAtivoTO> splits = (List<SplitAtivoTO>) super.executeQuery(query);
			int split = 1;
			if (!splits.isEmpty()) {
				for (SplitAtivoTO splitAtivo: splits) {
					split *= splitAtivo.getSplit();
				}
			}
			cotacao.setSplit(split);
		}
		return list;
	}

	public HibernateCotacaoAtivoDAO() {
		super(CotacaoAtivoTO.class);
	}

	// Implementação do singleton
	private static HibernateCotacaoAtivoDAO instance;
	public static HibernateCotacaoAtivoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateCotacaoAtivoDAO create() {
		if (instance == null) instance = new HibernateCotacaoAtivoDAO();
		return instance;
	}
}
