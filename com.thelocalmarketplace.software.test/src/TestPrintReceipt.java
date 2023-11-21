/*
 * Favour Adah - 30140479
 * Farouq Arafeh - 30258214
 * Adam Attwood - 30185194
 * Lucas Carvalho - 30113633
 * Tiffany Hung - 10149429
 * Mann Patel - 30182233
 * Arumakankani Sayuru Silva - 30190402
 * Jake Waiand - 30179510
 * 
 * description: test case for print receipts
 */
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.PrintReceipt;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class TestPrintReceipt {

    private BarcodedProduct barcodedProduct;
	private Barcode b1;
	private Barcode b2;
	private BarcodedProduct barcodedProduct2;

	@Test
    public void testPrintReceipt() {
        // Create instances of SelfCheckoutStation for each type (Bronze, Silver, Gold)
        SelfCheckoutStationBronze bronzeStation = new SelfCheckoutStationBronze();
        SelfCheckoutStationSilver silverStation = new SelfCheckoutStationSilver();
        SelfCheckoutStationGold goldStation = new SelfCheckoutStationGold();

        // Create instances of BarcodedProduct for testing
        b1 = new Barcode(new Numeral[] { Numeral.one });
        barcodedProduct = new BarcodedProduct(b1, "Apple", 5, 1);
        b2 = new Barcode(new Numeral[] { Numeral.two });
        barcodedProduct2 = new BarcodedProduct(b2, "Very Very Small Fruitcake", 2, 1);

        // Create an ArrayList to simulate the bill
        ArrayList<BarcodedProduct> bill = new ArrayList<>();
        bill.add(barcodedProduct);
        bill.add(barcodedProduct2);

        // Test for Bronze Station
        PrintReceipt printReceiptBronze = new PrintReceipt(bronzeStation);
        testPrintReceiptForStation(printReceiptBronze, bill);

        // Test for Silver Station
        PrintReceipt printReceiptSilver = new PrintReceipt(silverStation);
        testPrintReceiptForStation(printReceiptSilver, bill);

        // Test for Gold Station
        PrintReceipt printReceiptGold = new PrintReceipt(goldStation);
        testPrintReceiptForStation(printReceiptGold, bill);
    }

    private void testPrintReceiptForStation(PrintReceipt printReceipt, ArrayList<BarcodedProduct> bill) {
        // Mock the display and printer actions
        printReceipt.displayToCustomer("Testing display to customer");
        printReceipt.displayToAttendant("Testing display to attendant");

        // Print the receipt and cut it
        printReceipt.printReceipt(bill, 25.0);
        printReceipt.cutReceipt();

        // Remove the receipt and verify the output
        String removedReceipt = printReceipt.removeReceipt();
        assertEquals("TheLocalMarketplace\n\nRECEIPT DATE:", removedReceipt.substring(0, 36));
        assertTrue(removedReceipt.contains("Product 1 - 10.0"));
        assertTrue(removedReceipt.contains("Product 2 - 15.0"));
        assertTrue(removedReceipt.contains("TOTAL: 25.0"));
    }
}
