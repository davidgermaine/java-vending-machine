package com.techelevator.view;

import java.util.List;

public interface CampgroundDAO {
	
	// CREATE
	public void saveCampground(Campground campground);
	
	// READ
	public List<Campground> getAllCampgrounds(Park park);
	public Campground findCampgroundById(int id);
	public Campground findCampgroundByName(String name);
	//public List<Campground> findCampgroundByOpenMonth(String openMonth);
	//public List<Campground> findCampgroundByCloseMonth(String closeMonth);
	
	// UPDATE
	//public void update(Campground campground);
	
	// DESTROY
	public void delete(Campground campground);

}
