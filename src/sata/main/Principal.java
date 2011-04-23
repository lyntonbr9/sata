package sata.main;

import java.util.Iterator;
import java.util.List;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.simulacao.ISimulacao;
import sata.domain.simulacao.SimulacaoAcaoOperacaoDeAlta;
import sata.domain.simulacao.SimulacaoAcaoOperacaoDeBaixa;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.DataManagement;

public class Principal {

	
	public static void simulaAcao(String codigoAcao, String ano){
		System.out.println("testando Simulacao " + codigoAcao + " ano de " + ano);
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		ICotacaoAtivoDAO cotacaoAtivoDAO = daoFactory.getCotacaoAtivoDAO();
		List<CotacaoAtivoTO> listaCotacoesAtivo = cotacaoAtivoDAO.getCotacoesDoAtivo(codigoAcao, ano);
		//ISimulacao simulacao = new SimulacaoAcaoOperacaoAlta();
		ISimulacao simulacao = new SimulacaoAcaoOperacaoDeBaixa();
		ResultadoSimulacaoTO res = simulacao.getResultado(listaCotacoesAtivo, 30, 10, 0.5);
		System.out.println("ACAO: " + codigoAcao);
		System.out.println("TOTAL OPERACOES: " + res.getQtdTotalOperacoes());
		System.out.println("OPERACOES RISCO: " + res.getQtdOperacoesRiscoStop());
		System.out.println("OPERACOES GANHO: " + res.getQtdOperacoesSucesso());
		System.out.println("OPERACOES PERDA: " + res.getQtdOperacoesFalha());
		System.out.println("VALOR TOTAL: " + res.getValorTotal());
		System.out.println("VALOR GANHO: " + res.getValorGanho());
		System.out.println("VALOR PERDA: " + res.getValorPerda());
		System.out.println("==================================================");
		System.out.println("");
	}
	
	public static void insereAcaoDB(String codigoAcao, String ano){
		System.out.println("inserindo acao " + codigoAcao + " ano de " + ano);
		DataManagement dm = new DataManagement();
		String pathArqListaDeCotacoesDaAcao = ano + "\\saida_" + codigoAcao + "_" + ano + ".txt";
		dm.setArquivoListaCotacaoDeAtivos(pathArqListaDeCotacoesDaAcao);
		dm.importarArqCotacaoToDB(codigoAcao, ano);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String ano="2010";
		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
//		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String codigoAcao = i.next();
//			if (codigoAcao.equalsIgnoreCase("PETR4"))
//				simulaAcao(codigoAcao, ano);
//		}
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
		while(i.hasNext()){
			String codigoAcao = i.next();
			//if (codigoAcao.equalsIgnoreCase("PETR4"))
				//insereAcaoDB(codigoAcao, ano);
				simulaAcao(codigoAcao, ano);
		}
		
//		String ano="2010";
//		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoAtivoDAO ativoDAO = (ArquivoAtivoDAO) daoFactory.getAtivoDAO();
//		ativoDAO.setArquivoListaDeAtivos("listaDeAtivos_" + ano + ".txt");
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String codigoAcao = i.next();
//			simulaAcao(codigoAcao, ano);
//		}
		
		// TODO Auto-generated method stub
//		System.out.println("testando ArquivoDAO");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoAtivoDAO ativoDAO = (ArquivoAtivoDAO) daoFactory.getAtivoDAO();
//		ativoDAO.setArquivoListaDeAtivos("listaDeAtivos_2008.txt");
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String ativo = i.next();
//			System.out.println("ATIVO: " + ativo);
//		}
		
//		System.out.println("testando CotacaoAtivoDAO");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoCotacaoAtivoDAO cotacaoAtivoDAO = (ArquivoCotacaoAtivoDAO) daoFactory.getCotacaoAtivoDAO();
//		cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_PETR4_2008.txt");
//		Iterator<CotacaoAtivoTO> i = cotacaoAtivoDAO.getCotacoesDosAtivos().iterator();
//		while(i.hasNext()){
//			CotacaoAtivoTO ativo = i.next();
//			System.out.println("ATIVO: " + ativo.getCodigo());
//			System.out.println("ABERTURA: " + ativo.getAbertura());
//			System.out.println("MAXIMA: " + ativo.getMaxima());
//			System.out.println("MINIMA: " + ativo.getMinima());
//			System.out.println("FECHAMENTO: " + ativo.getFechamento());
//		}
		
//		System.out.println("testando Simulacao");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoCotacaoAtivoDAO cotacaoAtivoDAO = (ArquivoCotacaoAtivoDAO) daoFactory.getCotacaoAtivoDAO();
//		//cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_PETR4_2009.txt");
//		String codigoAcao = "BBDC4";
//		String ano="2009";
//		
//		cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_BBDC4_2009.txt");
//		List<CotacaoAtivoTO> listaCotacoesAtivo = cotacaoAtivoDAO.getCotacoesDosAtivos();
//		ISimulacao simulacao = new SimulacaoAcaoOperacaoAlta();
//		//simulacao.setQtdTotalOperacoesRiscoStop(25);
//		ResultadoSimulacaoTO res = simulacao.getResultado(listaCotacoesAtivo, 30, 10, 0.5);
//		System.out.println("ACAO: " + codigoAcao);
//		System.out.println("TOTAL OPERACOES: " + res.getQtdTotalOperacoes());
//		System.out.println("OPERACOES RISCO: " + res.getQtdOperacoesRiscoStop());
//		System.out.println("OPERACOES GANHO: " + res.getQtdOperacoesSucesso());
//		System.out.println("OPERACOES PERDA: " + res.getQtdOperacoesFalha());
//		System.out.println("VALOR TOTAL: " + res.getValorTotal());
//		System.out.println("VALOR GANHO: " + res.getValorGanho());
//		System.out.println("VALOR PERDA: " + res.getValorPerda());
		
//		while(i.hasNext()){
//			CotacaoAtivoTO ativo = i.next();
//			System.out.println("ATIVO: " + ativo.getCodigo());
//			System.out.println("ABERTURA: " + ativo.getAbertura());
//			System.out.println("MAXIMA: " + ativo.getMaxima());
//			System.out.println("MINIMA: " + ativo.getMinima());
//			System.out.println("FECHAMENTO: " + ativo.getFechamento());
//		}
	}

}
