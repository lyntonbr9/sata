package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AlertaTO;

public interface IAlertaDAO {

	public List<AlertaTO> getAlertasAtivos() throws SQLException;
	
}
