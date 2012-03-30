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

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.Posicao;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.Put;
import sata.auto.operacao.ativo.RendaFixa;
import sata.domain.util.FacesUtil;
import sata.domain.util.IConstants;

@ManagedBean
@SessionScoped
public class OperacaoMB implements IConstants {
	
	private final char ACAO = 'A';
	private final char CALL = 'C';
	private final char PUT = 'P';
	private final char NENHUM = '-';
	private final char RENDA_FIXA = 'R';
	private boolean alterar = false;
	
	private List<Operacao> operacoes = new ArrayList<Operacao>();
	private Operacao operacao;
	private Posicao posicao;
	private char tipoAtivo;
	private int qtdLotes = 1;
	private int ordemOpcao = 0;
	private int mesesParaVencimento = 1;
	private int diasParaFechamento = 1;
	private Condicao condicao = new Condicao();
	private StreamedContent arquivo;
	
	public void incluirOperacao() {
		try {
			if (formularioValido()) {
				if (alterar) operacoes.remove(operacao);
				operacao = converteOperacao(posicao);
				operacao.setAtivo(converteAtivo(tipoAtivo));
				operacao.setCondicao(trataCondicao());
				operacao.setQtdLotes(qtdLotes);
				operacao.setMesesParaVencimento(mesesParaVencimento);
				operacao.setDiasParaFechamento(diasParaFechamento);
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
			posicao = converteOperacao(operacao);
			tipoAtivo = converteAtivo(operacao.getAtivo());
			qtdLotes = operacao.getQtdLotes();
			if (isOpcaoSelecionada())
				ordemOpcao = ((Opcao)operacao.getAtivo()).getOrdem();
			mesesParaVencimento = operacao.getMesesParaVencimento();
			diasParaFechamento = operacao.getDiasParaFechamento();
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
	
	public void abrirArquivo(FileUploadEvent event) {
		try {
			InputStream in = event.getFile().getInputstream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			operacoes.clear();
			String linha;
			while ((linha = br.readLine()) != null) {
				int i=0;
				String[] colunas = linha.split(";");
				posicao = Posicao.get(colunas[i++].charAt(0));
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
	
	public void limpaCampos() {
		posicao = null;
		tipoAtivo = NENHUM;
		qtdLotes = 1;
		ordemOpcao = 1;
		mesesParaVencimento = 1;
		condicao = new Condicao();
		alterar = false;
		arquivo = null;
	}
	
	private boolean formularioValido() {
		boolean valido = true;
		if (tipoAtivo == NENHUM) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_OPERACOES_LABEL_ATIVO);
			valido = false;
		}
		if (posicao == null && !isRendaFixaSelecionada()) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_OPERACOES_LABEL_POSICAO);
			valido = false;
		}
		if (qtdLotes <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_OPERACOES_LABEL_QTD);
			valido = false;
		}
		if (mesesParaVencimento <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_OPERACOES_LABEL_MESES);
			valido = false;
		}
		return valido;
	}
	
	private Condicao trataCondicao() {
		if (condicao == null || condicao.getAtributo() == null || condicao.getOperacao() == null)
			return null;
		
		return condicao;
	}
	
	private void setReversivel(Operacao operacao) {
		if (operacao.getAtivo() instanceof RendaFixa)
			operacao.setReversivel(false);
	}
	
	private Operacao converteOperacao(Posicao posicao) {
		if (isRendaFixaSelecionada())
			return new Venda();
		
		switch (posicao) {
		case VENDIDO:  return new Venda();
		case COMPRADO: return new Compra();
		}
		return null;
	}
	
	private Posicao converteOperacao(Operacao operacao) {
		if (operacao instanceof Venda)
			return Posicao.VENDIDO;
		else if (operacao instanceof Compra)
			return Posicao.COMPRADO;
		else return null;
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
	
	public List<SelectItem> getPosicoes() {
		return Posicao.getSelectItems();
	}
	
	public List<SelectItem> getAtributos() {
		return Atributo.getSelectItems();
	}
	
	public List<SelectItem> getOperadores() {
		return Operador.getSelectItems();
	}
	
	public StreamedContent getArquivo() {
		if (arquivo == null)
			arquivo = geraArquivo();
		return arquivo;
	}
	
	public String getTextoOperacao() {
		if (alterar) return MSG_GENERAL_LABEL_ALTERAR;
		else return MSG_GENERAL_LABEL_INCLUIR;
	}
	
	public boolean isRendaFixaSelecionada() {
		return tipoAtivo == RENDA_FIXA;
	}
	
	public boolean isOpcaoSelecionada() {
		return tipoAtivo == CALL || tipoAtivo == PUT;
	}

	public List<Operacao> getOperacoes() {
		return operacoes;
	}

	public void setOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public char getTipoAtivo() {
		return tipoAtivo;
	}

	public void setTipoAtivo(char tipoAtivo) {
		this.tipoAtivo = tipoAtivo;
	}

	public int getQtdLotes() {
		return qtdLotes;
	}

	public void setQtdLotes(int qtdLotes) {
		this.qtdLotes = qtdLotes;
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

	public int getDiasParaFechamento() {
		return diasParaFechamento;
	}

	public void setDiasParaFechamento(int diasParaFechamento) {
		this.diasParaFechamento = diasParaFechamento;
	}

	public Condicao getCondicao() {
		return condicao;
	}

	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}

	public void setArquivo(StreamedContent arquivo) {
		this.arquivo = arquivo;
	}
}
