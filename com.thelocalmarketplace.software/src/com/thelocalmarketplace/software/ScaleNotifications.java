package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class ScaleNotifications implements ElectronicScaleListener{
	
	boolean scaleEnabled = false;
	boolean scaleTurnedOn = false;
	
	Session session = new Session();
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale enabled");
		scaleEnabled = true;
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale disabled");
		scaleEnabled = false;
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale turned on");
		scaleTurnedOn = true;	
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale turned off");
		scaleTurnedOn = false;
		
	}

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		
		switch(session.getSessionState()) {
			case Session.ADDING_ITEM:
				// compare actual (mass) to expected weight
				if (compareWeight(mass) == 0) {
					session.doneAdding();
				} else {
					weightDiscrepancyDuringAction(mass);	
				}
				break;
			case Session.REMOVING_ITEM:
				// compare actual (mass) to expected weight
				if (compareWeight(mass) == 0) {
					session.doneRemoving();
				} else {
					weightDiscrepancyDuringAction(mass);				
				}
				break;
			case Session.SESSION_INACTIVE:
				// do nothing
				break;
			default:
				// this point is reached when: 
				// 1. customer selected add own bag and has placed their bag in the bagging area
				// or the following incorrect actions occur:
				// 2. session started, customer placed item into bagging area without adding item
				// 3. session started, customer removed an item without choosing the remove option
				
				if (session.isAddBagsSelected() == true) {	
					// implement bags too heavy use case here
					// - if weight differs from allowable range, get attendant approves weight discrepancy
					
					// update total expected weight with current mass
					session.updateTotalExpectedWeight(mass);
					
					// reset addBagsSelected flag
					session.doneAddBags();
					
					// signal to customer they can continue
					System.out.println("You may now continue.");
				} else {
					weightDiscrepancyByIncorrectAction(mass);
				}
			}
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	// getter methods
	public boolean isScaleEnabled() {
		return scaleEnabled;
	}
	
	public boolean isScaleTurnedOn() {
		return scaleTurnedOn;
	}
	
	// helper methods
	private int compareWeight(Mass actualMass) {
		return actualMass.compareTo(session.getTotalExpectedWeight());
	}
	
	private void weightDiscrepancyDuringAction(Mass actualMass) {
		// signal to customer & attendant
		System.out.println("There is a weight discrepancy.");
		
		switch (compareWeight(actualMass)) {
			case -1: // actual < expected weight
				System.out.println("Please add item to the bagging area.");
				break;
			case 1: // actual > expected weight
				System.out.println("Please remove item from the bagging area.");
				break;
		}
		
	}
	
	private void weightDiscrepancyByIncorrectAction(Mass actualMass) {
		
		switch (compareWeight(actualMass)) {
			case -1: // actual < expected weight
				System.out.println("There is a weight discrepancy.");
				System.out.println("Please return item to the bagging area.");
				break;
			case 1: // actual > expected weight
				System.out.println("There is a weight discrepancy.");
				System.out.println("Please remove unexpected item from the bagging area.");
				break;
			default: // actual == expected weight
				System.out.println("There is no longer a weight discrepancy. Please continue.");
		}
	}
}
