package es.ubu.lsi.model.multas;

import java.io.Serializable;


import javax.persistence.*;



@Entity
@NamedQuery(name="Incidencia.findAll", query="SELECT i FROM Incidencia i")
@Table(name = "Incidencia")
public class Incidencia implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "nif",
							column = @Column(name= "nif", length=10, nullable = false)),
		@AttributeOverride(name = "fecha",
							column=@Column(name = "fecha", length=7, nullable = false))
	})
	
	private IncidenciaPK id;

	@Column(name = "anotacion")
	private String anotacion;

	// Se puede hacer {cascade en OneToOne (Añadir un tipo que no existia)
	@OneToOne
	@JoinColumn(name = "idtipo")
	private int idTipo;
	
	//bi-directional many-to-one association to Conductor
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="NIF", nullable = true, insertable = false, updatable = false)		
	private Conductor conductor;

	public Incidencia() {

	}

	public String getAnotacion() {
		return anotacion;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public IncidenciaPK getId() {
		return id;
	}

	public void setId(IncidenciaPK id) {
		this.id = id;
	}

	public Conductor getConductor() {
		return conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", anotacion=" + anotacion + ", idTipo=" + idTipo + ", conductor=" + conductor
				+ "]";
	}

}
