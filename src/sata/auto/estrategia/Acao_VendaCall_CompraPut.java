package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Simulacao;

public class Acao_VendaCall_CompraPut extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer ordemCall = parametros[param++];
		Integer ordemPut = parametros[param++];
		
		simulacoes.add(new Simulacao(new Compra(acao)));
		simulacoes.add(new Simulacao(
				new Venda (new Call(acao, ordemCall)),
				new Compra(new Put (acao, ordemPut))));
	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraPut estrategia = new Acao_VendaCall_CompraPut();
		estrategia.acao = new Acao("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2006;
		estrategia.executa(TipoRelatorio.REINVESTIMENTO, 7, -12);
	}

	@Override
	public String getNomeEstrategia(Integer... parametros) {
		return "Compra Ação; Venda Call("+parametros[0]+"); Compra Put("+parametros[1]+")";
	}
}
