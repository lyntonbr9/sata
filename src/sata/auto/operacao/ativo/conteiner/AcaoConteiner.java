package sata.auto.operacao.ativo.conteiner;

import java.util.Arrays;
import java.util.List;

import sata.auto.operacao.ativo.Acao;

public class AcaoConteiner {
	
	private static List<Acao> acoes = Arrays.asList(new Acao("PETR4"), new Acao("VALE5"), new Acao("OGXP3"), new Acao("BVMF3"));
	
	public static Acao get(String codigoAcao) {
		for (Acao acao: acoes)
			if (acao.getNome().equals(codigoAcao))
				return acao;
		return null;
	}

	public static List<Acao> getAcoes() {
		return acoes;
	}
}
