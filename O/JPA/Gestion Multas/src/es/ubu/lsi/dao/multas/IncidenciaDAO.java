package es.ubu.lsi.dao.multas;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.multas.Incidencia;
import es.ubu.lsi.model.multas.IncidenciaPK;

public class IncidenciaDAO extends JpaDAO<Incidencia, IncidenciaPK> {

	public IncidenciaDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(ConductorDAO.class);
	
	@Override
	public List<Incidencia> findAll() {
		try {
			TypedQuery<Incidencia> query = getEntityManager().createNamedQuery("Incidencia.findAll", Incidencia.class);
			
			return query.getResultList();
		}catch(Exception ex) {
			logger.error("Error querying asociacion: {}", ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}

}
