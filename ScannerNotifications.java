package com.thelocalmarketplace.software;

import java.util.HashMap;
import java.util.Map;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;


public class ScannerNotifications implements BarcodeScannerListener {
	
	Session session = new Session();

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scanner enabled");
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scanner disabled");
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scanner turned on");
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scanner turned off");
		
	}

	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		// if session state == SESSION_INACTIVE || PAY_FOR_ORDER,
		// ignore scanned barcode (do nothing)
		// otherwise:
		if (session.getSessionState() == Session.ORDER_EMPTY 
				|| session.getSessionState() == Session.ORDER_NOT_EMPTY) {
			
			// if checkout state == BLOCK -> ignore scan
			// otherwise: 
			if (session.getCheckoutState() == Session.UNBLOCK) {
				// block checkout from further customer interactions
				session.blockCheckout();
				
				// get barcoded product from scanned barcode
				BarcodedProduct barcodedProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				
				// add price of barcoded product to total price
				session.addToTotalPrice(barcodedProduct);
				
				// add expected weight of barcoded product to total expected weight
				session.addToTotalExpectedWeight(barcodedProduct);
				
				// signal to customer to place item in bagging area
				System.out.println("Please place the scanned item in the bagging area.");
			}
			
			
		}
	}

		

}
