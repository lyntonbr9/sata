package sata.domain.data;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.SATAUtil;
import sata.metastock.robos.CotacaoLopesFilho;

public class DataManagement {

	public static void atualizarBD() {
		System.out.println("Vai atualizar o banco de dados...");
		
		// pega a data atual
		Calendar calendario = SATAUtil.getDataAtual();
		
		String dataAtual = (calendario.get(Calendar.DAY_OF_MONTH)) - 2 + "/" 
				+ (calendario.get(Calendar.MONTH) + 1) + "/"
				+ calendario.get(Calendar.YEAR);
		
		System.out.println("Dia: " + calendario.get(Calendar.DAY_OF_MONTH));
		System.out.println("Mes: " + calendario.get(Calendar.MONTH));
		System.out.println("Ano: " + calendario.get(Calendar.YEAR));

		// para todas as acoes
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
		ICotacaoAtivoDAO caDAO = daoFactory.getCotacaoAtivoDAO();
		List<String> listaDeAcoes = ativoDAO.getCodigosAtivos();
		for (String acao : listaDeAcoes) {
			if (acao.equalsIgnoreCase("PETR4")) {
				//pega o ultimo dia cadastrado da acao
				String dataUltimoCadastro = "09/05/2011";
				// atualiza as cotacoes da acao
				List<CotacaoAtivoTO> listaCotacoesAtivo = SATAUtil.getCotacoesFromYahooFinances(acao, dataUltimoCadastro, dataAtual);
				for(CotacaoAtivoTO caTO : listaCotacoesAtivo){
					System.out.println(caTO.getAbertura());
					System.out.println(caTO.getMaxima());
					System.out.println(caTO.getMinima());
					System.out.println(caTO.getFechamento());
				}
					//caDAO.insertCotacaoDoAtivo(caTO);
			}

		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		atualizarBD();
	}

}
