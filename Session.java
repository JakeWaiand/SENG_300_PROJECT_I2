package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.BarcodedProduct;

// Description: used to track what state the session is in
// initial session state is SESSION_INACTIVE
// initial checkout state is BLOCK
public class Session {
	
	public static final int SESSION_INACTIVE = 0;
	public static final int ORDER_EMPTY = 1;
	public static final int ORDER_NOT_EMPTY = 2;
	public static final int PAY_FOR_ORDER = 3;
	
	public static final boolean UNBLOCK = true;
	public static final boolean BLOCK = false;
	
	private static int sessionState = SESSION_INACTIVE;
	private static boolean checkoutState = BLOCK;
	
	private static Mass totalExpectedWeight = Mass.ZERO;
	private static long totalPrice = 0;

	// sessionState methods to transition from one state to another
	void startSession() {
		sessionState = ORDER_EMPTY;
		checkoutState = UNBLOCK;
	}

	void addItem() {
		sessionState = ORDER_NOT_EMPTY;
	}

	void removeItem0() {
		sessionState = ORDER_EMPTY;
	}

	void removeItem1() {
		sessionState = ORDER_NOT_EMPTY;
	}

	void payForOrder() {
		sessionState = PAY_FOR_ORDER;
	}
	
	void endSession() {
		sessionState = SESSION_INACTIVE;
		checkoutState = BLOCK;
	}
	
	// method to get current session state
	int getSessionState() {
		return sessionState;
	}
	
	// checkoutState methods to transition between states
	void blockCheckout() {
		checkoutState = BLOCK;
	}
	
	void unblockCheckout() {
		checkoutState = UNBLOCK;
	}
	
	// method to get current checkout state
	boolean getCheckoutState() {
		return checkoutState;
	}
	
	// methods to add and get total expected weight
	void addToTotalExpectedWeight(BarcodedProduct product) {
		// convert product weight (double type) into Mass type
		Mass productExpectedWeight = new Mass(product.getExpectedWeight());
		
		totalExpectedWeight = productExpectedWeight.sum(totalExpectedWeight);
	}
	
	Mass getTotalExpectedWeight() {
		return totalExpectedWeight;
	}
	
	// methods to add and get total price
	void addToTotalPrice(BarcodedProduct product) {
		totalPrice += product.getPrice();
	}
	
	long getTotalPrice() {
		return totalPrice;
	}
}