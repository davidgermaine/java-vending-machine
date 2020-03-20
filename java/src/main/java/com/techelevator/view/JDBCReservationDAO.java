package com.techelevator.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//CREATE
	@Override
	public void createReservation(Reservation reservation) {
		String sqlCreateReservation = "INSERT INTO reservation(reservation_id, site_id, name, "
				+ "from_date, to_date, create_date) VALUES(?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateReservation, reservation.getReservationId(), reservation.getSiteId(),
				reservation.getName(), reservation.getFromDate(), reservation.getToDate(), reservation.getCreateDate());
	}
	
	//READ
	@Override
	public Reservation getReservationById(int id) {
		String sqlFindReservationById = "SELECT * FROM reservation WHERE reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationById, id);
		
		Reservation reservation = null;
		while (results.next()) {
			reservation = mapRowToReservation(results);
		}
		return reservation;
	}
	
	@Override
	public List<Reservation> getReservationsBySite(Site site) {
		String sqlFindReservationById = "SELECT * FROM reservation WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationById, site.getSiteId());
		
		List<Reservation> reservationList = new ArrayList<>();
		while (results.next()) {
			Reservation reservation = mapRowToReservation(results);
			reservationList.add(reservation);
		}
		return reservationList;
	}
	
	@Override
	public boolean isReserved(Site site, LocalDate fromDate, LocalDate toDate) {
		String sqlFindReservationById = "SELECT * FROM reservation WHERE site_id = ? AND from_date BETWEEN ? AND ? "
				+ "OR site_id = ? AND to_date BETWEEN ? AND ? "
				+ "OR site_id = ? AND ? BETWEEN from_date AND to_date "
				+ "OR site_id = ? AND ? BETWEEN from_date AND to_date";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationById, 
				site.getSiteId(), fromDate, toDate,
				site.getSiteId(), fromDate, toDate,
				site.getSiteId(), fromDate,
				site.getSiteId(), toDate);
		
		List<Reservation> reservationList = new ArrayList<>();
		while (results.next()) {
			Reservation bookedReservation = mapRowToReservation(results);
			reservationList.add(bookedReservation);
		}
		if (reservationList.size() > 0) {
			return true;
		}
		return false;
	}
	
	//DESTROY
	@Override
	public void delete(Reservation reservation) {
		String sqlDeleteReservation = "DELETE FROM reservation WHERE reservation_id = ? AND site_id = ? AND name = ?";
		jdbcTemplate.update(sqlDeleteReservation, reservation.getReservationId(), reservation.getSiteId(),
				reservation.getName());
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(LocalDate.parse(results.getString("from_date")));
		reservation.setToDate(LocalDate.parse(results.getString("to_date")));
		reservation.setCreateDate(LocalDate.parse(results.getString("create_date")));
		return reservation;
	}
	
	public int getNextId() {
		String sqlFindMaxId = "SELECT * FROM reservation ORDER BY reservation_id DESC LIMIT 1";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindMaxId);
		
		int id = 0;
		while (results.next()) {
			id = results.getInt("reservation_id") + 1;
		}
		return id;
	}

}
