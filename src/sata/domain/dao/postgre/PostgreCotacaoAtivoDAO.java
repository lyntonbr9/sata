package sata.domain.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.experimental.categories.Categories;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;
import sata.domain.util.SATAUtil;

public class PostgreCotacaoAtivoDAO implements ICotacaoAtivoDAO, IConstants {

	private Connection con;
	
	public PostgreCotacaoAtivoDAO(Connection postgreConnection){
		this.con = postgreConnection;
	}

	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = '" + codigoAtivo + "' "
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}
	
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String ano) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = '" + codigoAtivo + "' "
			+ " AND ano = '" + ano + "' " 
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}
	
	@Override
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String dataInicial, 
													String dataFinal) {
		
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		
		// TODO Auto-generated method stub
		String tabela = "CotacaoAtivo"; //tabela das acoes
		if (codigoAtivo.length() != 5)
			tabela = "CotacaoOpcao"; //tabela das opções
		
		String sqlStmt = "SELECT * FROM \"" + tabela + "\"" 
			+ " WHERE \"codigoAtivo\" = ? AND periodo BETWEEN ? AND ? ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setTimestamp(2, SATAUtil.getTimeStampPeriodoCotacao(dataInicial));
			ps.setTimestamp(3, SATAUtil.getTimeStampPeriodoCotacao(dataFinal));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}

	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO) {
		
		String tabela = "CotacaoAtivo"; //tabela das acoes
		if (caTO.getCodigo().length() != 5)
			tabela = "CotacaoOpcao"; //tabela das opções
		
		String sqlStmt = "INSERT INTO \"" + tabela + "\"" 
			+ "(\"codigoAtivo\", periodo, tipoperiodo, abertura, maxima, minima, fechamento, ano, volume) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1,caTO.getCodigo());
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(caTO.getPeriodo()));
			ps.setString(3,caTO.getTipoPeriodo());
			ps.setString(4,caTO.getAbertura());
			ps.setString(5,caTO.getMaxima());
			ps.setString(6,caTO.getMinima());
			ps.setString(7,caTO.getFechamento());
			ps.setString(8,caTO.getAno());
			ps.setString(9, caTO.getVolume());
			ps.executeUpdate();
			
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean existeCotacao(String codigoAtivo, String periodo){
		
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? AND periodo = ? ";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(periodo));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //se existe
			{
				PostgreDAOFactory.returnConnection(con);
				return true;
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public String getDataUltimoCadastro(String codigoAtivo) {
		
		String dataUltimoCadastro = "";
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? ORDER BY periodo DESC";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //pega o primeiro registro
			{
				dataUltimoCadastro = SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"), false);
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataUltimoCadastro;
	}
	
	public static void main(String[] args) {
		
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
//		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", SATAUtil.getDataFormatadaParaBD("21/06/2011"), SATAUtil.getDataFormatadaParaBD("18/07/2011"));
		
		String[] datasInicioSerie = {"03/01/2011","18/01/2011","18/02/2011","22/03/2011","19/04/2011","17/05/2011","21/06/2011","19/07/2011"};
		String[] datasFimSerie = {"17/01/2011","17/02/2011","21/03/2011","18/04/2011","16/05/2011","20/06/2011","18/07/2011","15/08/2011"};
//		String dataInicioSerie = "";
//		String dataFimSerie = "";
		
		for (int i = 0; i < 7; i++)
		{
			List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", SATAUtil.getDataFormatadaParaBD(datasInicioSerie[i]), SATAUtil.getDataFormatadaParaBD(datasFimSerie[i]));

			for(CotacaoAtivoTO cotacaoAcaoTO : listaCotacoesAcao){
				System.out.println(cotacaoAcaoTO.getCodigo() + ": " + Double.parseDouble(cotacaoAcaoTO.getFechamento())/100 + " dia: " + cotacaoAcaoTO.getPeriodo());
				analisaMelhorOpcaoCompra(cotacaoAcaoTO, i, datasInicioSerie[i], datasFimSerie[i]);
			}
		}
	}
	public static String analisaMelhorOpcaoCompra(CotacaoAtivoTO cotacaoAcaoTO, int indiceSerieOpcao, String dataInicial, String dataFinal){

		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		Properties SATAProps = SATAPropertyLoader.loadProperties(IConstants.ARQ_SATA_CONF);
		int valorMaximoOpcaoPUT = Integer.parseInt((String)SATAProps.get(PROP_VALORMAX_OPCAO_PUT));
		int valorMaximoOpcaoCALL = Integer.parseInt((String)SATAProps.get(PROP_VALORMAX_OPCAO_CALL));
		int qtdDiasFaltamParaVencimentoPUT = Integer.parseInt((String)SATAProps.get(PROP_QTD_DIAS_FALTAM_VENCIMENTO_PUT));
		int qtdDiasFaltamParaVencimentoCALL = Integer.parseInt((String)SATAProps.get(PROP_QTD_DIAS_FALTAM_VENCIMENTO_CALL));

		
		int valorAcaoArredondado = Integer.parseInt(cotacaoAcaoTO.getFechamento())/100;
		System.out.println("valor acao arredondado: " + valorAcaoArredondado);
		
		System.out.println("ANALISANDO OPCOES DE VENDA PUT");
		//procura nas puts OTMs da acao
		for(int i = valorAcaoArredondado; i >= valorAcaoArredondado - 3; i--)
		{
			String nomeOpcao = "PETR" + SERIES_PUT[indiceSerieOpcao] + i;
			System.out.println("nomeOpcao: " + nomeOpcao);
			//pega todas as cotacoes da opcao de todos os dias
			List<CotacaoAtivoTO> listaTodasCotacoesOpcaoPUT = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(dataInicial), SATAUtil.getDataFormatadaParaBD(dataFinal));
			if(listaTodasCotacoesOpcaoPUT.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			//pega a cotacao da opcao no dia
			List<CotacaoAtivoTO> listaCotacaoOpcaoCorrente = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()), SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()));
			if(listaCotacaoOpcaoCorrente.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			CotacaoAtivoTO cotacaoOpcaoCorrente = listaCotacaoOpcaoCorrente.get(0);
			int j=0;
			for(CotacaoAtivoTO caTO : listaTodasCotacoesOpcaoPUT){
				j++;
				if(cotacaoOpcaoCorrente.getPeriodo().equalsIgnoreCase(caTO.getPeriodo())){ //Porque existem dias que a opcao nao teve cotacao(negociacao)
//					System.out.println(caTO.getCodigo());
					System.out.println(caTO.getPeriodo());
//					System.out.println(caTO.getAno());
//					System.out.println("A: " + caTO.getAbertura());
//					System.out.println("Max: " + caTO.getMaxima());
//					System.out.println("Min: " + caTO.getMinima());
					System.out.println("F: " + caTO.getFechamento());
					int valorOpcao = Integer.parseInt(caTO.getFechamento());
//					System.out.println("valorOpcao: " + valorOpcao);
					System.out.println("diaDaOpcao: " + j);
					if(valorOpcao <= valorMaximoOpcaoPUT && j < listaTodasCotacoesOpcaoPUT.size() - qtdDiasFaltamParaVencimentoPUT)
					{
						System.out.println("[PUT] Melhor opcao put: " + caTO.getCodigo() + " data compra: " + caTO.getPeriodo());
						System.out.println("[PUT] PRECO DE COMPRA: " + listaTodasCotacoesOpcaoPUT.get(j-1).getFechamento());
						System.out.println("[PUT] PRECO DE VENDA 75%: " + listaTodasCotacoesOpcaoPUT.get(j).getFechamento());
						System.out.println("[PUT] PRECO DE VENDA 25%: " + listaTodasCotacoesOpcaoPUT.get(j+1).getFechamento()); 
						//ver o resto das cotacoes
						for(int k=j+2; k<listaTodasCotacoesOpcaoPUT.size(); k++)
							System.out.println("[PUT] diaDaOpcao: " + k + " valor: " + listaTodasCotacoesOpcaoPUT.get(k).getFechamento());

						break;
					}					
				}
			}
			System.out.println("###################");
		}
		
		System.out.println("ANALISANDO OPCOES DE COMPRA CALL");
		//procura nas calls OTMs da acao
		for(int i = valorAcaoArredondado; i <= valorAcaoArredondado + 3; i++)
		{
			String nomeOpcao = "PETR" + SERIES_CALL[indiceSerieOpcao] + i;
			System.out.println("nomeOpcao: " + nomeOpcao);
			//pega todas as cotacoes da opcao de todos os dias
			List<CotacaoAtivoTO> listaTodasCotacoesOpcaoCall = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(dataInicial), SATAUtil.getDataFormatadaParaBD(dataFinal));
			if(listaTodasCotacoesOpcaoCall.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			//pega a cotacao da opcao no dia
			List<CotacaoAtivoTO> listaCotacaoOpcaoCorrente = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()), SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()));
			if(listaCotacaoOpcaoCorrente.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			CotacaoAtivoTO cotacaoOpcaoCorrente = listaCotacaoOpcaoCorrente.get(0);
			int j=0;
			for(CotacaoAtivoTO caTO : listaTodasCotacoesOpcaoCall){
				j++;
				if(cotacaoOpcaoCorrente.getPeriodo().equalsIgnoreCase(caTO.getPeriodo())){ //Porque existem dias que a opcao nao teve cotacao(negociacao)
					System.out.println(caTO.getPeriodo());
					System.out.println("F: " + caTO.getFechamento());
					int valorOpcao = Integer.parseInt(caTO.getFechamento());
					System.out.println("diaDaOpcao: " + j);
					System.out.println("qtd cotacoes opcao: " + listaTodasCotacoesOpcaoCall.size());
					if(valorOpcao <= valorMaximoOpcaoCALL && j < listaTodasCotacoesOpcaoCall.size() - qtdDiasFaltamParaVencimentoCALL)
					{
						System.out.println("[CALL] Melhor opcao call: " + caTO.getCodigo() + " data compra: " + caTO.getPeriodo());
						System.out.println("[CALL] PRECO DE COMPRA: " + listaTodasCotacoesOpcaoCall.get(j-1).getFechamento());
						System.out.println("[CALL] PRECO DE VENDA 75%: " + listaTodasCotacoesOpcaoCall.get(j).getFechamento());
						System.out.println("[CALL] PRECO DE VENDA 25%: " + listaTodasCotacoesOpcaoCall.get(j+1).getFechamento()); 
						//ver o resto das cotacoes
						for(int k=j+2; k<listaTodasCotacoesOpcaoCall.size(); k++)
							System.out.println("[CALL] diaDaOpcao: " + k + " valor: " + listaTodasCotacoesOpcaoCall.get(k).getFechamento());
						break;
					}					
				}
			}
			System.out.println("###################");
		}
		
		return null;
	}
}

