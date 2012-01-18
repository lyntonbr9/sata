package sata.auto.operacao.ativo.conteiner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sata.auto.operacao.ativo.Acao;

public class AcaoConteiner {
	
	private static List<String> acoes = Arrays.asList("PETR4", "VALE5", "OGXP3", "BVMF3");
	private static Map<String, Acao> conteiner = new TreeMap<String, Acao>();
	
	public static Acao get(String acao) {
		if (!conteiner.containsKey(acao))
			conteiner.put(acao, new Acao(acao));
		return conteiner.get(acao);
	}

	public static List<String> getAcoes() {
		return acoes;
	}
}
