package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import com.techelevator.view.JDBCParkDAO;
import com.techelevator.view.Park;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCParkDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO parkDAO;
	//private JDBCCampgroundDAO campgroundDAO;
	//private JDBCSiteDAO siteDAO;
	//private JDBCReservationDAO reservationDAO;
	
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_save_park_creates_new_park() {
		Park testPark = new Park(100, "Test Park", "Michigan", LocalDate.parse("2020-02-21"), 4, 21,
				"A park located entirely in the Tech Town building, 3rd floor.");
		parkDAO.savePark(testPark);
		
		boolean foundFlag = false;
		if (parkDAO.findParkById(testPark.getParkId()).getName().equals(testPark.getName()) &&
				parkDAO.findParkById(testPark.getParkId()).getParkId() == testPark.getParkId()) {
			foundFlag = true;
		}
		assertTrue(foundFlag);
	}

	@Test
	public void test_delete_removes_park() {
		Park testPark = new Park(100, "Test Park", "Michigan", LocalDate.parse("2020-02-21"), 4, 21,
				"A park located entirely in the Tech Town building, 3rd floor.");
		parkDAO.savePark(testPark);
		
		int preSize = parkDAO.getAllParks().size();
		parkDAO.delete(testPark);
		int postSize = parkDAO.getAllParks().size();
		
		assertEquals(preSize - 1, postSize);
	}

	@Test
	public void test_get_all_parks_returns_all_parks() {
		List<Park> parkList = parkDAO.getAllParks();
		int counter = 0;
		for (Park park : parkList) {
			counter++;
		}
		assertTrue(counter > 0);
		assertTrue(parkList.size() == counter);
	}

	@Test
	public void test_find_park_by_name_returns_park_with_name() {
		Park testPark = new Park(100, "Test Park", "Michigan", LocalDate.parse("2020-02-21"), 4, 21,
				"A park located entirely in the Tech Town building, 3rd floor.");
		parkDAO.savePark(testPark);
		
		Park newPark = null;
		newPark = parkDAO.findParkByName(testPark.getName());
		assertTrue(testPark.getParkId() == newPark.getParkId());
		assertTrue(testPark.getName().equals(newPark.getName()));
	}

}