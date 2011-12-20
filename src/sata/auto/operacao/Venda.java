package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
	public Venda() {}
	
	public Venda(Ativo ativo, int mesesParaVencimento, int dia) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.dia = dia;
	}

	@Override
	public int getDiaPrecoAcao() {
		return dia;
	}

	@Override
	public int getDiaPrecoOpcao() {
		if (dia == ULTIMO_DIA) 
			return PRIMEIRO_DIA; // Se está vendendo no último dia, buscar o valor do primeiro dia
		else return dia;
	}
}
