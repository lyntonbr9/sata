package sata.domain.util;

public interface IConstants {

	public int CANDLE_VERDE = 4;
	public int CANDLE_VERMELHA = 3;
	public String NOVA_LINHA = System.getProperty("line.separator");
	
	public String mesesYahooFinances[] = {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
	
	public String ARQ_SATA_CONF = "sata-conf";
	
	public String PROP_SATA_DB_JDBC_DRIVER = "sata.db.jdbc.driver";
	public String PROP_SATA_DB_URL="sata.db.url";
	public String PROP_SATA_DB_USERNAME="sata.db.username";
	public String PROP_SATA_DB_PASSWORD="sata.db.password";
	public String PROP_SATA_DB_MAXPOOLSIZE="sata.db.maxpoolsize";
	
	public String PROP_PCTGEM_VARIACAOALTAPOUCOTEMPO = "pctgem.variacaoAltaPoucoTempo";
	public String PROP_CANDLE_PCTGEMPAVIOGRANDE = "candlestick.pctgem.paviogrande";
	
}

