package es.ubu.lsi.model.multas;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="TipoIncidencia")
public class TipoIncidencia implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="descripcion", length = 30)
	private String descripcion;
	
	@Column(name="valor")
	private int valor;
	
	@OneToMany(mappedBy="tipoincidencia")
	private Set<Incidencia> incidencias;

	
	public TipoIncidencia() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
	
	public Set<Incidencia> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(Set<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

	@Override
	public String toString() {
		return "TipoIncidencia [id=" + id + ", descripcion=" + descripcion + ", valor=" + valor + "]";
	}

	
}
