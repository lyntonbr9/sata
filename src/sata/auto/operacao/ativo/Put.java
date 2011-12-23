package sata.auto.operacao.ativo;

import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.DataTO;

public class Put extends Opcao {
	
	public Put() {}
	
	public Put(Acao acao, Integer ordem) {
		this.acao = acao;
		this.ordem = ordem;
	}

	@Override
	public Preco criaPreco(DataTO data, Operacao operacao) {
		return new PrecoOpcao(false,data,acao,operacao.getMesesParaVencimento(),operacao.getMomento(),operacao.getMomentoOperacaoOpcao(), ordem);
	}
}
