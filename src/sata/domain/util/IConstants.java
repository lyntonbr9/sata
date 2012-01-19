package sata.domain.util;

import java.math.BigDecimal;
import java.util.Locale;

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
	
	double SPREAD = 2.5;
	char COMPRADO = 'C';
	char VENDIDO = 'V';
	int ABERTURA = 0;
	int FECHAMENTO = 1;
	
	Locale LOCALE_BRASIL = new Locale("pt", "BR");
	Locale LOCALE_EUA = new Locale("en");
	Locale LOCALE_DEFAULT = LOCALE_BRASIL;
	
	BigDecimal CEM = new BigDecimal(100);
	
	String MSG_BUNDLE = "msg/messages";
	
	String MSG_LABEL_ACAO = "simulacao.formSimulacao.acao.label";
	String MSG_LABEL_OPERACAO = "simulacao.formOperacao.operacao.label";
	String MSG_LABEL_QTD = "simulacao.formOperacao.qtd.label";
	String MSG_LABEL_ORDEM = "simulacao.formOperacao.ordem.label";
	String MSG_LABEL_ATIVO = "simulacao.formOperacao.ativo.label";
	String MSG_LABEL_MESES = "simulacao.formOperacao.meses.label";
	String MSG_LABEL_MEDIA_ANUAL = "simulacao.resultado.mediaAnual";
	String MSG_LABEL_MEDIA_MENSAL = "simulacao.resultado.mediaMensal";
	String MSG_LABEL_VALOR_INICIAL = "simulacao.resultado.valorInicial";
	String MSG_LABEL_VALOR_FINAL = "simulacao.resultado.valorFinal";
	String MSG_LABEL_COMPRA = "list.operacao.compra";
	String MSG_LABEL_VENDA = "list.operacao.venda";
	
	String MSG_PATTERN_OPERACAO = "toString.operacao.pattern";
	String MSG_PATTERN_OPERACAO_MESES = "toString.operacao.pattern.meses";
	String MSG_PATTERN_PRECO_ACAO = "toString.precoAcao.pattern";
	String MSG_PATTERN_PRECO_OPCAO = "toString.precoOpcao.pattern";
	String MSG_PATTERN_PRECO_RENDA_FIXA = "toString.precoRendaFixa.pattern";
	
	String MSG_ENUM_PREFIX_ATRIBUTO = "enum.atributo.";
	String MSG_ENUM_PREFIX_TIPO_RELATORIO = "enum.tipoRelatorio.";
	String MSG_ENUM_PREFIX_TIPO_CALCULO_VALOR_INVESTIDO = "enum.tipoCalculoValorInvestido.";
	
	String MSG_ERRO_CAMPO_OBRIGATORIO = "general.msg.error.campoObrigatorio";
	String MSG_ERRO_VALOR_MAIOR_QUE_ZERO = "general.msg.error.maiorQueZero";
}

