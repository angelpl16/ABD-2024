package lsi.ubu.tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsi.ubu.excepciones.CompraBilleteTrenException;
import lsi.ubu.servicios.Servicio;
import lsi.ubu.servicios.ServicioImpl;
import lsi.ubu.util.PoolDeConexiones;

public class Tests {

	/** Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Tests.class);

	
	public static final String ORIGEN = "Burgos";
	public static final String DESTINO = "Madrid";

	/**
	 * ejecutarTestAnularBilletes: Se configuran y ejecutan los test para comprobar si se anula correctamente el billete.
	 * 
	 * @author <a href="mailto:xpl1001@alu.ubu.es">Angel Palacios</a>
	 * @version 1.0
	 */
	public void ejecutarTestsAnularBilletes() {

		Servicio servicio = new ServicioImpl();

		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		// Prueba caso no existe el viaje
		try {
			java.util.Date fecha = toDate("15/04/2010");
			Time hora = Time.valueOf("12:00:00");
			int nroPlazas = 3;
			int idTicket = 1;

			servicio.anularBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas, idTicket);

			LOGGER.info("NO se da cuenta de que no existe el viaje MAL");
		} catch (SQLException e) {
			if (e.getErrorCode() == CompraBilleteTrenException.NO_EXISTE_VIAJE) {
				LOGGER.info("Se da cuenta de que no existe el viaje OK");
			}
		}

		// Prueba caso no existe ticket
		//No se como relacionar la excepcion lanzada en la clase ServicioIMPL con esta son crear una tercera excepcion
		try {
			java.util.Date fecha = toDate("20/04/2022");
			Time hora = Time.valueOf("8:30:00");
			int nroPlazas = 10;
			int idTicket = 5;

			servicio.anularBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas, idTicket);

			LOGGER.info("NO se da cuenta de que no existe el ticket");
		} catch (SQLException e) {
			if (e.getErrorCode() == 0) {
				LOGGER.info("Detecta que no existe el ticket OK");
			}

		}

		// Prueba caso existe ticket y viaje
		try {
			java.util.Date fecha = toDate("20/04/2022");
			Time hora = Time.valueOf("8:30:00");
			int nroPlazas = 1;
			int idTicket = 1;

			servicio.anularBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas, idTicket);

			con = pool.getConnection();
			st = con.prepareStatement(" SELECT nPlazasLibres FROM viajes where idViaje = 1");
			rs = st.executeQuery();

			// Comprobar que se sumen los tickets eliminados
			
			
			if (rs.next()) {
				if (rs.getInt(1) == 31) {
					LOGGER.info("Se suman correctamente los viajes de los tickets OK");
				} else {
					LOGGER.info("NO se contabilizan los tickets MAL");
				}
			} else {
				LOGGER.info("NO se detecta viaje MAL");
			}
		} catch (SQLException e) {
			LOGGER.info("Error inesperado MAL");
		}
	}

	/**
	 * ejecutarTestCompraBilletes: Se configuran y ejecutan los test para probar la función de compra de billetes
	 * 
	 * @author Profesorado
	 * @version 1.0
	 */
	public void ejecutarTestsCompraBilletes() {

		Servicio servicio = new ServicioImpl();

		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		// Prueba caso no existe el viaje
		try {
			java.util.Date fecha = toDate("15/04/2010");
			Time hora = Time.valueOf("12:00:00");
			int nroPlazas = 3;

			servicio.comprarBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas);

			LOGGER.info("NO se da cuenta de que no existe el viaje MAL");
		} catch (SQLException e) {
			if (e.getErrorCode() == CompraBilleteTrenException.NO_EXISTE_VIAJE) {
				LOGGER.info("Se da cuenta de que no existe el viaje OK");
			}
		}

		// Prueba caso si existe pero no hay plazas
		try {
			java.util.Date fecha = toDate("20/04/2022");
			Time hora = Time.valueOf("8:30:00");
			int nroPlazas = 50;

			servicio.comprarBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas);

			LOGGER.info("NO se da cuenta de que no hay plazas MAL");
		} catch (SQLException e) {
			if (e.getErrorCode() == CompraBilleteTrenException.NO_PLAZAS) {
				LOGGER.info("Se da cuenta de que no hay plazas OK");
			}
		}

		// Prueba caso si existe y si hay plazas
		try {
			java.util.Date fecha = toDate("20/04/2022");
			Time hora = Time.valueOf("8:30:00");
			int nroPlazas = 5;

			servicio.comprarBillete(hora, fecha, ORIGEN, DESTINO, nroPlazas);

			con = pool.getConnection();
			st = con.prepareStatement(
					" SELECT IDVIAJE||IDTREN||IDRECORRIDO||FECHA||NPLAZASLIBRES||REALIZADO||IDCONDUCTOR||IDTICKET||CANTIDAD||PRECIO "
							+ " FROM VIAJES natural join tickets"
							+ " where idticket=3 and trunc(fechacompra) = trunc(current_date)");
			rs = st.executeQuery();

			String resultadoReal = "";
			while (rs.next()) {
				resultadoReal += rs.getString(1);
			}

			String resultadoEsperado = "11120/04/2225113550";
			// LOGGER.info("R"+resultadoReal);
			// LOGGER.info("E"+resultadoEsperado);
			if (resultadoReal.equals(resultadoEsperado)) {
				LOGGER.info("Compra ticket OK");
			} else {
				LOGGER.info("Compra ticket MAL");
			}

		} catch (SQLException e) {
			LOGGER.info("Error inesperado MAL");
		}
	}

	/**
	 * toDate: Convierte uan cadena en formato fecha
	 * 
	 * @param miString Cadena que contiene la fecha
	 * @return Fecha en el formato correcto
	 */
	private java.util.Date toDate(String miString) { // convierte una cadena en fecha
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Las M en mayusculas porque sino interpreta
																		// minutos!!
			java.util.Date fecha = sdf.parse(miString);
			return fecha;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
