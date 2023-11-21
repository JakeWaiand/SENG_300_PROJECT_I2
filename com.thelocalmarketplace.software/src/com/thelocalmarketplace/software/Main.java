/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package com.thelocalmarketplace.software;

import java.util.Map;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import powerutility.PowerGrid;

public class Main {
	public static void initialize(Map<PriceLookUpCode, PLUCodedProduct> PLU_PRODUCT_DATABASE, 
			Map<Barcode, BarcodedProduct> BARCODED_PRODUCT_DATABASE,
			Map<Product, Integer> INVENTORY) {

		// initialize use cases with the scs of interest
		SelfCheckoutStation scs = new SelfCheckoutStation();
		AddItemByBarcodeScanner aibs = new AddItemByBarcodeScanner(scs);
		PayViaCash pvc = new PayViaCash(scs);
		WeightDiscrepancy wd = new WeightDiscrepancy(scs);

		PowerGrid pg = PowerGrid.instance();
		// what type of power source should this be? probably the below is purely for
		// when testing
//		pg.engageUninterruptiblePowerSource();

		// plug in + turn on scs
		scs.plugIn(pg);
		scs.turnOn();

		// currently no means to 'end' a session other than paying the full amount

		
		ProductDatabases.PLU_PRODUCT_DATABASE.putAll(PLU_PRODUCT_DATABASE);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.putAll(BARCODED_PRODUCT_DATABASE);
		ProductDatabases.INVENTORY.putAll(INVENTORY);
		
		Main.startSession();


	}

	/**
	 * Starts the session of the SelfCheckoutStation State is the static instance
	 * that determines state of session. If it's already in session, does nothing,
	 * If its not yet in session, session started by setting true in State class
	 */
	public static void startSession() {
		// if session already started
		if (State.isSession()) {
			return;
		}
		// else set session to start (true)
		State.setSession(true);
	}

}
