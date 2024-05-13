package es.ubu.lsi.model.multas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Direccion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ciudad", length=20)
	private String ciudad;
	
	@Column(name = "cp", length=5)
	private String codigoPostal;
	
	@Column(name = "direccion", length=100)
	private String direccion;

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Direccion [ciudad=" + ciudad + ", codigoPostal=" + codigoPostal + ", direccion=" + direccion + "]";
	}
	
	
	
}
