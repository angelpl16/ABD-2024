package es.ubu.lsi.model.multas;
import java.security.Timestamp;
import java.util.Date;

import javax.persistence.*;

import oracle.sql.CLOB;

@Entity
@Table(name = "Incidencia")
public class Incidencia {

	@Id
	@Column(name="fecha")
	private Timestamp fecha;
	
	@Column(name="nif", length = 10)
	private CLOB nif;
	
	@Column(name="anotacion")
	private String anotacion;
	
	@Column(name="idtipo")
	private int idTipo;
	
	public Incidencia() {
		
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public CLOB getNif() {
		return nif;
	}

	public void setNif(CLOB nif) {
		this.nif = nif;
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

	@Override
	public String toString() {
		return "Incidencia [fecha=" + fecha + ", nif=" + nif + ", anotacion=" + anotacion + ", idTipo=" + idTipo + "]";
	}
	
	
	
}
