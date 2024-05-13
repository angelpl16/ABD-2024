package es.ubu.lsi.model.multas;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@NamedQuery(name="Vehiculo.findAll", query="SELECT v FROM Vehiculo v")
@Table(name = "Vehiculo")
public class Vehiculo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idauto", length = 3)
	private String idAuto;
	
	@Column(name="nombre", length = 50)
	private String nombre;
	
	@Embedded
	private Direccion direccion;
	

	@OneToMany(mappedBy = "vehiculo")
	private Set<Conductor> conductores;
	
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

	public Set<Conductor> getConductores() {
		return conductores;
	}
	

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Vehiculo [idAuto=" + idAuto + ", nombre=" + nombre + ", direccion=" + direccion + ", conductores="
				+ conductores + "]";
	}
	
	
}
