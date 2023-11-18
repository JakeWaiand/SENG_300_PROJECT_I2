/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package com.thelocalmarketplace.software;

import java.math.BigDecimal;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.*;



public class PayViaCash  implements CoinValidatorObserver{
	private SelfCheckoutStation sm;
	private boolean customerSelectedPayCash = false;
	private double amountOwed = 0;
	
	

	public PayViaCash( SelfCheckoutStation sm ) {
		this.sm.coinValidator.attach(this);
		
		
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
	//This method will reduce the amount due each time a coin is inserted 
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		if ( amountOwed > 0 && (customerSelectedPayCash && State.isSession()) ) {
		amountOwed = amountOwed - value.doubleValue();
		displayMessageToScreen(String.format("Amount Due: $%.2f", amountOwed));
		}
		
		//Customer has finished paying. Print receipt and reset pay-with-cash variable
		if (amountOwed <= 0 ) {
			printReceipt();
			customerSelectedPayCash = false;
		}
		
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// Hardware should handle. System does nothing.
	}
	
	//Simulates displaying message to the self-checkout screen
	public void displayMessageToScreen(String message) {
		System.out.println(message);
	}
	
	//Simulates printing a receipt 
	public void printReceipt() {
		System.out.println("receipt");
	}
	
	//Alert class of when coin payment has been selected.
	public void setCustomerSelectedPayCash(boolean customerSelectedPayCash) {
		this.customerSelectedPayCash = customerSelectedPayCash;
		Double amount = AddItemByBarcodeScanner.expectedPrice;
		amountOwed = amount.doubleValue();
	}
	
	public double getAmountOwed() {
		return amountOwed;
	}
	

}
