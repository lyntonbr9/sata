package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public interface ISimulacao {

	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes);
}
