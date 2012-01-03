package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Simulacao;

public class PutATM_CallOTM extends Estrategia {
	
	@Override
	public void prepara() {
		anoInicial = 2000;
		anoFinal = 2011;
		Acao acao = new Acao("PETR4");
		
		Put putATM = new Put(acao, 0);
		Call callOTM = new Call(acao, 4);
		
		Condicao volatilidadeBaixa = new Condicao(Atributo.VOLATILIDADE, Operador.MENOR, 0.3);
		Condicao volatilidadeAlta = new Condicao(Atributo.VOLATILIDADE, Operador.MAIOR_IGUAL, 0.3);
		
		simulacoes.add(new Simulacao(
				new Compra(acao, 1),
				new Compra(putATM, 1),
				new Venda(callOTM, 2, volatilidadeBaixa),
				new Venda(callOTM, 1, volatilidadeAlta)));
	}
	
	public static void main(String[] args) {
		new PutATM_CallOTM().executa(TipoRelatorio.REINVESTIMENTO);
	}
}
