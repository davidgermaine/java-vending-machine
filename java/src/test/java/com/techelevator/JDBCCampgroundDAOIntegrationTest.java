package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
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

import com.techelevator.view.Campground;
import com.techelevator.view.JDBCCampgroundDAO;
import com.techelevator.view.JDBCParkDAO;
import com.techelevator.view.Park;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCCampgroundDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO parkDAO;
	private JDBCCampgroundDAO campgroundDAO;
	
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
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_save_campground_creates_new_campground() {
		Campground testCampground = new Campground(100, 1, "Test Campsite", "01", "12", 50.00);
		campgroundDAO.saveCampground(testCampground);
		
		boolean foundFlag = false;
		if (campgroundDAO.findCampgroundById(testCampground.getCampgroundId()).getName().equals(testCampground.getName())
				&& campgroundDAO.findCampgroundById(testCampground.getCampgroundId()).getCampgroundId() == testCampground.getCampgroundId()) {
			foundFlag = true;
		}
		assertTrue(foundFlag);
	}
	
	@Test
	public void test_find_campground_by_name_returns_campground_with_name() {
		Campground testCampground = new Campground(100, 1, "Test Campsite", "01", "12", 50.00);
		campgroundDAO.saveCampground(testCampground);
		
		Campground newCampground = null;
		newCampground = campgroundDAO.findCampgroundByName(testCampground.getName());
		assertTrue(testCampground.getParkId() == newCampground.getParkId());
		assertTrue(testCampground.getName().equals(newCampground.getName()));
	}
	
	@Test
	public void test_get_all_campgrounds_returns_all_parks() {
		Park park = parkDAO.findParkById(1);
		List<Campground> campgroundList = campgroundDAO.getAllCampgrounds(park);
		int counter = 0;
		for (Campground campground : campgroundList) {
			counter++;
		}
		assertTrue(counter > 0);
		assertTrue(campgroundList.size() == counter);
	}
	
	@Test
	public void test_delete_removes_campground() {
		Park park = parkDAO.findParkById(1);
		Campground testCampground = new Campground(100, 1, "Test Campsite", "01", "12", 50.00);
		campgroundDAO.saveCampground(testCampground);
		
		int preSize = campgroundDAO.getAllCampgrounds(park).size();
		campgroundDAO.delete(testCampground);
		int postSize = campgroundDAO.getAllCampgrounds(park).size();
		
		assertEquals(preSize - 1, postSize);
	}

}
