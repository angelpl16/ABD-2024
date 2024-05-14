package es.ubu.lsi.dao.multas;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.multas.Vehiculo;

public class VehiculoDAO extends JpaDAO<Vehiculo, String> {

	public VehiculoDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(ConductorDAO.class);
	
	@Override
	public List<Vehiculo> findAll() {
		try {
			TypedQuery<Vehiculo> query = getEntityManager().createNamedQuery("Vehiculo.findAll", Vehiculo.class);
			
			return query.getResultList();
		}catch(Exception ex) {
			logger.error("Error querying asociacion: {}", ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}
	
//	public List<Vehiculo> findAllGraph() {
//	return entityManager.createNamedQuery("Vehiculo.findAll")
//			.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("InfoVehiculoGrafo"))
//			.getResultList();
//	}

}
