package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AcompanhamentoTO;

public interface IAcompOpcoesDAO {

	public List<AcompanhamentoTO> listar() throws SQLException;
	public void salvar(AcompanhamentoTO opcao) throws SQLException;
	public AcompanhamentoTO recuperar(Integer id) throws SQLException;
}
