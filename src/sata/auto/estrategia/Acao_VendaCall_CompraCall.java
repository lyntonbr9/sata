package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.simulacao.Simulacao;

public class Acao_VendaCall_CompraCall extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer ordemCallVenda = parametros[param++];
		Integer ordemCallCompra = parametros[param++];
		
		simulacoes.add(new Simulacao(
				new Venda (new RendaFixa(acao, 0.01), false),
				new Venda (new Call(acao, ordemCallVenda)),
				new Compra(new Call(acao, ordemCallCompra))));
	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraCall estrategia = new Acao_VendaCall_CompraCall();
		estrategia.acao = new Acao("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 4, -2);
	}

	@Override
	public String getNomeEstrategia(Integer... parametros) {
		return "Renda Fixa; Venda Call("+parametros[0]+"); Compra Call("+parametros[1]+")";
	}
}
