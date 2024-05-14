package es.ubu.lsi.dao.multas;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.*;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.multas.Conductor;

public class ConductorDAO extends JpaDAO<Conductor, String> {

	private static final Logger logger = LoggerFactory.getLogger(ConductorDAO.class);
	
	public ConductorDAO(EntityManager em) {
		super(em);
	}

	@Override
	public List<Conductor> findAll() {
		try {
			EntityManager em = getEntityManager();
			return em.createNamedQuery("Conductor.findAll", Conductor.class).getResultList();
		
		}catch(Exception ex) {
			logger.error("Error querying asociacion: {}", ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}
}
