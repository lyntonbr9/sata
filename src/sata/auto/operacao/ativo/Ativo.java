package sata.auto.operacao.ativo;

import java.util.HashMap;
import java.util.Map;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.Dia;
import sata.domain.util.SATAUtil;

public abstract class Ativo {
	
	Map<Dia,Preco> precos = new HashMap<Dia, Preco>();
	
	abstract Preco criaPreco(Dia dia, Operacao operacao) throws SATAEX;
	public abstract String getBundleMessage();
	
	public Preco calculaPreco(Dia dia, Operacao operacao) throws SATAEX {
		Preco preco;
		if (!precos.containsKey(dia)) {
			preco = criaPreco(dia, operacao);
			precos.put(dia, preco);
		}
		else preco = precos.get(dia);
		preco.calculaPreco();
		return preco;
	}
	
	public Preco getPreco(Dia dia, Operacao operacao) throws SATAEX {
		return calculaPreco(dia, operacao);
	}
	
	public void limpaPrecos() {
		precos.clear();
	}
	
	@Override
	public String toString() {
		return SATAUtil.getMessage(getBundleMessage());
	}
}
