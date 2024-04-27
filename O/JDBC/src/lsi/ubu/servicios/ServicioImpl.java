package lsi.ubu.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsi.ubu.excepciones.CompraBilleteTrenException;
import lsi.ubu.util.PoolDeConexiones;

public class ServicioImpl implements Servicio {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicioImpl.class);

	@Override
	public void anularBillete(Time hora, java.util.Date fecha, String origen, String destino, int nroPlazas, int ticket)
			throws SQLException {
		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		/* Conversiones de fechas y horas */
		java.sql.Date fechaSqlDate = new java.sql.Date(fecha.getTime());
		java.sql.Timestamp horaTimestamp = new java.sql.Timestamp(hora.getTime());

		Connection con = pool.getConnection();
		/*
		 * try {
		 * 
		 * } catch (CompraBilleteTrenException e) {
		 * 
		 * } finally {
		 * 
		 * }
		 */

	}

	@Override
	public void comprarBillete(Time hora, Date fecha, String origen, String destino, int nroPlazas)
			throws SQLException {
		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		/* Conversiones de fechas y horas */
		java.sql.Date fechaSqlDate = new java.sql.Date(fecha.getTime());
		java.sql.Timestamp horaTimestamp = new java.sql.Timestamp(hora.getTime());

		long fechaLong = fechaSqlDate.getTime();
		java.sql.Timestamp fechaTime = new java.sql.Timestamp(fechaLong);

		long horaLong = horaTimestamp.getTime();

		long fechaCompleta = horaLong + (fechaTime.getTime() - (fechaTime.getTime() % 100));

		java.sql.Timestamp timeFinal = new java.sql.Timestamp(fechaCompleta);

		Connection con = pool.getConnection();
		/*
		 * PreparedStatement st = null; ResultSet rs = null;
		 */

		boolean existeRecorridoBool =false;
		// Result Sets
		ResultSet existeRecorrido = null;

		try {
			// Obtenemos el id del viaje asociado al ticket
			String conBilletesLibres = "SELECT viajes.idViaje FROM viajes INNER JOIN recorridos on viajes.idRecorrido = recorridos.idRecorrido WHERE viajes.fecha = ? and estacionOrigen = ? and estacionDestino = ?";
			PreparedStatement stBilletesLibres = con.prepareStatement(conBilletesLibres);

			stBilletesLibres.setDate(1, fechaSqlDate);
			stBilletesLibres.setString(2, origen);
			stBilletesLibres.setString(3, destino);

			existeRecorrido = stBilletesLibres.executeQuery();
			if (existeRecorrido.next()) {
				existeRecorridoBool = true;
			}
			else {
				throw new SQLException();
			}
			String insBilletes = "INSERT INTO tickets (idTicket, idViaje, fechaCompra, cantidad, precio) VALUES (seq_tickets.nextval, ?, ?, ?, 50)";
			PreparedStatement stBilletes = con.prepareStatement(insBilletes);

			int idViaje = existeRecorrido.getInt(1);
			stBilletes.setInt(1, idViaje);
			stBilletes.setTimestamp(2, timeFinal);
			stBilletes.setInt(3, nroPlazas);

			stBilletes.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			con.rollback();
			System.out.println(e.getErrorCode() + e.getMessage());
			/*String conExisteRecorrido = "SELECT * FROM viajes INNER JOIN recorridos on viajes.idRecorrido = recorridos.idRecorrido WHERE viajes.fecha = ? and estacionOrigen = ? and estacionDestino = ?";
			PreparedStatement stExisteRecorrido = con.prepareStatement(conExisteRecorrido);

			stExisteRecorrido.setDate(1, fechaSqlDate);
			stExisteRecorrido.setString(2, origen);
			stExisteRecorrido.setString(3, destino);

			existeRecorrido = stExisteRecorrido.executeQuery();*/

			if (existeRecorridoBool == true) {
				throw new CompraBilleteTrenException(1);
			} else {
				throw new CompraBilleteTrenException(2);
			}

		} finally {
			if (existeRecorrido!= null) {
				//billetesLibres.close();
				existeRecorrido = null;
			}

			con.close();
		}
		// Toda la información inferior es como resultaría el programa con una
		// programación con enfoque defensivo
		/*
		 * try { //Comprobamos si el recorrido existe //Para saber si el recorrido
		 * existe comprobamos que haya al menos un recorrido que salga de la estacion a
		 * la hora indicada
		 * 
		 * ResultSet existeRecorrido;
		 * 
		 * 
		 * String conExisteRecorrido =
		 * "SELECT count(*) FROM recorridos WHERE estacionOrigen = ? and horaSalida = ?"
		 * ; PreparedStatement stExisteRecorrido =
		 * con.prepareStatement(conExisteRecorrido);
		 * 
		 * stExisteRecorrido.setString(1, origen); stExisteRecorrido.setTimestamp(2,
		 * horaTimestamp);
		 * 
		 * existeRecorrido = stExisteRecorrido.executeQuery();
		 * 
		 * if (!existeRecorrido.next()) { throw new CompraBilleteTrenException(2); }
		 * 
		 * //Comprobamos si en el viaje solicitado quedan billetes libres //Si hay
		 * asientos libres la variable nPlazasLibres no será igual a 0
		 * 
		 * ResultSet billetesLibres;
		 * 
		 * String conBilletesLibres =
		 * "SELECT nPlazasLibres, idViajes FROM viajes INNER JOIN recorridos on viajes.idRecorrido = recorridos.idRecorrido WHERE viajes.fecha = ? and estacionOrigen = ? and estacionDestino = ?"
		 * ; PreparedStatement stBilletesLibres =
		 * con.prepareStatement(conBilletesLibres);
		 * 
		 * stBilletesLibres.setDate(1, fechaSqlDate); stBilletesLibres.setString(2,
		 * origen); stBilletesLibres.setString(3, destino);
		 * 
		 * billetesLibres = stBilletesLibres.executeQuery();
		 * 
		 * if (billetesLibres.getInt(0) == 0) { throw new CompraBilleteTrenException(1);
		 * }
		 * 
		 * String insBilletes =
		 * "INSERT INTO tickets (idTicket, idViaje, fechaCompra, cantidad, precio) VALUES seq_tickets.nextval, ?, ?, ?, 10)"
		 * ; PreparedStatement stBilletes = con.prepareStatement(insBilletes);
		 * 
		 * stBilletes.setInt(1, billetesLibres.getInt(1)); stBilletes.setDate(2,
		 * fechaSqlDate); stBilletes.setInt(3, nroPlazas);
		 * 
		 * stBilletes.executeUpdate();
		 * 
		 * 
		 * con.commit();
		 * 
		 * stExisteRecorrido.close(); stBilletesLibres.close(); stBilletes.close();
		 * 
		 * } catch (SQLException e) { con.rollback();
		 * 
		 * if (con != null) { con.rollback(); }
		 * 
		 * if (e instanceof CompraBilleteTrenException) { throw
		 * (CompraBilleteTrenException) e; }
		 * 
		 * } finally { con.close(); }
		 */

	}

}
