package com.thelocalmarketplace.software;

import java.math.BigDecimal;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinSlotObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;

public class payWithCoin implements CoinValidatorObserver, CoinSlotObserver{

	private static final boolean ENABLE = true;
	private static final boolean DISABLE = false;
	private static final boolean ON = true;
	private static final boolean OFF = false;
	private static BigDecimal amountOwed;
	private static final BigDecimal zero = new BigDecimal("0");

	boolean payStatus = DISABLE;
	boolean payPower = OFF;

	Session session = new Session();

	public payWithCoin(BigDecimal amountOwed) {
		payWithCoin.amountOwed = amountOwed;
	}


	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// System.out.println("pay has been Enabled");
		payStatus = ENABLE;
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// System.out.println("pay has been disabled");
		payStatus = DISABLE;
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// System.out.println("pay has been turned on");
		payPower = ON;
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// System.out.println("device is turned off");
		payPower = OFF;
	}

	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		// System.out.println("valid coin detected");
		// ensure that the coin in valid
		// ensure that the cart isn't empty
		// subtract value from owedAmount

		// At this point we know that the coin is valid
		payWithCoin.amountOwed = amountOwed.subtract(value);
		if (amountOwed.compareTo(zero) <= 0) {
			// order has been paid for, end session
			session.endSession();
		}
		else {
			// please enter more currency
			System.out.println("Remaining balance: " + amountOwed);
		}

	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinInserted(CoinSlot slot) {
		// figure out if the coin is valid
		// if valid coin is detected then call validCoinDetected
		// if coin is invalid then call invalidCoinDetected

		System.out.println("Coin has been inserted");

	}
}
