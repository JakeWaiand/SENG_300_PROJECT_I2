package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class ScaleNotifications implements ElectronicScaleListener{

	private static final boolean ENABLE = true;
	private static final boolean DISABLE = false;
	private static final boolean ON = true;
	private static final boolean OFF = false;
	
	boolean scaleStatus = DISABLE;
	boolean scalePower = OFF;
	
	Session session = new Session();
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale enabled");
		scaleStatus = ENABLE;
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale disabled");
		scaleStatus = DISABLE;
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale turned on");
		scalePower = ON;	
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		//System.out.println("Scale turned off");
		scalePower = OFF;
		
	}

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		
		if (session.getSessionState() == Session.ADDING_ITEM || session.getSessionState() == Session.REMOVING_ITEM) {
			
			// compare actual (mass) to expected weight
			if (mass.compareTo(session.getTotalExpectedWeight()) == 0) { 
				// no weight discrepancy, item is added to order
				// i.e. session state updated to BILL_NOT_EMPTY
				session.doneAdding();
				
				// unblock checkout for next interaction
				session.unlockCheckout();
				
			} else { // actual != expected weight
				// signal to customer & attendant
				System.out.println("There is a weight discrepancy.");
				
				if (mass.compareTo(session.getTotalExpectedWeight()) == -1) { // actual < expected weight
					System.out.println("Please add item to the bagging area.");
				} else { // actual > expected weight
					System.out.println("Please remove item from the bagging area.");
				}
			}
			
		} else {
			// this point is reached when: 
			// 1. customer selected add own bag and has placed their bag in the bagging area
			// 2. session started, customer placed item into bagging area without adding item
			// 3. session started, customer removed an item without choosing the remove option
			
			if (session.isAddBagsSelected() == true) {	
				// implement bags too heavy use case here
				// - if weight differs from allowable range, get attendant approves weight discrepancy
				
				// update current actual weight to total expected weight
				session.updateTotalExpectedWeight(mass);
				
				// reset addBagsSelected flag
				session.doneAddBags();
				
				// signal to customer they can continue
				System.out.println("You may now continue.");
				
			} else if (session.getSessionState() == Session.SESSION_INACTIVE) {
				// session is not active -> ignore change of mass
				
			} else {
				
				// signal to customer
				System.out.println("There is a weight discrepancy.");
				
				if (mass.compareTo(session.getTotalExpectedWeight()) == -1) { // actual < expected weight
					System.out.println("Please return item to the bagging area.");
				} else { // actual > expected weight
					System.out.println("Please remove unexpected item from the bagging area.");
				}
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

}
