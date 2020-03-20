package com.techelevator.view;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
	
	//CREATE
	public void createReservation(Reservation reservation);
	
	//READ
	public Reservation getReservationById(int id);
	public List<Reservation> getReservationsBySite(Site site);
	public boolean isReserved(Site site, LocalDate fromDate, LocalDate toDate);
	public int getNextId();
	
	//UPDATE
	//public void update(Reservation reservation);
	
	//DESTROY
	public void delete(Reservation reservation);

}
