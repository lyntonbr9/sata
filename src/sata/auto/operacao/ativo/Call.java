package sata.auto.operacao.ativo;

import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.DataTO;


public class Call extends Opcao {
	
	public Call() {}
	
	public Call(Acao acao, Integer ordem) {
		this.acao = acao;
		this.ordem = ordem;
	}

	@Override
	public Preco criaPreco(DataTO data, Operacao operacao) {
		return new PrecoOpcao(true,data,acao,operacao.getMesesParaVencimento(),operacao.getMomento(),operacao.getMomentoOperacaoOpcao(), ordem);
	}
}
