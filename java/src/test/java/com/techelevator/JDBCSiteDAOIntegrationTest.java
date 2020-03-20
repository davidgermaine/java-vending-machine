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
import com.techelevator.view.JDBCSiteDAO;
import com.techelevator.view.Park;
import com.techelevator.view.Site;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCSiteDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO siteDAO;
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
		siteDAO = new JDBCSiteDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_save_site_creates_new_site() {
		Site testSite = new Site(1000, 1, 1000, 8, true, 20, true);
		siteDAO.saveSite(testSite);
		
		boolean foundFlag = false;
		if (siteDAO.findSiteById(testSite.getSiteId()).getSiteId() == testSite.getSiteId()
				&& siteDAO.findSiteById(testSite.getSiteId()).getSiteNumber() == testSite.getSiteNumber()) {
			foundFlag = true;
		}
		assertTrue(foundFlag);
	}
	
	@Test
	public void test_get_all_sites_returns_all_sites() {
		Campground campground = campgroundDAO.findCampgroundById(1);
		List<Site> siteList = siteDAO.getAllSites(campground);
		int counter = 0;
		for (Site site : siteList) {
			counter++;
		}
		assertTrue(counter > 0);
		assertTrue(siteList.size() == counter);
	}
	
	@Test
	public void test_delete_removes_site() {
		Campground campground = campgroundDAO.findCampgroundById(1);
		Site testSite = new Site(1000, 1, 1000, 8, true, 20, true);
		siteDAO.saveSite(testSite);
		
		int preSize = siteDAO.getAllSites(campground).size();
		siteDAO.delete(testSite);
		int postSize = siteDAO.getAllSites(campground).size();
		
		assertEquals(preSize - 1, postSize);
	}

}
