package sata.auto.gui.web.mbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Derivado;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;
import sata.auto.to.ValorResultado;
import sata.auto.web.util.FacesUtil;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

@ManagedBean
@SessionScoped
public class SimulacaoMB implements IConstants {
	
	private Acao acao;
	private int anoInicial = 2011;
	private int anoFinal = 2011;
	private TipoCalculoValorInvestido tipoCalculoValorInvestido = TipoCalculoValorInvestido.CUSTO_MONTAGEM;
	private int percValorInvestido = 100;
	private Resultado resultado;
	private String relatorio;
	private TipoRelatorio tipoRelatorio = TipoRelatorio.ANUAL;
	private String nome;
	private int escalaGrafico = 1;
	private CartesianChartModel graficoModel = new CartesianChartModel();
	private UploadedFile uploadedFile;
	private double taxaDeJuros = SATAUtil.getTaxaDeJuros()*100;
	private int diasParaVencimento = 31;
	
	public void simular() {
		try {
			if (acao == null) {
				FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_GENERAL_LABEL_ACAO);
			}
			else {
				OperacaoMB operacaoMB = FacesUtil.getMB(OperacaoMB.class);
				nome = "";
				Simulacao simulacao = new Simulacao();
				setAcao(operacaoMB.getOperacoes(), acao);
				simulacao.setOperacoes(operacaoMB.getOperacoes());
				simulacao.setAnoInicial(anoInicial);
				simulacao.setAnoFinal(anoFinal);
				simulacao.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
				simulacao.setPercValorInvestido(percValorInvestido);
				simulacao.setDiasParaVencimento(diasParaVencimento);
				SATAUtil.setTaxaDeJuros(taxaDeJuros/100);
				if (resultado != null) resultado.limpa();
				resultado = simulacao.getResultado();
				relatorio = FacesUtil.formataTexto(resultado.imprime(tipoRelatorio));
			}
		}
		catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void alterarRelatorio() {
		try {
			relatorio = FacesUtil.formataTexto(resultado.imprime(tipoRelatorio));
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
	
	private void setAcao(List<Operacao> operacoes, Acao acao) {
		for (Operacao operacao : operacoes) {
			operacao.getAtivo().limpaPrecos();
			if (operacao.getAtivo() instanceof Acao)
				operacao.setAtivo(acao);
			else if (operacao.getAtivo() instanceof Derivado) {
				((Derivado)operacao.getAtivo()).setAcao(acao);
				((Derivado)operacao.getAtivo()).setAcao(acao);
			}
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
	
	public List<SelectItem> getTiposCalculoValorInvestido() {
		return TipoCalculoValorInvestido.getSelectItems();
	}
	
	public List<Acao> getAcoes() {
		return AcaoConteiner.getAcoes();
	}

	
	public boolean isShowPercValorInvestido() {
		return tipoCalculoValorInvestido == TipoCalculoValorInvestido.PRECO_ACAO;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
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

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
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

	public int getPercValorInvestido() {
		return percValorInvestido;
	}

	public void setPercValorInvestido(int percValorInvestido) {
		this.percValorInvestido = percValorInvestido;
	}

	public int getDiasParaVencimento() {
		return diasParaVencimento;
	}

	public void setDiasParaVencimento(int diasParaVencimento) {
		this.diasParaVencimento = diasParaVencimento;
	}
}
