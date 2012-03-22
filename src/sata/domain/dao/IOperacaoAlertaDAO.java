package sata.domain.dao;

import java.util.List;

import sata.domain.to.OperacaoRealizadaTO;

public interface IOperacaoAlertaDAO {

	public List<OperacaoRealizadaTO> getOperacoesParaAcompanhar();
	
}
