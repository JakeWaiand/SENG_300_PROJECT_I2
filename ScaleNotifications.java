package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class ScaleNotifications implements ElectronicScaleListener {

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
		//System.out.println("Mass on scale changed");

		// if session state is == SESSION_INACTIVE || PAY_FOR_ORDER, 
		// ignore change of mass (do nothing)
		// use cases doesn't specify that a weight discrepancy should occur
		
		if (session.getSessionState() == Session.ORDER_EMPTY 
				|| session.getSessionState() == Session.ORDER_NOT_EMPTY) {
			if (session.getCheckoutState() == Session.BLOCK) {
				// this point is reached when:
				// 1. session started, customer chooses to add item (by scan, PLU, text, etc.)
				// 2. session started, customer chooses to remove item
				
				// compare actual (mass) to expected weight
				if (mass.compareTo(session.getTotalExpectedWeight()) == 0) { 
					// no weight discrepancy, item is added to order
					// i.e. session state updated to ORDER_NOT_EMPTY
					session.addItem();
					
					// unblock checkout for next interaction
					session.unblockCheckout();
					
				} else { // actual != expected weight
					// signal to customer & attendant
					System.out.println("There is a weight discrepancy.");
					
					if (mass.compareTo(session.getTotalExpectedWeight()) == -1) { // actual < expected weight
						System.out.println("Please add item to the bagging area.");
					} else { // actual > expected weight
						System.out.println("Please remove item from the bagging area.");
					}
				}
				
			} else { // checkout is not blocked
				// this point is reached when: 
				// 1. session started, customer placed item into bagging area without adding item
				// 2. session started, customer removed an item without choosing the remove option
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
		//System.out.println("Mass on scale exceeds limit");
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		//System.out.println("Mass on scale no longer exceeds limit");
		
	}

}
