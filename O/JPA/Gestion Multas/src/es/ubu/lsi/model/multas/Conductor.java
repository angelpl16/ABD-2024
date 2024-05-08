package es.ubu.lsi.model.multas;

import javax.persistence.*;

@Entity
@Table(name="Conductor")
public class Conductor {
	@Id
	@Column(name = "nif", length = 10)
	private String nif;
	
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column(name = "apellido", length = 50)
	private String apellido;
	
	@Column(name = "direccion", length = 100)
	private String direccion;
	
	@Column(name = "cp", length = 5)
	private int cp;
	
	@Column(name = "ciudad", length = 20)
	private String Ciudad;
	
	@Column(name = "puntos", precision = 3, scale = 0)
	private int puntos;
	
	@Column(name = "idauto", length = 3)
	private String idAuto;
	
	public Conductor() {
		
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
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

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getCiudad() {
		return Ciudad;
	}

	public void setCiudad(String ciudad) {
		Ciudad = ciudad;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public String getIdAuto() {
		return idAuto;
	}

	public void setIdAuto(String idAuto) {
		this.idAuto = idAuto;
	}

	@Override
	public String toString() {
		return "Conductor [nif=" + nif + ", nombre=" + nombre + ", apellido=" + apellido + ", direccion=" + direccion
				+ ", cp=" + cp + ", Ciudad=" + Ciudad + ", puntos=" + puntos + ", idAuto=" + idAuto + "]";
	}
	
	
}
