package sata.auto.estrategia;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;

public class PutATM_CallOTM extends Estrategia {
	
	@Override
	public void executa() {
		Acao acao = new Acao("PETR4");
		Put putATM = new Put(acao, 0);
		Call callOTM = new Call(acao, 4);
		
		Condicao volatilidadeBaixa = new Condicao(Condicao.VOLATILIDADE, Condicao.MENOR, 0.35);
		Condicao volatilidadeAlta = new Condicao(Condicao.VOLATILIDADE, Condicao.MAIOR_IGUAL, 0.35);
		
		simulacao.getOperacoes().add(new Compra(acao, 1, null));
		simulacao.getOperacoes().add(new Compra(putATM, 1, null));
		simulacao.getOperacoes().add(new Venda(callOTM, 2, volatilidadeBaixa));
		simulacao.getOperacoes().add(new Venda(callOTM, 1, volatilidadeAlta));
		
		simulacao.setAnoInicial(2000);
		simulacao.setAnoFinal(2011);
	}
	
	public static void main(String[] args) {
		executa(new PutATM_CallOTM());
	}
}
