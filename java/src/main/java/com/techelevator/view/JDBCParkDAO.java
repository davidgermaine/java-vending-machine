package com.techelevator.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void savePark(Park park) {
		String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sqlInsertPark, park.getParkId(), park.getName(), park.getLocation(),
				park.getEstablishDate(), park.getArea(), park.getVistors(), park.getDescription());
	}

	@Override
	public Park findParkById(int id) {
		// empty object
		Park park = null;
		// sql command
		String sqlFindParkById = "select * from park where park_id = ?";
		// rowset runs sql command
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParkById, id);
		// loop - maprowto park is a method, returning the park object
		while (results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(LocalDate.parse(results.getString("establish_date")));
		park.setArea(results.getInt("area"));
		park.setVistors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;
	}

	@Override
	public void delete(Park park) {
		String sqlDeletePark = "delete from park where park_id = ? and name = ?";
		jdbcTemplate.update(sqlDeletePark, park.getParkId(), park.getName());
		
	}
	
	@Override
	public List<Park> getAllParks() {
		String sqlReadAllParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReadAllParks);
		
		List<Park> parkList = new ArrayList<>();
		while (results.next()) {
			Park park = mapRowToPark(results);
			parkList.add(park);
		}
		return parkList;
	}

	@Override
	public Park findParkByName(String name) {
		String sqlFindParkByName = "Select * FROM park WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParkByName, name);
		
		Park park = null;
		if (results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}

}
