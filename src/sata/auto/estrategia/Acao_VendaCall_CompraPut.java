package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Stop;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Simulacao;

public class Acao_VendaCall_CompraPut extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer qtdLotesCall = parametros[param++];
		Integer ordemCall = parametros[param++];
		Integer ordemPut = parametros[param++];
		
		Simulacao simulacao = new Simulacao(new Compra(new Call (acao, -10)),
											new Venda (qtdLotesCall, new Call(acao, ordemCall)),
											new Compra(new Put (acao, ordemPut)));
//		simulacao.setStop(new Stop(Atributo.PERCENTUAL_ACAO, Operador.MAIOR, 8));
		
		simulacoes.add(simulacao);	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraPut estrategia = new Acao_VendaCall_CompraPut();
		estrategia.acao = new Acao("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 2, 4, -1);
	}

	@Override
	public String getNomeEstrategia(Integer... parametros) {
		return "Compra Ação; Venda Call("+parametros[0]+"); Compra Put("+parametros[1]+")";
	}
}
