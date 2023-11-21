/*
 * Favour Adah - 30140479
 * Farouq Arafeh - 30258214
 * Adam Attwood - 30185194
 * Lucas Carvalho - 30113633
 * Tiffany Hung - 10149429
 * Mann Patel - 30182233
 * Sneh Patel - 30086076
 * Arumakankani Sayuru Silva - 30190402
 * Jake Waiand - 30179510
 *
 * Description: print receipt class implements a receipt printer observer.
 * i.e. any events related to the receipt printer gets notified here.
 *
 */

package com.thelocalmarketplace.software;

import java.time.LocalDate;
import java.util.ArrayList;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.printer.ReceiptPrinterGold;
import com.jjjwelectronics.printer.ReceiptPrinterSilver;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import powerutility.NoPowerException;

public class PrintReceipt implements ReceiptPrinterListener{

	private final String BRONZE = "BRONZE";
	private final String SILVER = "SILVER";
	private final String GOLD = "GOLD";
	
	private SelfCheckoutStationBronze bronzeStation;
	private SelfCheckoutStationSilver silverStation;
	private SelfCheckoutStationGold goldStation;
	
	private String stationType = "";
	
	//constructors for each station type
	
	public PrintReceipt(SelfCheckoutStationBronze station) {
		this.bronzeStation = station;
		this.stationType = BRONZE;
	}
	
	public PrintReceipt(SelfCheckoutStationSilver station) {
		this.silverStation = station;
		this.stationType = SILVER;
	}
	
	public PrintReceipt(SelfCheckoutStationGold station) {
		this.goldStation = station;
		this.stationType = GOLD;
	}
	
	//Simulates displaying a message to the customer
	public void displayToCustomer(String message) {
		System.out.println("CUSTOMER: "+message);
	}
	
	//Simulates displaying a message to the attendant
	public void displayToAttendant(String message) {
		System.out.println("ATTENDANT: "+message);
	}
	
	//Creates a receipt and prints it (Requires power)
	public void printReceipt(ArrayList<BarcodedProduct> bill, double amountPaid) {
		String receipt = "";
		
		//make receipt
		/* This is roughly how the receipt should look:
		 * TheLocalMarketplace
		 * 
		 * RECEIPT DATE: [current date]
		 * 
		 * SALE:
		 * -[list of products]
		 * 
		 * TOTAL: [total amount paid]
		 */
		LocalDate currentDate = LocalDate.now();
		receipt += "TheLocalMarketplace\n\nRECEIPT DATE: " + currentDate + "\n\nSALE:\n";
		for (BarcodedProduct b : bill) {
			receipt += "-" + b.getDescription() + " - " + b.getPrice() + "\n";
		}
		receipt += "\nTOTAL: " + amountPaid;
		
		//print receipt
		try {
			switch(stationType) {
				case BRONZE:
					for (char c : receipt.toCharArray()) {
						bronzeStation.printer.print(c);
					}
				case SILVER:
					for (char c : receipt.toCharArray()) {
						silverStation.printer.print(c);
					}
				case GOLD:
					for (char c : receipt.toCharArray()) {
						goldStation.printer.print(c);
					}
			}
		} catch(EmptyDevice e) {
			System.out.println(e.getMessage());
		} catch(OverloadedDevice e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Cuts receipt so it can be removable by user. (Requires power)
	public void cutReceipt() {
		try {
			switch(stationType) {
				case BRONZE:
					bronzeStation.printer.cutPaper();
				case SILVER:
					silverStation.printer.cutPaper();
				case GOLD:
					goldStation.printer.cutPaper();
			}
		} catch (NoPowerException e) {
			System.out.println(e.getMessage());
		}
	}

	//Removes the receipt from the printer. Returns the receipt as a String.
	public String removeReceipt() {
		switch(stationType) {
			case BRONZE:
				return bronzeStation.printer.removeReceipt();
			case SILVER:
				return silverStation.printer.removeReceipt();
			case GOLD:
				return goldStation.printer.removeReceipt();
			default:
				return "";
		}
	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		displayToAttendant("The receipt printer is disabled.");
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		displayToAttendant("The receipt printer is off.");
	}

	@Override
	public void thePrinterIsOutOfPaper() {
		displayToCustomer("Error with receipt printer, please wait for attendant.");
		displayToAttendant("Error with receipt printer - out of paper.");
	}

	@Override
	public void thePrinterIsOutOfInk() {
		displayToCustomer("Error with receipt printer, please wait for attendant.");
		displayToAttendant("Error with receipt printer - out of ink.");
	}

	@Override
	public void thePrinterHasLowInk() {
		displayToAttendant("Receipt printer is low on ink.");
	}

	@Override
	public void thePrinterHasLowPaper() {
		displayToAttendant("Receipt printer is low on paper.");
	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		displayToCustomer("Issue resolved.");
		displayToAttendant("Issue resolved.");
	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		displayToCustomer("Issue resolved.");
		displayToAttendant("Issue resolved.");
	}

}
