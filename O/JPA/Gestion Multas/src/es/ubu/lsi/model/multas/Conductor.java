package es.ubu.lsi.model.multas;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@NamedQuery(name="Conductor.findAll", query="SELECT c FROM Conductor c")
@Table(name="Conductor")
public class Conductor implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nif", length = 10)
	private String nif;
	
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column(name = "apellido", length = 50)
	private String apellido;
	
	@Embedded
	private Direccion direccion;
	
	@Column(name = "puntos", precision = 3, scale = 0)
	private int puntos;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idAuto")
	private Vehiculo vehiculo;
	
	@OneToMany(mappedBy = "conductor")
	private Set<Incidencia> incidencias;
	
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

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	public Set<Incidencia> getIncidencias(){
		return null;
	}

	@Override
	public String toString() {
		return "Conductor [nif=" + nif + ", nombre=" + nombre + ", apellido=" + apellido + ", direccion=" + direccion
				+ ", puntos=" + puntos + ", vehiculo=" + vehiculo + "]";
	}
	
	
}
