package sata.domain.util;

public interface IConstants {

	public int CANDLE_VERDE = 4;
	public int CANDLE_VERMELHA = 3;
	public String NOVA_LINHA = System.getProperty("line.separator");
	
	public String mesesYahooFinances[] = {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
	
	public String[] SERIES_CALL = {"A","B","C","D","E","F","G","H","I","J","K","L"};
	public String[] SERIES_PUT = {"M","N","O","P","Q","R","S","T","U","V","W","X"};
	
	public String ARQ_SATA_CONF = "sata-conf";
	
	public String PROP_SATA_DB_JDBC_DRIVER = "sata.db.jdbc.driver";
	public String PROP_SATA_DB_URL="sata.db.url";
	public String PROP_SATA_DB_USERNAME="sata.db.username";
	public String PROP_SATA_DB_PASSWORD="sata.db.password";
	public String PROP_SATA_DB_MAXPOOLSIZE="sata.db.maxpoolsize";
	
	public String PROP_PCTGEM_VARIACAOALTAPOUCOTEMPO = "pctgem.variacaoAltaPoucoTempo";
	public String PROP_CANDLE_PCTGEMPAVIOGRANDE = "candlestick.pctgem.paviogrande";
	
	public String PROP_VALORMAX_OPCAO_PUT = "opcao.put.valormaximo";
	public String PROP_QTD_DIAS_FALTAM_VENCIMENTO_PUT = "opcao.put.qtdDiasFaltamParaVencimento";
	public String PROP_VALORMAX_OPCAO_CALL = "opcao.call.valormaximo";
	public String PROP_QTD_DIAS_FALTAM_VENCIMENTO_CALL = "opcao.call.qtdDiasFaltamParaVencimento";
	
	public int QTD_DIAS_ANO = 365;
	public int QTD_DIAS_FALTA_1_MES_VENC = 29; //quando for saber 1 mes
	public int QTD_DIAS_FALTA_2_MES_VENC = 59; //quando for saber 2 meses
	public double TAXA_DE_JUROS = 0.1150; //11,5%
	public double PCTGEM_OPCAO = 0.05;
	public int QTD_DIAS_UTEIS_ANO = 252;
	public int QTD_DIAS_UTEIS_MES=21;
	
	public double SPREAD = 2.5;
}

