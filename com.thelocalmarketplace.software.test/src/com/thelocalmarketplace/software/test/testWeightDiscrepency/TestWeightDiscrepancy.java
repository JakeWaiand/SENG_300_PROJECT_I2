/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Rebecca Hamshaw - 30191086
 * @author Mann Patel - 30182233
 */
package testWeightDiscrepency;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.AddItemByBarcodeScanner;
import com.thelocalmarketplace.software.State;
import com.thelocalmarketplace.software.WeightDiscrepancy;

import powerutility.PowerGrid;

import com.jjjwelectronics.scale.ElectronicScale;

public class TestWeightDiscrepancy {
	private WeightDiscrepancy weightDiscrepancy;
    private SelfCheckoutStation selfCheckoutStation;
    private ElectronicScale scale;

    @Before
    public void setUp() {
		SelfCheckoutStation scs = new SelfCheckoutStation();
		PowerGrid pg = PowerGrid.instance();
		// what type of power source should this be? probably the below is purely for
		// when testing
		PowerGrid.engageUninterruptiblePowerSource();

		// plug in + turn on scs
		scs.plugIn(pg);
		scs.turnOn();

    	scale = scs.baggingArea;
        selfCheckoutStation = new SelfCheckoutStation();
        
        weightDiscrepancy = new WeightDiscrepancy(selfCheckoutStation);
    }

    
    @Test
    public void testBlockSessionMassLessThanExpected() {
    	AddItemByBarcodeScanner.expectedWeight = 200;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        assertTrue(State.isSession() == false);
    }
    
    @Test
    public void testBlockSessionMassMoreThanExpected() {
    	AddItemByBarcodeScanner.expectedWeight = 50;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        assertTrue(State.isSession() == false);
    }
    
    @Test
    public void testSessionMassIsCorrect() {
    	AddItemByBarcodeScanner.expectedWeight = 100;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        assertTrue(State.isSession() == true);
    }
   
    
    @Test
    public void testBlockSessionMassExceedsLimit() {
    	weightDiscrepancy.theMassOnTheScaleHasExceededItsLimit(scale);
    	assertTrue(State.isSession()==false);
    }
    
    @Test
    public void testBlockSessionMassLessThanLimit() {
    	weightDiscrepancy.theMassOnTheScaleHasExceededItsLimit(scale);
    	assertTrue(State.isSession()==false);
    }
    
    @Test
    public void testUnblockSessionMassCorrected() {
    	weightDiscrepancy.theMassOnTheScaleHasExceededItsLimit(scale);
    	assertTrue(State.isSession()==false);
    	weightDiscrepancy.theMassOnTheScaleNoLongerExceedsItsLimit(scale);
    	assertTrue(State.isSession()==true);
    }
    

   
    @Test
    public void testUnblockSessionMassIsExpected() {
    	AddItemByBarcodeScanner.expectedWeight = 200;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        assertTrue(State.isSession() == false);
        AddItemByBarcodeScanner.expectedWeight = 100;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        assertTrue(State.isSession() == true); 	
    }
    
    @Test
    public void testDisplayMessageToCustomerMassLessThanExpected() {
    	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(outputStreamCaptor));
    	
    	AddItemByBarcodeScanner.expectedWeight = 200;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        String output = outputStreamCaptor.toString().trim();
      //Test if expected message is displayed
        assertTrue(output.contains("Add expected item from bagging area."));
        //reset
        PrintStream standardOut = System.out;
        System.setOut(standardOut); 
   }
    
    @Test
    public void testDisplayMessageToAttendantMassLessThanExpected() {
    	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(outputStreamCaptor));
    	
    	AddItemByBarcodeScanner.expectedWeight = 200;
        weightDiscrepancy.theMassOnTheScaleHasChanged(scale, new Mass(100));
        String output = outputStreamCaptor.toString().trim();
        //Test if expected message is displayed
        assertTrue(output.contains("Item missing from customer bagging area"));
      //reset
        PrintStream standardOut = System.out;
        System.setOut(standardOut); 
   }
    
    @Test
    public void testDisplayMessageToCustomerMassExceedsLimit() {
    	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(outputStreamCaptor));
    	
    	weightDiscrepancy.theMassOnTheScaleHasExceededItsLimit(scale);
        String output = outputStreamCaptor.toString().trim();
        //Test if expected message is displayed
        assertTrue(output.contains("Add the item to the bagging area or select 'do not bag item'."));
      //reset
        PrintStream standardOut = System.out;
        System.setOut(standardOut); 
   }
    
    @Test
    public void testDisplayMessageToAttendantMassExceedsLimit() {
    	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(outputStreamCaptor));
    	
    	weightDiscrepancy.theMassOnTheScaleHasExceededItsLimit(scale);
        String output = outputStreamCaptor.toString().trim();
        //Test if expected message is displayed
        assertTrue(output.contains("Missing item in customer bagging area"));
      //reset
        PrintStream standardOut = System.out;
        System.setOut(standardOut); 
   }
}
    