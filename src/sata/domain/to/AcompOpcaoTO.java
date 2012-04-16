package sata.domain.to;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="AcompOpcao")
public class AcompOpcaoTO implements TO {

	@Id @GeneratedValue
	@Column
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_acomp")
	private AcompanhamentoTO acompanhamento;
	
	@Column
	private Integer percExercicio;
	
	@Column
	private Integer percTolerancia;
	
	public Integer getPercToleradoSuperior() {
		return percExercicio + percTolerancia;
	}
	
	public Integer getPercToleradoInferior() {
		return percExercicio - percTolerancia;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(25,67).
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
	public Integer getPercExercicio() {
		return percExercicio;
	}
	public void setPercExercicio(Integer percExercicio) {
		this.percExercicio = percExercicio;
	}
	public Integer getPercTolerancia() {
		return percTolerancia;
	}
	public void setPercTolerancia(Integer percTolerancia) {
		this.percTolerancia = percTolerancia;
	}
	public AcompanhamentoTO getAcompanhamento() {
		return acompanhamento;
	}
	public void setAcompanhamento(AcompanhamentoTO acompanhamento) {
		this.acompanhamento = acompanhamento;
	}
}
