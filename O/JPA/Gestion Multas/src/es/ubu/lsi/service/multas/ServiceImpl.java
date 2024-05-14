package es.ubu.lsi.service.multas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.multas.ConductorDAO;
import es.ubu.lsi.dao.multas.TipoIncidenciaDAO;
import es.ubu.lsi.model.multas.Conductor;
import es.ubu.lsi.model.multas.TipoIncidencia;
import es.ubu.lsi.model.multas.Vehiculo;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

public class ServiceImpl extends PersistenceService implements Service {

	private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);
	
	@Override
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException {
				
		EntityManager em = this.createSession();
		
		try {
			
		beginTransaction(em);

		ConductorDAO conductorDAO = new ConductorDAO(em);
		TipoIncidenciaDAO tipoDAO = new TipoIncidenciaDAO(em);

		Conductor conductor = conductorDAO.findById(nif);
		TipoIncidencia tipoIncidencia = tipoDAO.findById(tipo);
		
			if (conductor == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
			}

			if (tipoIncidencia == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_INCIDENT_TYPE);
			}
			
			String anotacion = tipoIncidencia.getDescripcion();
			Long valor = tipoIncidencia.getValor();

			em.createNativeQuery("INSERT INTO Incidencia " + "(fecha, NIF, anotacion, IDTIPO) VALUES (?1, ?2, ?3, ?4)")
					.setParameter(1, fecha).setParameter(2, nif).setParameter(3, anotacion).setParameter(4, tipo)
					.executeUpdate();

			if (conductor.getNif().equals(nif)) {

				if (conductor.getPuntos() -valor >= 0) {
					conductor.setPuntos((int) (conductor.getPuntos()- valor));
				} else {
					conductor.setPuntos(0);
					throw new IncidentException(IncidentError.NOT_AVAILABLE_POINTS);

				}
			}
			
		} catch (Exception e) {
			
			if (e instanceof IncidentException) {
				throw (IncidentException) e;
			}
			
			logger.error("Error en la transaccion: " + e.toString());
			
			rollbackTransaction(em);

		} finally {

			if (em != null)
				em.close();
		}

	}

	@Override
	public void indultar(String nif) throws PersistenceException {
	}

	@Override
	public List<Vehiculo> consultarVehiculos() throws PersistenceException {
		return null;
	}

}
