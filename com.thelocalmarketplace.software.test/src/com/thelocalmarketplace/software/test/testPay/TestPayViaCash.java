/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package testPay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;

import com.thelocalmarketplace.software.AddItemByBarcodeScanner;
import com.thelocalmarketplace.software.PayViaCash;
import com.thelocalmarketplace.software.State;

import powerutility.PowerGrid;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

public class TestPayViaCash {
    private PayViaCash payViaCash;
    private SelfCheckoutStation selfCheckoutStation;
    public BigDecimal coinValue;
    @Before
    public void setUp() {
    	PowerGrid pg = PowerGrid.instance();
        PowerGrid.engageUninterruptiblePowerSource();
        
        selfCheckoutStation = new SelfCheckoutStation();
        selfCheckoutStation.plugIn(pg);
        selfCheckoutStation.turnOn();
        
        coinValue = new BigDecimal("5.0");
        
        payViaCash = new PayViaCash(selfCheckoutStation);
        
        selfCheckoutStation.coinValidator.attach(payViaCash);
        State.setSession(true);
        
    }
    
    @Test
    public void testValidCoinDetected() {
        double initialAmountOwed = 10.0;
        AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
        payViaCash.setCustomerSelectedPayCash(true);
        payViaCash.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
        double expected = initialAmountOwed - coinValue.doubleValue();
        double actual = payViaCash.getAmountOwed();
        assertEquals(expected, actual, 0.01);
    } 
    
    @Test
    public void testInValidCoinDetected() throws DisabledException, CashOverloadException {
    	payViaCash.setCustomerSelectedPayCash(true);
        double initialAmountOwed = 10.0;
        AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
        payViaCash.invalidCoinDetected(selfCheckoutStation.coinValidator);
        double actual = payViaCash.getAmountOwed();
        assertEquals(initialAmountOwed, actual, 0.01);
    }
    
    @Test
    public void testDisplayAmountOwedToUser() {
    	 ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    	 System.setOut(new PrintStream(outputStreamCaptor));
    	 
    	 payViaCash.setCustomerSelectedPayCash(true);
         double initialAmountOwed = 10.0;
         AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
         payViaCash.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
         assertEquals("Amount Due: $5.00", outputStreamCaptor.toString().trim());
         
         PrintStream standardOut = System.out;
         System.setOut(standardOut);
    }
    
    @Test
    public void testPrintReceiptWhenCustomerPaid() {
   	 ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
   	 System.setOut(new PrintStream(outputStreamCaptor));
   	 
     double initialAmountOwed = 5.0;
     AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
     payViaCash.setCustomerSelectedPayCash(true);
     payViaCash.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
     String output = outputStreamCaptor.toString().trim();
     assertTrue(output.contains("receipt"));
     
     PrintStream standardOut = System.out;
     System.setOut(standardOut);
   }
    
    @Test
    public void testPrintReceiptWhenCustomerOverpaid() {
      	 ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      	 System.setOut(new PrintStream(outputStreamCaptor));
      	 
        double initialAmountOwed = 5.0;
        AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
    	payViaCash.setCustomerSelectedPayCash(true);
        payViaCash.validCoinDetected(selfCheckoutStation.coinValidator, coinValue.add(coinValue));
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("receipt"));
        
        PrintStream standardOut = System.out;
        System.setOut(standardOut);
      }
    
    @Test
    public void testBlockSession() {
    	double initialAmountOwed = 10.0;
        AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
    	payViaCash.setCustomerSelectedPayCash(true);
    	State.setSession(false);
        payViaCash.invalidCoinDetected(selfCheckoutStation.coinValidator);
        double actual = payViaCash.getAmountOwed();
        assertEquals(initialAmountOwed, actual, 0.01);
      }
    
    @Test
    public void testIgnoreCoinWhenPayWithCashIsNotSelected() {	 
    	double initialAmountOwed = 10.0;
        AddItemByBarcodeScanner.expectedPrice = initialAmountOwed;
    	payViaCash.setCustomerSelectedPayCash(false);
        payViaCash.invalidCoinDetected(selfCheckoutStation.coinValidator);
        double actual = payViaCash.getAmountOwed();
        assertEquals(initialAmountOwed, actual, 0.01);
      }
    
    
    // add items, pay with currency , check amount owed
    //create item, add with scanner,self check out
    
    

	
}