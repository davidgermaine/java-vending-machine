package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.view.JDBCReservationDAO;
import com.techelevator.view.JDBCSiteDAO;
import com.techelevator.view.Reservation;
import com.techelevator.view.Site;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCReservationDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO siteDAO;
	private JDBCReservationDAO reservationDAO;
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_create_reservation_creates_new_reservation() {
		Reservation testReservation = new Reservation(1000, 1, "Test Reservation", LocalDate.parse("3000-06-06"), LocalDate.parse("3000-06-10"), LocalDate.now());
		reservationDAO.createReservation(testReservation);
		
		boolean foundFlag = false;
		if (reservationDAO.getReservationById(testReservation.getReservationId()).getReservationId() == testReservation.getReservationId()
				&& reservationDAO.getReservationById(testReservation.getReservationId()).getName().equals(testReservation.getName())) {
			foundFlag = true;
		}
		assertTrue(foundFlag);
	}
	
	@Test
	public void test_get_reservations_by_site_returns_all_reservations_at_site() {
		Site site = siteDAO.findSiteById(1);
		List<Reservation> reservationList = reservationDAO.getReservationsBySite(site);
		int counter = 0;
		for (Reservation reservation : reservationList) {
			counter++;
		}
		assertTrue(counter > 0);
		assertTrue(reservationList.size() == counter);
	}
	
	@Test
	public void test_that_entering_reserved_site_and_time_returns_is_reserved() {
		Reservation reservation = reservationDAO.getReservationById(1);
		Site site = siteDAO.findSiteById(reservation.getSiteId());
		assertTrue(reservationDAO.isReserved(site, reservation.getFromDate(), reservation.getToDate()));
	}
	
	@Test
	public void test_that_entering_unreserved_site_and_time_returns_is_reserved() {
		Reservation reservation = new Reservation(1000, 1, "Test Reservation", LocalDate.parse("3000-06-06"), LocalDate.parse("3000-06-10"), LocalDate.now());
		Site site = siteDAO.findSiteById(reservation.getSiteId());
		assertFalse(reservationDAO.isReserved(site, reservation.getFromDate(), reservation.getToDate()));
	}
	
	@Test
	public void test_delete_removes_reservation() {
		Reservation reservation = new Reservation(1000, 1, "Test Reservation", LocalDate.parse("3000-06-06"), LocalDate.parse("3000-06-10"), LocalDate.now());
		Site testSite = siteDAO.findSiteById(reservation.getSiteId());
		reservationDAO.createReservation(reservation);
		
		int preSize = reservationDAO.getReservationsBySite(testSite).size();
		reservationDAO.delete(reservation);
		int postSize = reservationDAO.getReservationsBySite(testSite).size();
		
		assertEquals(preSize - 1, postSize);
	}
	
	@Test
	public void test_next_id_returns_next_available_id() {
		String sqlFindMaxId = "SELECT * FROM reservation ORDER BY reservation_id DESC LIMIT 1";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindMaxId);
		
		int id = 0;
		while (results.next()) {
			id = results.getInt("reservation_id");
		}
		
		int nextId = reservationDAO.getNextId();
		assertEquals(id + 1, nextId);
	}

}
