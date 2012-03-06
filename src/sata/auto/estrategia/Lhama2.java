package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.simulacao.Simulacao;

public class Lhama2 extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		Venda rendaFixa = new Venda(new RendaFixa(acao));
		rendaFixa.setReversivel(false);
		
		Condicao volatilidadeBaixa = new Condicao(Atributo.VOLATILIDADE, Operador.MENOR, 0.3);
		Compra compra2xCallATM = new Compra(2, new Call(acao, 0), volatilidadeBaixa);
		Venda vendaCallOTM4_2meses = new Venda(new Call(acao, 4), 2, volatilidadeBaixa);
		Venda vendaCallITM2 = new Venda(new Call(acao, -2), volatilidadeBaixa);
		Compra compraCallOTM8_2meses = new Compra(new Call(acao, 8), 2, volatilidadeBaixa);
		
//		compra2xCallATM.setDiasParaFechamento(6);
//		vendaCallOTM4_2meses.setDiasParaFechamento(6);
//		vendaCallITM2.setDiasParaFechamento(6);
//		compraCallOTM8_2meses.setDiasParaFechamento(6);
		
		simulacoes.add(new Simulacao(
				rendaFixa,
				compra2xCallATM ,
				vendaCallOTM4_2meses,
				vendaCallITM2,
				compraCallOTM8_2meses));
	}
	
	public static void main(String[] args) {
		Lhama2 estrategia = new Lhama2();
		estrategia.acao = new Acao("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Lhama2("+acao.getNome()+")" + separador +
		anoInicial + separador + anoFinal;
	}
}
