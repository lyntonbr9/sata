package sata.auto.gui.web.mbean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;
import sata.auto.to.ValorResultado;
import sata.domain.util.FacesUtil;

@ManagedBean
@SessionScoped
public class SimulacaoMB {
	
	private static final char NENHUM = '-';
	private static final char COMPRA = 'C';
	private static final char VENDA = 'V';
	private static final char ACAO = 'A';
	private static final char CALL = 'C';
	private static final char PUT = 'P';
	
	private List<String> acoes = Arrays.asList("PETR4", "VALE5", "OGXP3", "BVMF3");
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
	
	public void incluirOperacao() {
		try {
			if (formularioValido()) {
				if (alterar) operacoes.remove(operacao);
				operacao = converteOperacao(tipoOperacao);
				operacao.setAtivo(converteAtivo(tipoAtivo));
				operacao.setCondicao(trataCondicao());
				operacao.setQtdLotes(qtdLotes);
				operacao.setMesesParaVencimento(mesesParaVencimento);
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
		tipoOperacao = ' ';
		tipoAtivo = ' ';
		qtdLotes = 1;
		mesesParaVencimento = 1;
		condicao = new Condicao();
		alterar = false;
		arquivo = null;
	}
	
	public void simular() {
		try {
			Simulacao simulacao = new Simulacao();
			simulacao.setOperacoes(operacoes);
			simulacao.setAnoInicial(anoInicial);
			simulacao.setAnoFinal(anoFinal);
			simulacao.setTipoCalculoValorInvestido(TipoCalculoValorInvestido.TOTAL_COMPRADO);
			resultado = simulacao.getResultado();
			relatorio = formataTexto(resultado.imprime(tipoRelatorio));
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
			List<ValorResultado> resultados = resultado.listaResultadoComReinvestimento();
			novaSerie.setLabel(nome);
			for (int i=0; i<resultados.size(); i+=escalaGrafico) {
				ValorResultado valorResultado = resultados.get(i);
				novaSerie.set(valorResultado.getValor(), valorResultado.getResultado());
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
			String linha;
			while ((linha = br.readLine()) != null) {
				int i=0;
				String[] colunas = linha.split(";");
				tipoOperacao = colunas[i++].charAt(0);
				qtdLotes = Integer.valueOf(colunas[i++]);
				tipoAtivo = colunas[i++].charAt(0);
				acao = colunas[i++];
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
		if (tipoOperacao == NENHUM) {
			FacesUtil.addError("Selecione uma operação");
			valido = false;
		}
		if (qtdLotes <= 0) {
			FacesUtil.addError("A quantidade deve ser maior que zero");
			valido = false;
		}
		if (tipoAtivo == NENHUM) {
			FacesUtil.addError("Selecione um ativo");
			valido = false;
		}
		if (mesesParaVencimento <= 0) {
			FacesUtil.addError("O número de meses para vencimento deve ser maior que zero");
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
	
	private Operacao converteOperacao(char tipoOperacao) {
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
		else return NENHUM;
	}
	
	private Ativo converteAtivo(char tipoAtivo) {
		switch (tipoAtivo) {
		case ACAO: return new Acao(acao);
		case CALL: return new Call(new Acao(acao), ordemOpcao);
		case PUT: return new Put(new Acao(acao), ordemOpcao);
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
				if (operacao.getAtivo() instanceof Acao) {
					buff.write(((Acao)operacao.getAtivo()).getNome()+";");
					buff.write(";");
				}
				else {
					buff.write(((Opcao)operacao.getAtivo()).getAcao().getNome()+";");
					buff.write(((Opcao)operacao.getAtivo()).getOrdem()+";");
				}
				buff.write(operacao.getMesesParaVencimento()+";");
				if (operacao.getCondicao() != null) {
					buff.write(operacao.getCondicao().getAtributo()+";");
					buff.write(operacao.getCondicao().getOperacao()+";");
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
	
	public List<SelectItem> getTiposRelatorios() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(TipoRelatorio.ANUAL,"Anual"));
		items.add(new SelectItem(TipoRelatorio.MENSAL,"Mensal"));
		items.add(new SelectItem(TipoRelatorio.COMPLETO,"Completo"));
		items.add(new SelectItem(TipoRelatorio.REINVESTIMENTO,"Reinvestimento"));
		return items;
	}
	
	public List<SelectItem> getAtributos() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(Atributo.PRECO,"Preço"));
		items.add(new SelectItem(Atributo.VOLATILIDADE,"Volatilidade"));
		items.add(new SelectItem(Atributo.MEDIA_MOVEL,"Média Móvel"));
		return items;
	}
	
	public List<SelectItem> getOperadores() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(Operador.IGUAL,"="));
		items.add(new SelectItem(Operador.DIFERENTE,"<>"));
		items.add(new SelectItem(Operador.MAIOR,">"));
		items.add(new SelectItem(Operador.MENOR,"<"));
		items.add(new SelectItem(Operador.MAIOR_IGUAL,">="));
		items.add(new SelectItem(Operador.MENOR_IGUAL,"<="));
		return items;
	}
	
	public String getTextoOperacao() {
		if (alterar) return "Alterar";
		else return "Adicionar";
	}
	
	public boolean isOpcaoSelecionada() {
		return tipoAtivo == 'C' || tipoAtivo == 'P';
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

	public List<String> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<String> acoes) {
		this.acoes = acoes;
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
}
