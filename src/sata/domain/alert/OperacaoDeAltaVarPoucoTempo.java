package sata.domain.alert;

import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.simulacao.SimulacaoAcaoAltaVarPoucoTempo;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.SATAUtil;
import sata.metastock.data.ValuesMeta;
import sata.metastock.mail.SendMailUsingAuthentication;

public class OperacaoDeAltaVarPoucoTempo {
	
	public static void alerta(){
		
		/* verifica se vai enviar o alerta por e-mail */
		SimulacaoAcaoAltaVarPoucoTempo s = new SimulacaoAcaoAltaVarPoucoTempo();
		List<String> listaDeAcoesParaAlerta = new ArrayList<String>();
		
		IAtivoDAO ativoDAO = SATAFactoryFacade.getAtivoDAO();
		List<String> listaDeAcoes = ativoDAO.getCodigosAtivos();
		List<CotacaoAtivoTO> listaCotacoesAtivoTO;
		for (String acao : listaDeAcoes) {
//			if(acao.equalsIgnoreCase("LAME4")){
				ValuesMeta values = new ValuesMeta(acao,"");
				listaCotacoesAtivoTO = values.getValores();
				s.getResultado(listaCotacoesAtivoTO, null);
				ArrayList<Integer> indicesIndicados = s.getIndicesIndicadosSimulacao();
				if (SATAUtil.indicePertence(indicesIndicados, listaCotacoesAtivoTO.size() - 3)){
					listaDeAcoesParaAlerta.add(acao);
				}				
//			}
		}
		//envia o e-mail caso tenha acoes em alerta
		if (listaDeAcoesParaAlerta.size() > 0){
			SendMailUsingAuthentication.sendEmailOperAltaVarPoucoTempo(listaDeAcoesParaAlerta);
		}
	}
	
}
