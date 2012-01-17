package sata.auto.enums;

public enum Operador {
	IGUAL,
	DIFERENTE,
	MAIOR,
	MENOR,
	MAIOR_IGUAL,
	MENOR_IGUAL;
	
	public static Operador get(String operador) {
		final String upper = operador.toUpperCase();
		if (upper.equals("IGUAL")) return IGUAL;
		if (upper.equals("DIFERENTE")) return DIFERENTE;
		if (upper.equals("MAIOR")) return MAIOR;
		if (upper.equals("MENOR")) return MENOR;
		if (upper.equals("MAIOR_IGUAL")) return MAIOR_IGUAL;
		if (upper.equals("MENOR_IGUAL")) return MENOR_IGUAL;
		return null;
	}
}
