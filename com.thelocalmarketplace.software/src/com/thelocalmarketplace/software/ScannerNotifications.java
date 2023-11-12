package com.thelocalmarketplace.software;

import java.util.Map;

import com.jjjwelectronics.*;
import com.jjjwelectronics.scanner.*;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;


public class ScannerNotifications implements BarcodeScannerListener {

    Session session = new Session();
    public Map<Barcode, BarcodedProduct> database = ProductDatabases.BARCODED_PRODUCT_DATABASE;

    public ScannerNotifications() {

    }

    @Override
    public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {

        /* TODO:
            - Implement block/unblock session
         */

        session.startAdding();

        if (session.getSessionState() == Session.ADDING_ITEM) {

            BarcodedProduct product = database.get(barcode);

//            BarcodedItem item = new BarcodedItem(product.getBarcode(), new Mass(product.getExpectedWeight()));

            if (product != null) {
                session.record.addToBill(product);
                session.record.addToTotalPrice(product);
                session.addToTotalExpectedWeight(product);

                // signal to customer to place item in bagging area
                System.out.println("Please place the scanned item in the bagging area.");

                System.out.println(product.getDescription() + " was added to bagging area");

                session.doneAdding();

            } else {
                System.out.println("No such product in database");
            }
        }

    }

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

}