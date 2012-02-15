package sata.auto.gui.web.mbean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Derivado;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.Put;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;
import sata.auto.to.ValorResultado;
import sata.domain.util.FacesUtil;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

@ManagedBean
@SessionScoped
public class SimulacaoMB implements IConstants {
	
	private static final char NENHUM = '-';
	private static final char COMPRA = 'C';
	private static final char VENDA = 'V';
	private static final char ACAO = 'A';
	private static final char CALL = 'C';
	private static final char PUT = 'P';
	private static final char RENDA_FIXA = 'R';
	private boolean alterar = false;
	
	List<Operacao> operacoes = new ArrayList<Operacao>();
	String acao;
	char tipoOperacao;
	int qtdLotes = 1;
	char tipoAtivo;
	int ordemOpcao;
	int mesesParaVencimento = 1;
	int anoInicial = 2000;
	int anoFinal = 2011;
	TipoCalculoValorInvestido tipoCalculoValorInvestido = TipoCalculoValorInvestido.CUSTO_MONTAGEM;
	Resultado resultado;
	String relatorio;
	Operacao operacao;
	TipoRelatorio tipoRelatorio = TipoRelatorio.ANUAL;
	Condicao condicao = new Condicao();
	String nome;
	int escalaGrafico = 1;
	CartesianChartModel graficoModel = new CartesianChartModel();
	StreamedContent arquivo;
	UploadedFile uploadedFile;
	double taxaDeJuros = SATAUtil.getTaxaDeJuros()*100;
	
