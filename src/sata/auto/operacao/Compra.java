package sata.auto.operacao;

import java.math.BigDecimal;

import sata.auto.operacao.ativo.Ativo;
import sata.auto.to.DataTO;

public class Compra extends Operacao {
	
	public Compra() {}
	
	public Compra(Ativo ativo, int mesesParaVencimento, int dia) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.dia = dia;
	}
	
	@Override
	public BigDecimal getValor(DataTO data) {
		return super.getValor(data).negate();
	}

	@Override
	public int getDiaPrecoAcao() {
		return dia;
	}

	@Override
	public int getDiaPrecoOpcao() {
		return dia;
	}
}
