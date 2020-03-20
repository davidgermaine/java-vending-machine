package com.techelevator.view;

import java.time.LocalDate;

public class Park {

	private int parkId;
	private String name;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int vistors;
	private String description;

	public Park(int parkId, String name, String location, LocalDate establishDate, int area, int vistors,
			String description) {
		this.parkId = parkId;
		this.name = name;
		this.location = location;
		this.establishDate = establishDate;
		this.area = area;
		this.vistors = vistors;
		this.description = description;
	}

	public Park() {
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getVistors() {
		return vistors;
	}

	public void setVistors(int vistors) {
		this.vistors = vistors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
