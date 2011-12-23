package sata.auto.operacao.ativo;

public abstract class Opcao extends Ativo {
	
	Acao acao;
	Integer ordem;
	
	public Integer getOrdem() {
		return ordem;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + ordem + ")";
	}
}
