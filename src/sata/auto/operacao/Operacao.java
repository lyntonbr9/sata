package sata.auto.operacao;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.simulacao.Simulacao;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;

public abstract class Operacao implements IConstants {
	
	int qtdLotes = 1;
	int momento = ABERTURA;
	Ativo ativo;
	int mesesParaVencimento = 1;
	Condicao condicao;
	Operacao reversa;
	int mesesParaReversao = 1;
	
	public Operacao() {}
	
	public Operacao(Ativo ativo) {
		this.ativo = ativo;
	}
	
	public Operacao(Ativo ativo, int mesesParaVencimento) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
	}
	
	public Operacao(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
	}
	
	public Operacao(int qtdLotes, Ativo ativo) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, int mesesParaReversao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.mesesParaReversao = mesesParaReversao;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, Condicao condicao, int mesesParaReversao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
		this.mesesParaReversao = mesesParaReversao;
	}
	
	protected Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, int momento, Condicao condicao, Operacao reversa, int mesesParaReversao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
		this.condicao = condicao;
		this.reversa = reversa;
		this.mesesParaReversao = mesesParaReversao;
	}
	
	public abstract Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int mesesParaReversaoReverso);
	
	public Preco getPreco(Dia dia) throws CotacaoInexistenteEX {
		return ativo.getPreco(dia, this);
	}
	
	public boolean condicaoVerdadeira(Preco preco) {
		if (condicao == null) return true;
		return condicao.verdadeira(preco);
	}
	
	private BigDecimal precoExercicioOpcao;
	
	public BigDecimal getPrecoExercicioOpcao(Dia dia, Opcao opcao) throws CotacaoInexistenteEX {
		if (ativo instanceof Opcao) {
			if (momento == ABERTURA)
				return calculaPrecoExercicioOpcao(dia, opcao);

			else {
				precoExercicioOpcao = reversa.precoExercicioOpcao;
				return precoExercicioOpcao;
			}
		}
		return null;
	}
	
	private BigDecimal calculaPrecoExercicioOpcao(Dia dia, Opcao opcao) throws CotacaoInexistenteEX {
		BigDecimal spread = new BigDecimal(SPREAD/100);
		BigDecimal precoAcaoNoDiaDaOpcao = opcao.getAcao().getPreco(dia);
		BigDecimal volatilidade = opcao.getAcao().getVolatilidade(dia);
		BigDecimal multiplicador = new BigDecimal(opcao.getOrdem(volatilidade)).multiply(spread);
		precoExercicioOpcao = precoAcaoNoDiaDaOpcao.add(precoAcaoNoDiaDaOpcao.multiply(multiplicador));
		return precoExercicioOpcao;
	}
	
	public Operacao getReversa() {
		if (reversa == null) {
			int momentoReverso = FECHAMENTO;
			int mesesParaVencimentoReverso = mesesParaVencimento-1;
			int mesesParaReveresaoReverso = mesesParaVencimento-1;
			if (momento == FECHAMENTO) {
				momentoReverso = ABERTURA;
				mesesParaVencimentoReverso = mesesParaVencimento+1;
				mesesParaReveresaoReverso = mesesParaVencimento+1;
			}
			reversa = criaOperacaoReversa(mesesParaVencimentoReverso, momentoReverso, mesesParaReveresaoReverso);
		}
		return reversa;
	}
	
	public int getDiasParaVencimento(Dia dia) {
		Dia diaOperacao = dia;
		Dia diaFechamento = Simulacao.getDiaFechamento(dia.getMes());
		if (momento == ABERTURA) {
			for (int i=2; i<=mesesParaVencimento; i++) {
				diaOperacao = Simulacao.getDiaAbertura(diaOperacao.getMes().getMesAnterior());
			}
		}
		if (momento == FECHAMENTO) {
			for (int i=1; i<=mesesParaVencimento; i++) {
				diaFechamento = Simulacao.getDiaFechamento(dia.getMes().getMesAnterior());
			}
		}
		return getDiferenca(diaOperacao, diaFechamento);
	}
	
	private int getDiferenca(Dia dia1, Dia dia2) {
		long longDia = dia1.getCalendar().getTimeInMillis();
		long longFechamento = dia2.getCalendar().getTimeInMillis();
		if (longDia > longFechamento) {
			dia2 = Simulacao.getDiaFechamento(dia1.getMes().getMesPosterior());
			longFechamento = dia2.getCalendar().getTimeInMillis();
		}
		return (int) (((longFechamento - longDia) / (24*60*60*1000)) + 1);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Operacao))
			return false;
		return ((Operacao)other).ativo.equals(ativo)
			&& ((Operacao)other).getClass().equals(getClass());
	}
	
	@Override
	public String toString() {
		return qtdLotes + "x " + getClass().getSimpleName() + " " + ativo;
	}
	
	public int getQtdLotes() {
		return qtdLotes;
	}
	public void setQtdLotes(int qtdLotes) {
		this.qtdLotes = qtdLotes;
	}
	public void setAtivo(Ativo ativo) {
		this.ativo = ativo;
	}
	public Ativo getAtivo() {
		return ativo;
	}
	public int getMomento() {
		return momento;
	}
	public void setMomento(int momento) {
		this.momento = momento;
	}
	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}
	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}
	public Condicao getCondicao() {
		return condicao;
	}
	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}
	public void setReversa(Operacao reversa) {
		this.reversa = reversa;
	}
	public int getMesesParaReversao() {
		return mesesParaReversao;
	}
	public void setMesesParaReversao(int mesesParaReversao) {
		this.mesesParaReversao = mesesParaReversao;
	}
}
