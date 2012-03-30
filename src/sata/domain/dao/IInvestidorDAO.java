package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.InvestidorTO;

public interface IInvestidorDAO {

	public List<InvestidorTO> listaInvestidores() throws SQLException;
}
