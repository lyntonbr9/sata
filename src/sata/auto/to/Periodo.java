package sata.auto.to;

public class Periodo {
	
	Dia diaInicial;
	Dia diaFinal;
	
	public Periodo() {}
	
	public Periodo(Dia diaInicial, Dia diaFinal) {
		this.diaInicial = diaInicial;
		this.diaFinal = diaFinal;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Periodo))
			return false;
		return ((Periodo)other).diaInicial.equals(diaInicial)
			&& ((Periodo)other).diaFinal.equals(diaFinal);
	}
	
	@Override
	public String toString() {
		return diaInicial.formatoPadrao() + " - " + diaFinal.formatoPadrao();
	}
	
	public Dia getDiaInicial() {
		return diaInicial;
	}
	public void setDiaInicial(Dia diaInicial) {
		this.diaInicial = diaInicial;
	}
	public Dia getDiaFinal() {
		return diaFinal;
	}
	public void setDiaFinal(Dia diaFinal) {
		this.diaFinal = diaFinal;
	}
}
