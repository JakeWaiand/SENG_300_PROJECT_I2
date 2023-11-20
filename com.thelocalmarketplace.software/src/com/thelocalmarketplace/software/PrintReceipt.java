/*
 * @author Jake Waiand (30179510)
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

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import powerutility.NoPowerException;

public class PrintReceipt implements ReceiptPrinterListener{

	public void displayToCustomer(String message) {
		System.out.println("CUSTOMER: "+message);
	}
	
	public void displayToAttendant(String message) {
		System.out.println("ATTENDANT: "+message);
	}
	
	public void printReceipt(ArrayList<BarcodedProduct> bill, double amountPaid, String printerGrade) {
		String receipt = "";
		
		//make receipt
		LocalDate currentDate = LocalDate.now();
		receipt += "TheLocalMarketplace\n\nRECEIPT DATE: " + currentDate + "\n\nSALE:\n";
		for (BarcodedProduct b : bill) {
			receipt += "-" + b.getDescription() + " - " + b.getPrice() + "\n";
		}
		receipt += "\nTOTAL: " + amountPaid;
		
		//printing the receipt
		try {
			switch(printerGrade) {
				case "BRONZE":
					ReceiptPrinterBronze printerB = new ReceiptPrinterBronze();
					for (char c : receipt.toCharArray()) {
						printerB.print(c);
					}
					printerB.cutPaper();
					System.out.println(printerB.removeReceipt());
				case "GOLD":
					ReceiptPrinterGold printerG = new ReceiptPrinterGold();
					for (char c : receipt.toCharArray()) {
						printerG.print(c);
					}
					printerG.cutPaper();
					System.out.println(printerG.removeReceipt());
				case "SILVER":
					ReceiptPrinterSilver printerS = new ReceiptPrinterSilver();
					for (char c : receipt.toCharArray()) {
						printerS.print(c);
					}
					printerS.cutPaper();
					System.out.println(printerS.removeReceipt());
			}
		} catch (OverloadedDevice e) {
			System.out.println(e.getMessage());
			
		} catch (EmptyDevice e) {
			System.out.println(e.getMessage());
			
		} catch (NoPowerException e) {
			System.out.println(e.getMessage());
			
		} catch (InvalidArgumentSimulationException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
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
