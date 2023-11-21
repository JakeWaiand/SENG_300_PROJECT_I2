/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package testAddItem;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScanner;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItemByBarcodeScanner;
import com.thelocalmarketplace.software.State;

import powerutility.PowerGrid;

public class TestAddItemByBarcode {
    private BarcodedProduct bpWithInvalidBarcode;
    private Barcode b2;
    private BarcodedProduct barcodedProduct;
    private BarcodedItem barcodedItem;
    private BarcodedItem invalidBarcodedItem;
    private Barcode b1;
    private BarcodeScanner scanner;
    private final SelfCheckoutStation scs = new SelfCheckoutStation();
    private final int price = 5;
    private final int weight = 1;
    AddItemByBarcodeScanner aibs;

    @Before
    public void setup() {
        PowerGrid pg = PowerGrid.instance();
        // what type of power source should this be? probably the below is purely for
        // when testing
        PowerGrid.engageUninterruptiblePowerSource();

        // plug in + turn on scs
        scs.plugIn(pg);
        scs.turnOn();
        scanner = scs.scanner;
        aibs = new AddItemByBarcodeScanner(scs);
        b1 = new Barcode(new Numeral[] { Numeral.one });
        barcodedProduct = new BarcodedProduct(b1, "Apple", price, weight);
        b2 = new Barcode(new Numeral[] { Numeral.two });
        bpWithInvalidBarcode = new BarcodedProduct(b2, "Carrot", 2, 1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b1, barcodedProduct);
        // invalid not added to database: hence, invalid
//        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b2, bpWithInvalidBarcode);
        barcodedItem = new BarcodedItem(b1, new Mass(barcodedProduct.getExpectedWeight() * 1000000));
        invalidBarcodedItem = new BarcodedItem(b2, new Mass(bpWithInvalidBarcode.getExpectedWeight() * 1000000));

        aibs.startCustomerSession();
    }
    
    @Test
    public void testScanTwoBarcodesWithValidBarcodedProducts() {
    	aibs.startCustomerSession();
        // reset aibs
        AddItemByBarcodeScanner.expectedPrice = 0;
        AddItemByBarcodeScanner.expectedWeight = 0;
        scanner.scan(barcodedItem);
        scanner.scan(barcodedItem);

        assertTrue(AddItemByBarcodeScanner.expectedPrice == 2 * price);
        assertTrue(AddItemByBarcodeScanner.expectedWeight == 2 * weight);
        assertTrue(State.isSession() == true);
    }

    @Test
    public void testScanBarcodeWithValidBarcodedProduct() {
        aibs.startCustomerSession();
        // reset aibs
        AddItemByBarcodeScanner.expectedPrice = 0;
        AddItemByBarcodeScanner.expectedWeight = 0;
        scanner.scan(barcodedItem);

        assertTrue(AddItemByBarcodeScanner.expectedPrice == price);
        assertTrue(AddItemByBarcodeScanner.expectedWeight == weight);
        assertTrue(State.isSession() == true);
    }

    // Expected: no throws, but does not take in the scan.
    // No expected price change, expected weight change
    @Test
    public void testScanBarcodeWithInvalidBarcodedProduct() {
        aibs.startCustomerSession();
//        // reset aibs
//        AddItemByBarcodeScanner.expectedPrice = 0;
//        AddItemByBarcodeScanner.expectedWeight = 0;
        double delta1 = AddItemByBarcodeScanner.expectedPrice;
        double delta2 = AddItemByBarcodeScanner.expectedPrice;
        scanner.scan(invalidBarcodedItem);

        assertTrue(AddItemByBarcodeScanner.expectedPrice == delta1);
        assertTrue(AddItemByBarcodeScanner.expectedWeight == delta2);
        assertTrue(State.isSession() == true);

    }

    // Expected: no throws, same as testScanBarcodeWithInvalidBarcodedProduct
    @Test
    public void testScanBarcodeWhenNotInSession() {
        AddItemByBarcodeScanner.expectedPrice = 0;
        AddItemByBarcodeScanner.expectedWeight = 0;
        aibs.endCustomerSession();
        scanner.scan(barcodedItem);

        assertTrue(AddItemByBarcodeScanner.expectedPrice == 0);
        assertTrue(AddItemByBarcodeScanner.expectedWeight == 0);
        assertTrue(State.isSession() == false);
    }
    
}