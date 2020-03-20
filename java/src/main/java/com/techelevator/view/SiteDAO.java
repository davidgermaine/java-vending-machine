package com.techelevator.view;

import java.util.List;

public interface SiteDAO {
	
	//CREATE
	public void saveSite(Site site);
	
	//READ
	public List<Site> getAllSites(Campground campground);
	public Site findSiteById(int id);
	
	//UPDATE
	//public void update(Site site);
	
	//DESTROY
	public void delete(Site site);
}
