package sata.domain.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.ICotacaoAtivoDAO;
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
	}
	
	public static void main(String[] args) {
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
