package sata.domain.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;

public class DataManagement {
	
	private FileInputStream fisArqListaCotacoesDoAtivo;
	
	public void setArquivoListaCotacaoDeAtivos(String pathArqListaDeCotacoesDoAtivo){
		try {
			fisArqListaCotacoesDoAtivo = new FileInputStream(pathArqListaDeCotacoesDoAtivo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/*
	public void importarArqCotacaoToDB(String codigoAtivo, String ano){
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		if (this.fisArqListaCotacoesDoAtivo != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacoesDoAtivo);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				String conteudoLinha = "";
				while((conteudoLinha = brEntrada.readLine()) != null){
					ICotacaoAtivoDAO cotacaoAtivoDAO = daoFactory.getCotacaoAtivoDAO();
					String[] cotacaoDoAtivo = conteudoLinha.split(" ");
					CotacaoAtivoTO caTO = new CotacaoAtivoTO();
					caTO.setCodigo(cotacaoDoAtivo[0]);
					caTO.setAbertura(cotacaoDoAtivo[1]);
					caTO.setMaxima(cotacaoDoAtivo[2]);
					caTO.setMinima(cotacaoDoAtivo[3]);
					caTO.setFechamento(cotacaoDoAtivo[4]);
					caTO.setPeriodo(cotacaoDoAtivo[5]);
					caTO.setTipoPeriodo("D"); //cotacao diaria
					caTO.setAno(ano);
					cotacaoAtivoDAO.insertCotacaoDoAtivo(caTO);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}*/

	public void importarArqCotacaoHistoricaBovespaToDB(String codigoAtivo, String ano){
		
		//tenta abrir o arquivo de cotacoes
		try {
			fisArqListaCotacoesDoAtivo = new FileInputStream("COTAHIST_A" + ano + ".TXT");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (this.fisArqListaCotacoesDoAtivo != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacoesDoAtivo);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				
				int tamStrNomeAcaoOpcao;
				int tamStrNomeParametro;
				
				Date agora = new Date();
				long t1 = agora.getTime();
				System.out.println("Processando...");

				if (codigoAtivo.length() == 5) {
					tamStrNomeAcaoOpcao = 5;
					tamStrNomeParametro = 5;
				} else {
					tamStrNomeAcaoOpcao = 7;
					tamStrNomeParametro = 4;
				}
				
				ICotacaoAtivoDAO cotacaoAtivoDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
				String conteudoLinha = "";
				String periodo="";
				while((conteudoLinha = brEntrada.readLine()) != null)
				{
					if (conteudoLinha.substring(0, 2).equals("01")) 
					{
						if (conteudoLinha.substring(12, 24).trim().length() == tamStrNomeAcaoOpcao
							&& conteudoLinha.substring(12, 24).trim().substring(0,tamStrNomeParametro).equalsIgnoreCase(codigoAtivo)) 
						{
							periodo = String.valueOf(Integer.valueOf(conteudoLinha.substring(2, 10).trim()));
							//verifica se a cotacao nao existe
							if(cotacaoAtivoDAO.existeCotacao(codigoAtivo, periodo) == false){
								CotacaoAtivoTO caTO = new CotacaoAtivoTO();
								caTO.setCodigo(conteudoLinha.substring(12, 24).trim());
								caTO.setAbertura(String.valueOf(Integer.valueOf(conteudoLinha.substring(56, 69).trim())));
								caTO.setMaxima(String.valueOf(Integer.valueOf(conteudoLinha.substring(69, 82).trim())));
								caTO.setMinima(String.valueOf(Integer.valueOf(conteudoLinha.substring(82, 95).trim())));
								caTO.setFechamento(String.valueOf(Integer.valueOf(conteudoLinha.substring(108, 121).trim())));
								caTO.setVolume(String.valueOf(Long.parseLong(conteudoLinha.substring(170,188).trim())));
								caTO.setPeriodo(periodo);
								caTO.setTipoPeriodo("D"); //cotacao diaria
								caTO.setAno(ano);
								cotacaoAtivoDAO.insertCotacaoDoAtivo(caTO);								
							}
						}
					}
				}
				
				agora = new Date();
				long tempoDuracao = agora.getTime() - t1;
				
				System.out.println("Tempo de processamento: " + tempoDuracao);
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		DataManagement dm = new DataManagement();
		String ano="2011";
		IAtivoDAO ativoDAO = SATAFactoryFacade.getAtivoDAO();
		List<String> listaCodigosOpcoesLiquidas = ativoDAO.getCodigosOpcoesLiquidas(ano);
		for(String nomeOpcao: listaCodigosOpcoesLiquidas)
			dm.importarArqCotacaoHistoricaBovespaToDB(nomeOpcao, ano);
//		DataManagement dm = new DataManagement();
//		String periodo = "20100104";
//		Timestamp ts = dm.getTimeStampPeriodoCotacao(periodo);
//		System.out.println(dm.getTimeStampFormatado(ts, true));
//		
		//String periodo = "2011-01-04 00:00:00.0";
		//String periodoFormatado = periodo.substring(0,4) + "-" + periodo.substring(4,6) + "-" + periodo.substring(6,8);
//		Calendar cal = new GregorianCalendar();
//		cal.set(2011, 1, 4);
//		System.out.println(cal.get(Calendar.DAY_OF_MONTH)+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
	
		//cal.getTime();
		//System.out.println(periodoFormatado + " 00:00:00");
		//Timestamp ts = Timestamp.valueOf(periodo);
//		Timestamp ts = new Timestamp(cal.getTime().getTime());
//		try {	
//			DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//			
//			System.out.println(df.parse(periodo).getDay()+ "-" + df.parse(periodo).getMonth());
//			//Timestamp ts = new Timestamp(df.parse(periodo).getTime());			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		//Timestamp ts = new DataManagement().getTimeStampPeriodoCotacao(periodo);
//		cal.setTime(ts);
//		System.out.println(cal.get(Calendar.DAY_OF_MONTH)+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
//		
//		System.out.println(ts.getDay()+ "-" + ts.getMonth() + "-" + ts.getYear());		
//		
	}

}
