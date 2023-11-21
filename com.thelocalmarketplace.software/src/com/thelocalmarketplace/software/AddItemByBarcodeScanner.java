/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScanner;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

public class AddItemByBarcodeScanner implements BarcodeScannerListener {
	public static double expectedPrice = 0;
	public static double expectedWeight = 0;

	public AddItemByBarcodeScanner(SelfCheckoutStation selfCheckoutStation) {
		selfCheckoutStation.scanner.register(this);
		// Initialize any other necessary components.
	}

	public void startCustomerSession() {
		// Logic to start a new customer session.
		State.setSession(true);
	}

	public void endCustomerSession() {
		// Logic to end the current customer session.
		State.setSession(false);
	}

	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner scanner, Barcode barcode) {
		if (State.isSession()) {
			try {
				// 1. System: Detects a barcode.
				Barcode scannedBarcode = barcode;

				// 2. System: Determines the characteristics (weight and cost) of the product
				State.setSession(false);

				// associated with the barcode.
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(scannedBarcode);

				if (product != null) {
					// 3. System: Updates the expected weight from the bagging area.
					expectedWeight += product.getExpectedWeight();

					// 4. System: Updates the expected price.
					expectedPrice += product.getPrice();

					// 5. System: Signals to the Customer to place the scanned item in the bagging
					// area.
					System.out
							.println("Please place the item with barcode " + scannedBarcode + " in the bagging area.");

					// 6. System: Signals to the System that the weight has changed.

					// 7. System: Unblocks the station.

					State.setSession(true);
				} else {
					// Handle invalid barcode scenario (product not found)
					handleInvalidBarcode(new Exception("Product not found in database"));
				}
			} catch (Exception e) {
				// Handle invalid barcode scenario (unable to parse barcode)
				handleInvalidBarcode(e);
			}
		} else {
			// 2. An item is scanned when a customer session is not in progress.
			// The scanned information will simply be ignored.
			ignoreScannedItem();
		}
	}

	private void ignoreScannedItem() {
		System.out.println("Scanned item ignored. No customer session in progress.");
	}

	private void handleInvalidBarcode(Exception e) {
		System.out.println("Invalid barcode scanned: " + e.getMessage());
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
}