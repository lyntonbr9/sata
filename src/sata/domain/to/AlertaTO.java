package sata.domain.to;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.domain.util.IConstants;

public class AlertaTO implements IConstants {
	
	private Integer id;
	private String nome;
	private Integer porcentagemGanho;
	private Integer porcentagemPerda;
	private TipoCalculoValorInvestido tipoCalculoVI;
	private Integer percCalculoVI;
	private Collection<SerieOperacoesTO> series;
	
	public boolean alertaPorcentagemGanho(BigDecimal porcentagemSerie) {
		BigDecimal percGanho = new BigDecimal(porcentagemGanho);
		return porcentagemSerie.doubleValue() >= percGanho.doubleValue();
	}

	public boolean alertaPorcentagemPerda(BigDecimal porcentagemSerie) {
		BigDecimal percPerda = new BigDecimal(porcentagemPerda);
		return porcentagemSerie.doubleValue() <= percPerda.negate().doubleValue();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,27).
	       append(id).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getPorcentagemGanho() {
		return porcentagemGanho;
	}
	public void setPorcentagemGanho(Integer porcentagemGanho) {
		this.porcentagemGanho = porcentagemGanho;
	}
	public Integer getPorcentagemPerda() {
		return porcentagemPerda;
	}
	public void setPorcentagemPerda(Integer porcentagemPerda) {
		this.porcentagemPerda = porcentagemPerda;
	}
	public TipoCalculoValorInvestido getTipoCalculoVI() {
		return tipoCalculoVI;
	}
	public void setTipoCalculoVI(TipoCalculoValorInvestido tipoCalculoVI) {
		this.tipoCalculoVI = tipoCalculoVI;
	}
	public Integer getPercCalculoVI() {
		return percCalculoVI;
	}
	public void setPercCalculoVI(Integer percCalculoVI) {
		this.percCalculoVI = percCalculoVI;
	}
	public Collection<SerieOperacoesTO> getSeries() {
		return series;
	}
	public void setSeries(Collection<SerieOperacoesTO> series) {
		this.series = series;
	}
}
