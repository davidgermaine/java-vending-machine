package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Campground;
import com.techelevator.view.CampgroundDAO;
import com.techelevator.view.JDBCCampgroundDAO;
import com.techelevator.view.JDBCParkDAO;
import com.techelevator.view.JDBCReservationDAO;
import com.techelevator.view.JDBCSiteDAO;
import com.techelevator.view.Menu;
import com.techelevator.view.Park;
import com.techelevator.view.ParkDAO;
import com.techelevator.view.Reservation;
import com.techelevator.view.ReservationDAO;
import com.techelevator.view.Site;
import com.techelevator.view.SiteDAO;

public class CampgroundCLI {
	
	private static final String PARK_MENU_VIEW_CAMPGROUNDS = "View campgrounds inside of park";
	private static final String PARK_MENU_SEARCH_RESERVATION = "Search for a reservation";
	private static final String PARK_MENU_PREVIOUS_MENU = "Return to previous menu";
	private static final String[] PARK_MENU_OPTIONS = { PARK_MENU_VIEW_CAMPGROUNDS, PARK_MENU_SEARCH_RESERVATION, PARK_MENU_PREVIOUS_MENU };
	
	private static final String CAMPSITE_MENU_SEARCH_RESERVATION = "Search for a reservation";
	private static final String CAMPSITE_MENU_PREVIOUS_MENU = "Return to previous menu";
	private static final String[] CAMPSITE_MENU_OPTIONS = { CAMPSITE_MENU_SEARCH_RESERVATION, CAMPSITE_MENU_PREVIOUS_MENU };
	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		
		parkDAO = new JDBCParkDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		siteDAO	= new JDBCSiteDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		this.menu = new Menu(System.in, System.out);
	}

	public void run() {
		while(true) {
			System.out.println("View Parks Interface\n============================================");
			System.out.println("Select a park for further details:");
			
			List<Park> parkList = parkDAO.getAllParks();
			String[] parkNameArray = new String[parkList.size() + 1];
			
			for (int i = 0; i < parkList.size(); i++) {
				parkNameArray[i] = parkList.get(i).getName();
			}
			parkNameArray[parkNameArray.length - 1] = "Quit";
			
			String choice = (String)menu.getChoiceFromOptions(parkNameArray);
			for (int i = 0; i < parkList.size(); i++) {
				if (choice.equals(parkNameArray[parkNameArray.length - 1])) {
					System.out.println("Quitting program...");
					System.exit(0);
				} else if (choice.contentEquals(parkList.get(i).getName())) {
					Park userPark = parkDAO.findParkByName(choice);
					System.out.println("\nRetrieving information for " + choice + " Park...");
					runParkInformation(userPark);
				}
			}
		}
	}
	
	public void runParkInformation(Park park) {
		System.out.println("\n" + park.getName() + " Park");
		System.out.printf("\n%-20s %20s", "Location:", park.getLocation());
		System.out.printf("\n%-20s %20s", "Established:", park.getEstablishDate());
		System.out.printf("\n%-20s %20s", "Area", park.getArea() + " sq km");
		System.out.printf("\n%-20s %20s", "Annual Visitors", park.getVistors());
		System.out.println("\n");
		
		// Function to print out description in discrete lines
		String[] descriptionArray = park.getDescription().split(" ");
		for (int i = 0; i < descriptionArray.length; i++) {
			System.out.print(descriptionArray[i] + " ");
			if (i % 15 == 14) {
				System.out.print("\n");
			}
		}
		System.out.println("\n\nSelect a command:");
		
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if (choice.equals(PARK_MENU_VIEW_CAMPGROUNDS)) {
			System.out.println("\nRetrieving campground information for " + park.getName() + " Park...\n");
			runCampgroundInformation(park);
		} else if (choice.equals(PARK_MENU_SEARCH_RESERVATION)) {
			System.out.println("\nInitializing reservation system...\n");
			runReservationSelection(park);
		} else if (choice.equals(PARK_MENU_PREVIOUS_MENU)) {
			System.out.println("\nReturning to main menu...\n");
		}
	}
	
	public void runCampgroundInformation(Park park) {
		System.out.println("\n" + park.getName() + " Park Campgrounds\n");
		System.out.printf("%-15s %-35s %-15s %-15s %-10s\n", "Campground #", "Name", "Open (mm)", "Close (mm)", "Daily Fee");
	
		List<Campground> campgroundList = campgroundDAO.getAllCampgrounds(park);
		for (Campground campground : campgroundList) {

			System.out.printf("%-15s %-35s %-15s %-15s $%-10.2f\n", campground.getCampgroundId(), campground.getName(),
					campground.getOpenMonth(), campground.getCloseMonth(), campground.getDailyFee());
		}
		
		String choice = (String)menu.getChoiceFromOptions(CAMPSITE_MENU_OPTIONS);
		if (choice.equals(CAMPSITE_MENU_SEARCH_RESERVATION)) {
			System.out.println("\nInitializing reservation system...\n");
			runReservationSelection(park);
		} else if (choice.equals(CAMPSITE_MENU_PREVIOUS_MENU)) {
			System.out.println("\nReturning to previous menu...\n");
			runParkInformation(park);
		}
	}
	
	public void runReservationSelection(Park park) {
		System.out.println("\n" + park.getName() + " Park Campgrounds\n");
		System.out.printf("%-15s %-35s %-15s %-15s %-10s\n", "Campground #", "Name", "Open (mm)", "Close (mm)", "Daily Fee");
	
		List<Campground> campgroundList = campgroundDAO.getAllCampgrounds(park);
		for (Campground campground : campgroundList) {

			System.out.printf("%-15s %-35s %-15s %-15s $%-10.2f\n", campgroundList.indexOf(campground) + 1, campground.getName(),
					campground.getOpenMonth(), campground.getCloseMonth(), campground.getDailyFee());
		}
		
		Scanner input = new Scanner(System.in);
		Campground campground = null;
		while (true) {
			System.out.println("\nWhich campground (enter 0 to cancel)?: ");
			int number;
			try {
				number = Integer.parseInt(input.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("\nInvalid selection\n");
				continue;
			}
			if (number == 0) {
				System.out.println("\nReturning to main menu...\n\n");
				break;
			}
			try {
				campground = campgroundList.get(number - 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("\nThe selected campground does not exist at this park, please try again.\n");
				continue;
			}
			System.out.println("\nYou have selected " + campground.getName() + "...\n\n");
			runReservationCheck(campground);
			break;
		}
	}
	
	public void runReservationCheck(Campground campground) {
		Scanner input2 = new Scanner(System.in);
		LocalDate fromDate = null;
		LocalDate toDate = null;
		LocalDate currentDate = LocalDate.now();
		while (fromDate == null) {
			System.out.println("Arrival date (YYYY-MM-DD): ");
			String fromDateString = input2.nextLine();
			try {
				fromDate = LocalDate.parse(fromDateString);
				if (fromDate.compareTo(currentDate) < 0) {
					System.out.println("Cannot reserve campsite for a past date!\n");
					fromDate = null;
				} else if (Integer.parseInt(fromDateString.substring(5, 7)) < Integer.parseInt(campground.getOpenMonth())
						|| Integer.parseInt(fromDateString.substring(5, 7)) >= Integer.parseInt(campground.getCloseMonth())) {
					System.out.println("The selected campground is closed for the season during the selected time, "
							+ "please enter another date.");
					fromDate = null;
				}
			} catch (DateTimeParseException e) {
				System.out.println("Entered date does not match the required format...\n");
			}
		}
		while (toDate == null) {
			System.out.println("Depature date (YYYY-MM-DD): ");
			String toDateString = input2.nextLine();
			try {
				toDate = LocalDate.parse(toDateString);
				if (toDate.compareTo(fromDate) <= 0) {
					System.out.println("Reservations require staying at least one night!\n");
					toDate = null;
				} else if (Integer.parseInt(toDateString.substring(5, 7)) < Integer.parseInt(campground.getOpenMonth())
						|| Integer.parseInt(toDateString.substring(5, 7)) >= Integer.parseInt(campground.getCloseMonth())) {
					System.out.println("The selected campground is closed for the season during the selected time, "
							+ "please enter another date.");
					toDate = null;
				}
			} catch (DateTimeParseException e) {
				System.out.println("Entered date does not match the required format...\n");
			}
		}
		
		List<Site> siteList = siteDAO.getAllSites(campground);
		List<Site> availableSites = new ArrayList<>();
		for (Site site : siteList) {
			boolean isReserved = reservationDAO.isReserved(site, fromDate, toDate);
			if (isReserved == false) {
				availableSites.add(site);
			}
		}
		if (availableSites.size() <= 0) {
			System.out.println("The selected campground has no available reservations for the selected dates.");
		} else {
			runMakeReservation(availableSites, fromDate, toDate);
		}
	}
	
	public void runMakeReservation(List<Site> siteList, LocalDate fromDate, LocalDate toDate) {
		int days = (int)ChronoUnit.DAYS.between(fromDate, toDate);
		System.out.println("Results matching your search criteria:\n\n");
		System.out.printf("%-10s %-10s %-10s %-15s %-15s %-10s %-10s\n", "Option #", "Site No.", "Max Occup.", "Accessible?",
				"Max RV Length", "Utility", "Cost");
		if (siteList.size() > 5) {
			for (int i = 0; i < 5; i++) {
				Site site = siteList.get(i);
				System.out.printf("%-10s %-10s %-10s %-15s %-15s %-10s $%-10.2f\n", siteList.indexOf(site) + 1, 
						site.getSiteId(), site.getMaxOccupancy(), 
						site.isAccesible(), site.getMaxRvLength(), site.isUtilities(), 
						campgroundDAO.findCampgroundById(site.getCampgroundId()).getDailyFee() * days);
			}
		} else {
			for (int i = 0; i < siteList.size(); i++) {
				Site site = siteList.get(i);
				System.out.printf("%-10s %-10s %-10s %-15s %-15s %-10s $%-10.2f\n", siteList.indexOf(site) + 1, 
						site.getSiteId(), site.getMaxOccupancy(), 
						site.isAccesible(), site.getMaxRvLength(), site.isUtilities(), 
						campgroundDAO.findCampgroundById(site.getCampgroundId()).getDailyFee() * days);
			}
		}
		
		Scanner input3 = new Scanner(System.in);
		while (true) {
			System.out.println("\nEnter Option # of the site you would like to reserve: ");
			int siteNum;
			try {
				siteNum = Integer.parseInt(input3.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input detected.\n");
				continue;
			}
			if (siteNum == 0) {
				System.out.println("Returning to main menu...");
				break;
			}
			Site site = null;
			try {
				site = siteList.get(siteNum - 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Not a valid option.\n");
			}
			System.out.println("Enter a name to hold the reservation under: ");
			String name = input3.nextLine();
			
			
			int id = reservationDAO.getNextId();
			LocalDate currentDate = LocalDate.now();
			Reservation reservation = new Reservation(id, site.getSiteId(), name, fromDate, toDate, currentDate);
			reservationDAO.createReservation(reservation);
			
			System.out.println("A reservation for " + name + " has been made.  The reservation id is " + id + ".");
			System.out.println("Returning to main menu...\n\n");
			break;
		}
	}
}
