package com.techelevator.view;

public class Campground {
	
	private int campgroundId;
	private int parkId;
	private String name;
	private String openMonth;
	private String closeMonth;
	private double dailyFee;
	
	public Campground(int campgroundId, int parkId, String name, String openMonth, String closeMonth, double dailyFee) {
		this.campgroundId = campgroundId;
		this.parkId = parkId;
		this.name = name;
		this.openMonth = openMonth;
		this.closeMonth = closeMonth;
		this.dailyFee = dailyFee;
	}

	public Campground() {
	}

	public int getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
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

	public String getOpenMonth() {
		return openMonth;
	}

	public void setOpenMonth(String openMonth) {
		this.openMonth = openMonth;
	}

	public String getCloseMonth() {
		return closeMonth;
	}

	public void setCloseMonth(String closeMonth) {
		this.closeMonth = closeMonth;
	}

	public double getDailyFee() {
		return dailyFee;
	}

	public void setDailyFee(double dailyFee) {
		this.dailyFee = dailyFee;
	}

}
