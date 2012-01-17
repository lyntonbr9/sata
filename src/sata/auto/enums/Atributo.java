package sata.auto.enums;

public enum Atributo {
	PRECO,
	VOLATILIDADE,
	PERCENTUAL_OPERACAO,
	PERCENTUAL_ACAO,
	MEDIA_MOVEL;
	
	public static Atributo get(String atributo) {
		final String upper = atributo.toUpperCase();
		if (upper.equals("PRECO")) return PRECO;
		if (upper.equals("VOLATILIDADE")) return VOLATILIDADE;
		if (upper.equals("PERCENTUAL_OPERACAO")) return PERCENTUAL_OPERACAO;
		if (upper.equals("PERCENTUAL_ACAO")) return PERCENTUAL_ACAO;
		if (upper.equals("MEDIA_MOVEL")) return MEDIA_MOVEL;
		return null;
	}
}
