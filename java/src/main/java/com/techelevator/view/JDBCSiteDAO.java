package com.techelevator.view;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//CREATE
	@Override
	public void saveSite(Site site) {
		String sqlCreateSite = "INSERT INTO site VALUES(?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateSite, site.getSiteId(), site.getCampgroundId(), site.getSiteNumber(),
				site.getMaxOccupancy(), site.isAccesible(), site.getMaxRvLength(), site.isUtilities());
	}
	
	//READ
	@Override
	public List<Site> getAllSites(Campground campground) {
		String sqlGetAllSites = "SELECT * FROM site WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites, campground.getCampgroundId());
		
		List<Site> siteList = new ArrayList<>();
		while (results.next()) {
			Site site = mapRowToSite(results);
			siteList.add(site);
		}
		return siteList;
	}
	
	@Override
	public Site findSiteById(int id) {
		String sqlFindSiteById = "SELECT * FROM site WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindSiteById, id);
		
		Site site = null;
		if (results.next()) {
			site = mapRowToSite(results);
		}
		return site;
	}
	
	//DESTROY
	@Override
	public void delete(Site site) {
		String sqlDeleteSite = "DELETE FROM site WHERE site_id = ? and campground_id = ? and site_number = ?";
		jdbcTemplate.update(sqlDeleteSite, site.getSiteId(), site.getCampgroundId(), site.getSiteNumber());
	}
	
	private Site mapRowToSite(SqlRowSet results) {
		Site site = new Site();
		site.setSiteId(results.getInt("site_id"));
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccesible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		return site;
	}

}
