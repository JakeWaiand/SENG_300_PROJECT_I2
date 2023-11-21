/*
 * Favour Adah - 30140479
 * Farouq Arafeh - 30258214
 * Adam Attwood - 30185194
 * Lucas Carvalho - 30113633
 * Tiffany Hung - 10149429
 * Mann Patel - 30182233
 * Sneh Patel - 30086076
 * Arumakankani Sayuru Silva - 30190402
 * Jake Waiand - 30179510
 *
 * Description: pay with coins class implements a coin validator observer.
 * i.e. any events related to the coin validator gets notified here.
 *
 */

package com.thelocalmarketplace.software;

import java.math.BigDecimal;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.*;

public class PayWithCoins implements CoinValidatorObserver {
    private AbstractSelfCheckoutStation sm;
    private Session session;
    private static boolean customerSelectedPayCoins = false;
    private static double amountOwed = 0;

    public PayWithCoins(Session session) {
        this.sm = session.station;
        this.session = session;
        this.sm.coinValidator.attach(this);
    }

    
    private void dispenseChange() {
        double change = Math.abs(amountOwed);
        if (change > 0) {    
        	displayMessageToScreen(String.format("Dispensing change: $%.2f", change));
        }
    }

    private void resetPayment() {
        customerSelectedPayCoins = false;
        amountOwed = 0;
    }

    public void displayMessageToScreen(String message) {
        System.out.println(message);
    }

    public void printReceipt() {
        System.out.println("Receipt printed.");
    }

    public static void setCustomerSelectedPayCoins(boolean selectedPayCoins, double amount) {
        customerSelectedPayCoins = selectedPayCoins;
        amountOwed = Math.max(0, amount);
    }

    public double getAmountOwed() {
        return amountOwed;
    }

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		if (amountOwed > 0 && customerSelectedPayCoins && State.isSession()) {
            amountOwed -= value.doubleValue();
            displayMessageToScreen(String.format("Amount Due: $%.2f", Math.max(amountOwed, 0)));
        }
        if (amountOwed <= 0) {
            dispenseChange();
            session.printReceipt();
            resetPayment();
        }
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		displayMessageToScreen("Invalid coin detected. Please try another coin.");
		// TODO Auto-generated method stub
		
	}
}
