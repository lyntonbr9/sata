package sata.auto.operacao;

import java.math.BigDecimal;

import sata.auto.operacao.ativo.Ativo;
import sata.auto.to.DataTO;
import sata.domain.util.IConstants;

public abstract class Operacao implements IConstants {
	
	static final char COMPRADO = 'C';
	static final char VENDIDO = 'V';
	public static final int PRIMEIRO_DIA = 0;
	public static final int ULTIMO_DIA = 1;
	
	int dia;
	Ativo ativo;
	int mesesParaVencimento;
	
	public BigDecimal getValor(DataTO data) {
		return ativo.getValor(data, this);
	}
	
	public abstract int getDiaPrecoAcao();
	public abstract int getDiaPrecoOpcao();
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + ativo;
	}

	public void setAtivo(Ativo ativo) {
		this.ativo = ativo;
	}
	public Ativo getAtivo() {
		return ativo;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}
	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}
}
