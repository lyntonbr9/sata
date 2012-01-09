package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.ativo.Acao;
import sata.auto.simulacao.Simulacao;

public class SomenteAcao extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		simulacoes.add(new Simulacao(new Compra(acao)));
	}
	
	public static void main(String[] args) {
		SomenteAcao estrategia = new SomenteAcao();
		estrategia.acao = new Acao("OGXP3");
		estrategia.anoInicial = 2010;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.REINVESTIMENTO);
	}

	@Override
	public String getNomeEstrategia(Integer... parametros) {
		return "Compra Ação";
	}
}
