package es.ubu.lsi.model.multas;

import javax.persistence.*;

@Entity
@Table(name = "Vehiculo")
public class Vehiculo {
	
	@Id
	@Column(name="idauto", length = 3)
	private String idAuto;
	
	@Column(name="nombre", length = 50)
	private String nombre;
	
	@Column(name="direccion", length = 100)
	private String direccion;
	
	@Column(name="cp", length = 5)
	private String cp;
	
	@Column(name="ciudad", length = 20)
	private String ciudad;
	
	public Vehiculo() {
		
	}

	public String getIdAuto() {
		return idAuto;
	}

	public void setIdAuto(String idAuto) {
		this.idAuto = idAuto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return "Vehiculo [idAuto=" + idAuto + ", nombre=" + nombre + ", direccion=" + direccion + ", cp=" + cp
				+ ", ciudad=" + ciudad + "]";
	}
	
	
}
