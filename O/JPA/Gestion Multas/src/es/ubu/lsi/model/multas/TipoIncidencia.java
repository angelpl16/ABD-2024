package es.ubu.lsi.model.multas;

import javax.persistence.*;

@Entity
@Table(name="TipoIncidencia")
public class TipoIncidencia {
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="descripcion", length = 30)
	private String descripcion;
	
	@Column(name="valor")
	private int valor;
	
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

	@Override
	public String toString() {
		return "TipoIncidencia [id=" + id + ", descripcion=" + descripcion + ", valor=" + valor + "]";
	}

	
}
