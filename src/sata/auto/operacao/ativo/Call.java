package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.operacao.Operacao;
import sata.auto.to.DataTO;


public class Call extends Opcao {
	
	public Call() {}
	
	public Call(Acao acao, Integer ordem) {
		this.acao = acao;
		this.ordem = ordem;
	}

	@Override
	public BigDecimal getValor(DataTO data, Operacao operacao) {
		return blackScholes(true,data,acao,operacao.getMesesParaVencimento(),operacao.getDiaPrecoAcao(),operacao.getDiaPrecoOpcao());
	}
}
