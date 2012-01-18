package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoRendaFixa extends Preco implements IConstants {
	
	RendaFixa rendaFixa;
	BigDecimal precoAcao;
	BigDecimal percentual;
	
	public PrecoRendaFixa() {}
	
	public PrecoRendaFixa(RendaFixa rendaFixa, Dia dia) {
		this.dia = dia;
		this.rendaFixa = rendaFixa;
	}

	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		percentual = new BigDecimal(SATAUtil.getTaxaDeJuros(dia.getAno())/12);
		valor = calculaPrecoAcao().multiply(percentual);
	}
	
	@Override
	public BigDecimal calculaMediaMovel(Integer periodo) throws CotacaoInexistenteEX {
		return rendaFixa.getAcao().getMediaMovel(dia, periodo);
	}
	
	private BigDecimal calculaPrecoAcao() throws CotacaoInexistenteEX {
		precoAcao = rendaFixa.getAcao().getPreco(dia);
		volatilidade = rendaFixa.getAcao().getVolatilidade(dia);
		return precoAcao;
	}

	
	@Override
	public String toString() {
		return SATAUtil.getMessage(MSG_PATTERN_PRECO_RENDA_FIXA, 
				SATAUtil.formataNumero(precoAcao), 
				SATAUtil.formataNumero(percentual.multiply(CEM)), 
				SATAUtil.formataNumero(valor),
				SATAUtil.formataNumero(volatilidade));
	}

	public BigDecimal getPrecoAcao() {
		return precoAcao;
	}
	public void setPrecoAcao(BigDecimal precoAcao) {
		this.precoAcao = precoAcao;
	}
	public RendaFixa getRendaFixa() {
		return rendaFixa;
	}
	public void setRendaFixa(RendaFixa rendaFixa) {
		this.rendaFixa = rendaFixa;
	}
}