	public void incluirOperacao() {
		try {
			if (formularioValido()) {
				if (alterar) operacoes.remove(operacao);
				operacao = converteOperacao(tipoOperacao);
				operacao.setAtivo(converteAtivo(tipoAtivo));
				operacao.setCondicao(trataCondicao());
				operacao.setQtdLotes(qtdLotes);
				operacao.setMesesParaVencimento(mesesParaVencimento);
				setReversivel(operacao);
				operacoes.add(operacao);
				limpaCampos();
			}
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void editarOperacao() {
		try {
			tipoOperacao = converteOperacao(operacao);
			tipoAtivo = converteAtivo(operacao.getAtivo());
			qtdLotes = operacao.getQtdLotes();
			if (isOpcaoSelecionada())
				ordemOpcao = ((Opcao)operacao.getAtivo()).getOrdem();
			mesesParaVencimento = operacao.getMesesParaVencimento();
			alterar = true;
			if (operacao.getCondicao() != null)
				condicao = operacao.getCondicao();
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void excluirOperacao() {
		try {
			operacoes.remove(operacao);
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void limpaCampos() {
		tipoOperacao = NENHUM;
		tipoAtivo = NENHUM;
		qtdLotes = 1;
		ordemOpcao = 1;
		mesesParaVencimento = 1;
		condicao = new Condicao();
		alterar = false;
		arquivo = null;
	}
	
	public void simular() {
		try {
			if (acao == "") {
				FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_LABEL_ACAO);
			}
			else {
				nome = "";
				Simulacao simulacao = new Simulacao();
				setAcao(operacoes, acao);
				simulacao.setOperacoes(operacoes);
				simulacao.setAnoInicial(anoInicial);
				simulacao.setAnoFinal(anoFinal);
				simulacao.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
				SATAUtil.setTaxaDeJuros(taxaDeJuros/100);
				resultado = simulacao.getResultado();
				relatorio = formataTexto(resultado.imprime(tipoRelatorio));
			}
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void alterarRelatorio() {
		try {
			relatorio = formataTexto(resultado.imprime(tipoRelatorio));
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void addGrafico() {
		try {
			ChartSeries novaSerie = new ChartSeries();
			List<ValorResultado> resultados = getResultados();
			novaSerie.setLabel(nome);
			for (int i=0; i<resultados.size(); i+=escalaGrafico) {
				ValorResultado valorResultado = resultados.get(i);
				if (!valorResultado.isEmpty())
					novaSerie.set(valorResultado.getLabel(), valorResultado.getValores().get(0));
			}
			graficoModel.addSeries(novaSerie);
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void limparGrafico() {
		try {
			graficoModel.clear();
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void abrirArquivo(FileUploadEvent event) {
		try {
			InputStream in = event.getFile().getInputstream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			operacoes.clear();
			String linha;
			while ((linha = br.readLine()) != null) {
				int i=0;
				String[] colunas = linha.split(";");
				tipoOperacao = colunas[i++].charAt(0);
				qtdLotes = Integer.valueOf(colunas[i++]);
				tipoAtivo = colunas[i++].charAt(0);
				if (tipoAtivo == CALL || tipoAtivo == PUT)
					ordemOpcao = Integer.valueOf(colunas[i++]);
				else i++;
				mesesParaVencimento = Integer.valueOf(colunas[i++]);
				Atributo atributo = Atributo.get(colunas[i++]);
				Operador operador = Operador.get(colunas[i++]);
				if (atributo != null && operador != null)
					condicao = new Condicao(atributo,operador,Double.valueOf(colunas[i++]));
				else condicao = null;
				incluirOperacao();
			}
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	private boolean formularioValido() {
		boolean valido = true;
		if (tipoAtivo == NENHUM) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_LABEL_ATIVO);
			valido = false;
		}
		if (tipoOperacao == NENHUM && !isRendaFixaSelecionada()) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_LABEL_OPERACAO);
			valido = false;
		}
		if (qtdLotes <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_LABEL_QTD);
			valido = false;
		}
		if (mesesParaVencimento <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_LABEL_MESES);
			valido = false;
		}
		return valido;
	}
	
	private Condicao trataCondicao() {
		if (condicao == null || condicao.getAtributo() == null || condicao.getOperacao() == null)
			return null;
		
		return condicao;
	}
	
	private String formataTexto(String texto) {
		texto = texto.replace("\n", "<br/>");
		return texto;
	}
	
	private void setReversivel(Operacao operacao) {
		if (operacao.getAtivo() instanceof RendaFixa)
			operacao.setReversivel(false);
	}
	
	private void setAcao(List<Operacao> operacoes, String nomeAcao) {
		for (Operacao operacao : operacoes) {
			operacao.getAtivo().limpaPrecos();
			if (operacao.getAtivo() instanceof Acao)
				operacao.setAtivo(AcaoConteiner.get(nomeAcao));
			else if (operacao.getAtivo() instanceof Derivado) {
				((Derivado)operacao.getAtivo()).setAcao(AcaoConteiner.get(nomeAcao));
				((Derivado)operacao.getAtivo()).setAcao(AcaoConteiner.get(nomeAcao));
			}
		}
	}
	
	private Operacao converteOperacao(char tipoOperacao) {
		if (isRendaFixaSelecionada())
			return new Venda();
		
		switch (tipoOperacao) {
		case VENDA:	return new Venda();
		case COMPRA: return new Compra();
		}
		return null;
	}
	
	private char converteOperacao(Operacao operacao) {
		if (operacao instanceof Venda)
			return VENDA;
		else if (operacao instanceof Compra)
			return COMPRA;
		else return NENHUM;
	}
	
	private char converteAtivo(Ativo ativo) {
		if (ativo instanceof Acao)
			return ACAO;
		else if (ativo instanceof Call)
			return CALL;
		else if (ativo instanceof Put)
			return PUT;
		else if (ativo instanceof RendaFixa)
			return RENDA_FIXA;
		else return NENHUM;
	}
	
	private Ativo converteAtivo(char tipoAtivo) {
		switch (tipoAtivo) {
		case ACAO: return new Acao();
		case CALL: return new Call(new Acao(), ordemOpcao);
		case PUT: return new Put(new Acao(), ordemOpcao);
		case RENDA_FIXA: return new RendaFixa(new Acao());
		}
		return null;
	}
	
	private StreamedContent geraArquivo()  {
		try {
			File file = new File("C:/windows/temp/estrategia.txt");
			file.delete();
			FileWriter x = new FileWriter(file, true);
			BufferedWriter buff = new BufferedWriter(x);

			for (Operacao operacao: operacoes) {
				buff.write(converteOperacao(operacao)+";");
				buff.write(operacao.getQtdLotes()+";");
				buff.write(converteAtivo(operacao.getAtivo())+";");
				if (operacao.getAtivo() instanceof Opcao) {
					buff.write(((Opcao)operacao.getAtivo()).getOrdem()+";");
				}
				else buff.write(" ;");
				buff.write(operacao.getMesesParaVencimento()+";");
				if (operacao.getCondicao() != null) {
					buff.write(operacao.getCondicao().getAtributo().getName()+";");
					buff.write(operacao.getCondicao().getOperacao().getSimbol()+";");
					buff.write(operacao.getCondicao().getValor()+";");
				}
				else buff.write(" ; ; ;");
				buff.newLine();
			}
			buff.flush();
			buff.close();
			
			InputStream stream = new FileInputStream("C:\\windows\\temp\\estrategia.txt");     
	        StreamedContent sc = new DefaultStreamedContent(stream, "application/txt", "estrategia.txt");
	        return sc;
		}
		catch (Exception e) {
			FacesUtil.addException(e);
			return null;
		}
	}
	
	private List<ValorResultado> getResultados() {
		switch (tipoRelatorio) {
		case ANUAL: return resultado.listaResultadosAnuais();
		case COMPLETO:
		case MENSAL: return resultado.listaResultadosMensais();
		case REINVESTIMENTO: return resultado.listaResultadoComReinvestimento();
		}
		return null;
	}
	
	public List<SelectItem> getTiposRelatorios() {
		return TipoRelatorio.getSelectItems();
	}
	
	public List<SelectItem> getAtributos() {
		return Atributo.getSelectItems();
	}
	
	public List<SelectItem> getOperadores() {
		return Operador.getSelectItems();
	}
	
	public List<SelectItem> getTiposCalculoValorInvestido() {
		return TipoCalculoValorInvestido.getSelectItems();
	}
	
	public List<String> getAcoes() {
		return AcaoConteiner.getAcoes();
	}

	
	public String getTextoOperacao() {
		if (alterar) return "general.label.update";
		else return "general.label.add";
	}
	
	public boolean isOpcaoSelecionada() {
		return tipoAtivo == CALL || tipoAtivo == PUT;
	}
	
	public boolean isRendaFixaSelecionada() {
		return tipoAtivo == RENDA_FIXA;
	}
	
	public StreamedContent getArquivo() {
		if (arquivo == null)
			arquivo = geraArquivo();
		return arquivo;
	}
	
	public List<Operacao> getOperacoes() {
		return operacoes;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public void setOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
	}

	public char getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(char tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public int getQtdLotes() {
		return qtdLotes;
	}

	public void setQtdLotes(int qtdLotes) {
		this.qtdLotes = qtdLotes;
	}

	public char getTipoAtivo() {
		return tipoAtivo;
	}

	public void setTipoAtivo(char tipoAtivo) {
		this.tipoAtivo = tipoAtivo;
	}

	public int getOrdemOpcao() {
		return ordemOpcao;
	}

	public void setOrdemOpcao(int ordemOpcao) {
		this.ordemOpcao = ordemOpcao;
	}

	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}

	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public String getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(String relatorio) {
		this.relatorio = relatorio;
	}

	public int getAnoInicial() {
		return anoInicial;
	}

	public void setAnoInicial(int anoInicial) {
		this.anoInicial = anoInicial;
	}

	public int getAnoFinal() {
		return anoFinal;
	}

	public void setAnoFinal(int anoFinal) {
		this.anoFinal = anoFinal;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Condicao getCondicao() {
		return condicao;
	}

	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getEscalaGrafico() {
		return escalaGrafico;
	}

	public void setEscalaGrafico(int escalaGrafico) {
		this.escalaGrafico = escalaGrafico;
	}

	public CartesianChartModel getGraficoModel() {
		return graficoModel;
	}

	public void setGraficoModel(CartesianChartModel graficoModel) {
		this.graficoModel = graficoModel;
	}

	public void setArquivo(StreamedContent arquivo) {
		this.arquivo = arquivo;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public TipoCalculoValorInvestido getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}

	public void setTipoCalculoValorInvestido(
			TipoCalculoValorInvestido tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}

	public double getTaxaDeJuros() {
		return taxaDeJuros;
	}

	public void setTaxaDeJuros(double taxaDeJuros) {
		this.taxaDeJuros = taxaDeJuros;
	}
}
