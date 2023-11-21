/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package com.thelocalmarketplace.software;
import com.thelocalmarketplace.hardware.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.jjjwelectronics.*;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class WeightDiscrepancy implements ElectronicScaleListener {
   
    //sets the constructor 
    public WeightDiscrepancy(SelfCheckoutStation sm){
        sm.baggingArea.register(this);
    }
    
    //overrides whatever needs to be overridden from ElectronicScaleListener
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}
  
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {};
   
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {};
    
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {};



    //Displays message of error to the customers screen
    public void displayMessageToScreen(String message){
        System.out.println(message);
    }
    
    //Displays error message to the attendant
    public void displayMessageToAttendantScreen(String message){
        System.out.println(message);
    }
    
    //Gets the expected and actual mass and calculates if they are the same or not
    @Override
    public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass){
        double ExpectedWeight = AddItemByBarcodeScanner.expectedWeight;
        //if there are less items in the bagging area than expected, turn off the session and inform the customer and attendant
        BigInteger totalMass = mass.inMicrograms();
        if (totalMass.doubleValue() < ExpectedWeight){
            State.setSession(false);
            displayMessageToScreen("Add expected item from bagging area.");
            displayMessageToAttendantScreen("Item missing from customer bagging area");
        }
        else if (ExpectedWeight == totalMass.doubleValue()){
            State.setSession(true);
        }
    }
 
    //if there are more items in the bagging area than expected, turn off the session and inform the customer and attendant
    @Override
    public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale){
        State.setSession(false);
        displayMessageToScreen("Add the item to the bagging area or select 'do not bag item'.");
        displayMessageToAttendantScreen("Missing item in customer bagging area");
    }

    //if there is no discrepancy between items in the bagging area and the expected weight, turn on the session
    @Override
    public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale){
        State.setSession(true);
    }
        
   
    


}
