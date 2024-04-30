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
		
		int numTicket;

		/* Conversiones de fechas y horas */
		java.sql.Date fechaSqlDate = new java.sql.Date(fecha.getTime());
		java.sql.Timestamp horaTimestamp = new java.sql.Timestamp(hora.getTime());

		Connection con = pool.getConnection();
		
		int idViaje = 0;
		

		try {
			idViaje = obtenerViaje(fechaSqlDate, origen, destino, con);
			
			if (idViaje == -1) {
				throw new SQLException();
			}
			numTicket = existeTicket(ticket,con);
			
			if(numTicket == -1) {
				throw new SQLException("No existe un ticket con ese ID");
			}
			
			
			if (numTicket < nroPlazas) {
				throw new SQLException("El número de billetes que se quiere eliminar es superior al de los billetes reservados");
			}
			
			String updPlazas = "UPDATE viajes v set v.nPlazasLibres = v.nPlazasLibres + ? WHERE v.fecha = ? AND v.idRecorrido IN (SELECT r.idRecorrido FROM recorridos r WHERE r.estacionOrigen = ? AND r.estacionDestino = ?)";
			PreparedStatement stUpdPlazas = con.prepareStatement(updPlazas);
			
			stUpdPlazas.setInt(1, nroPlazas);
			stUpdPlazas.setDate(2, fechaSqlDate);
			stUpdPlazas.setString(3, origen);
			stUpdPlazas.setString(4, destino);
			
			stUpdPlazas.executeUpdate();
			
			con.commit();
		} catch (SQLException e) {
			if (idViaje == -1) {
				throw new CompraBilleteTrenException(2);
			} else {
				LOGGER.info(e.getErrorCode()+": "+e.getMessage());
				
			}
		} 

	}

	@Override
	public void comprarBillete(Time hora, Date fecha, String origen, String destino, int nroPlazas)
			throws SQLException {
		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		/* Conversiones de fechas y horas */
		java.sql.Date fechaSqlDate = new java.sql.Date(fecha.getTime());
		java.sql.Timestamp horaTimestamp = new java.sql.Timestamp(hora.getTime());

//		long fechaLong = fechaSqlDate.getTime();
//		java.sql.Timestamp fechaTime = new java.sql.Timestamp(fechaLong);

//		long horaLong = horaTimestamp.getTime();

	//	long fechaCompleta = horaLong + (fechaTime.getTime() - (fechaTime.getTime() % 100));

//		java.sql.Timestamp timeFinal = new java.sql.Timestamp(fechaCompleta);

		Connection con = pool.getConnection();


		int idViaje = 0;
		int ticketsDispo = 0;

		try {
//
			idViaje = obtenerViaje(fechaSqlDate, origen, destino, con);

			if (idViaje == -1) {
				throw new SQLException();
			}

			ticketsDispo = obtenerPlazasLibres(idViaje, nroPlazas, con);

			if (ticketsDispo == -1) {
				throw new SQLException();
			}

			String insBilletes = "INSERT INTO tickets (idTicket, idViaje, fechaCompra, cantidad, precio) VALUES (seq_tickets.nextval, ?, ?, ?, 50)";
			PreparedStatement stBilletes = con.prepareStatement(insBilletes);

			Date fechaActual = new Date();
			Timestamp actualDia = new Timestamp(fechaActual.getTime());
			stBilletes.setInt(1, idViaje);
			stBilletes.setTimestamp(2, actualDia);
			stBilletes.setInt(3, nroPlazas);

			stBilletes.executeUpdate();

			int libresFinal = calculaTicketsLibres(ticketsDispo, nroPlazas);

			String updCantidad = "UPDATE viajes SET nPlazasLibres = ? WHERE idViaje = ?";
			PreparedStatement stCantidad = con.prepareStatement(updCantidad);

			stCantidad.setInt(1, libresFinal);
			stCantidad.setInt(2, idViaje);

			stCantidad.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			if (idViaje == -1) {
				throw new CompraBilleteTrenException(2);
			} else if (ticketsDispo == -1) {
				throw new CompraBilleteTrenException(1);
			} else {
				LOGGER.info(e.getErrorCode()+": "+e.getMessage());
			}

		} finally {
			con.close();
		}

	}

	private int obtenerViaje(java.sql.Date fechaSqlDate, String origen, String destino, Connection con)
			throws SQLException {
		ResultSet existeRecorrido = null;
		int idViaje = 0;
		try {
			String conBilletesLibres = "SELECT viajes.idViaje FROM viajes INNER JOIN recorridos on viajes.idRecorrido = recorridos.idRecorrido WHERE viajes.fecha = ? and estacionOrigen = ? and estacionDestino = ?";
			PreparedStatement stBilletesLibres = con.prepareStatement(conBilletesLibres);

			stBilletesLibres.setDate(1, fechaSqlDate);
			stBilletesLibres.setString(2, origen);
			stBilletesLibres.setString(3, destino);

			existeRecorrido = stBilletesLibres.executeQuery();
			if (!existeRecorrido.next()) {
				throw new SQLException();
			}
			idViaje = existeRecorrido.getInt(1);
		} catch (SQLException e) {
			con.rollback();

			idViaje = -1;

		} finally {
			existeRecorrido.close();
		}

		return idViaje;
	}

	private int obtenerPlazasLibres(int idViaje, int nroPlazas, Connection con) throws SQLException {
		ResultSet ticketsLibres = null;
		int ticketsDispo = 0;
		try {
			String conPlazasLibres = "SELECT nPlazasLibres FROM viajes where idViaje = ?";
			PreparedStatement stTicketsLibres = con.prepareStatement(conPlazasLibres);

			stTicketsLibres.setInt(1, idViaje);

			ticketsLibres = stTicketsLibres.executeQuery();
			if (ticketsLibres.next()) {
				ticketsDispo = ticketsLibres.getInt(1);
			}

			if (ticketsDispo < nroPlazas) {
				throw new SQLException();
			}
		} catch (SQLException e) {
			con.rollback();
			ticketsDispo = -1;
		} finally {
			ticketsLibres.close();
		}

		return ticketsDispo;
	}

	private int calculaTicketsLibres(int ticketsDispo, int nroPlazas) {
		return ticketsDispo - nroPlazas;
	}
	
	private int existeTicket(int idTicket, Connection con) throws SQLException {
		ResultSet existeTicket = null;
		int ticket = -1;
		try {
			String conExisteTicket = "SELECT cantidad FROM tickets WHERE idTicket = ?";
			PreparedStatement stExisteTicket = con.prepareStatement(conExisteTicket);
			
			stExisteTicket.setInt(1, idTicket);
			
			existeTicket = stExisteTicket.executeQuery();
			
			if (!existeTicket.next()) {
				throw new SQLException();
			}
			
			ticket = existeTicket.getInt(1);
		} catch (SQLException e) {
			con.rollback();
			
		} finally {
			existeTicket.close();
		}
		return ticket;
	}

}
