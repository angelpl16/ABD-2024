package es.ubu.lsi.model.multas;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class IncidenciaPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date fecha;

	@Column(insertable=false, updatable=false)
	private String nif;
	
	public IncidenciaPK() {
		
	}
	
	public IncidenciaPK(java.util.Date fecha, String nif) {
		this.fecha = fecha;
		this.nif = nif;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(fecha, nif);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IncidenciaPK other = (IncidenciaPK) obj;
		return Objects.equals(fecha, other.fecha) && Objects.equals(nif, other.nif);
	}

	@Override
	public String toString() {
		return "IncidenciaPK [fecha=" + fecha + ", nif=" + nif + "]";
	}
	
}
