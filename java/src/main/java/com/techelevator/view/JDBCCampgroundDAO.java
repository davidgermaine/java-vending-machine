package com.techelevator.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	// CREATE
	@Override
	public void saveCampground(Campground campground) {
		String sqlSaveCampground = "INSERT INTO campground VALUES(?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlSaveCampground, campground.getCampgroundId(), campground.getParkId(),
				campground.getName(), campground.getOpenMonth(), campground.getCloseMonth(), BigDecimal.valueOf(campground.getDailyFee()));
	}
	
	// READ
	@Override
	public List<Campground> getAllCampgrounds(Park park) {
		int parkId = park.getParkId();
		String sqlReadParkCampgrounds = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReadParkCampgrounds, parkId);
		
		List<Campground> campgroundList = new ArrayList<>();
		while (results.next()) {
			Campground campground = mapRowToCampground(results);
			campgroundList.add(campground);
		}
		return campgroundList;
	}
	
	@Override
	public Campground findCampgroundById(int id) {
		String sqlFindCampgroundById = "SELECT * FROM campground WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundById, id);
		
		Campground campground = null;
		if (results.next()) {
			campground = mapRowToCampground(results);
		}
		return campground;
	}
	
	@Override
	public Campground findCampgroundByName(String name) {
		String sqlFindCampgroundById = "SELECT * FROM campground WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundById, name);
		
		Campground campground = null;
		if (results.next()) {
			campground = mapRowToCampground(results);
		}
		return campground;
	}
	
	// DESTROY
	@Override
	public void delete(Campground campground) {
		String sqlDeleteCampground = "DELETE FROM campground WHERE campground_id = ? AND name = ?";
		jdbcTemplate.update(sqlDeleteCampground, campground.getCampgroundId(), campground.getName());
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setCampgroundId(results.getInt("campground_id"));
		campground.setParkId(results.getInt("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpenMonth(results.getString("open_from_mm"));
		campground.setCloseMonth(results.getString("open_to_mm"));
		campground.setDailyFee(results.getDouble("daily_fee"));
		return campground;
	}

}
