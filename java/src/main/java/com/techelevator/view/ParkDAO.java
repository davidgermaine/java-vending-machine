package com.techelevator.view;

import java.util.List;

public interface ParkDAO {

	// CRUD
	//Create           Class / arguement name
	public void savePark(Park park);
	//Read
	public List<Park> getAllParks();
	public Park findParkById(int id);
	public Park findParkByName(String name);
	//Update
	//public void update(Park park);
	//Destroy
	public void delete(Park park);
	
	
	
	
	
}
