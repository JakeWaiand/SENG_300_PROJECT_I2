package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.*;

public class PayWithCoins implements CoinValidatorObserver {
    private SelfCheckoutStation sm;
    private boolean customerSelectedPayCoins = false;
    private double amountOwed = 0;

    public PayWithCoins(SelfCheckoutStation sm) {
        this.sm = sm;
        this.sm.coinValidator.attach(this);
    }

    @Override
    public void enabled(IComponent<? extends IComponentObserver> component) {
     
    }

    @Override
    public void disabled(IComponent<? extends IComponentObserver> component) {
      
    }

    @Override
    public void turnedOn(IComponent<? extends IComponentObserver> component) {
        
    }

    @Override
    public void turnedOff(IComponent<? extends IComponentObserver> component) {
     
    }

    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value) {
        try {
            if (amountOwed > 0 && customerSelectedPayCoins && State.isSession()) {
                amountOwed -= value.doubleValue();
                displayMessageToScreen(String.format("Amount Due: $%.2f", Math.max(amountOwed, 0)));
            }

            if (amountOwed <= 0) {
                dispenseChange();
                printReceipt();
                resetPayment();
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        displayMessageToScreen("Invalid coin detected. Please try another coin.");
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

    private void handleException(Exception e) {
     
        displayMessageToScreen("An error occurred. Please try again.");
    }

    public void displayMessageToScreen(String message) {
        System.out.println(message);
    }

    public void printReceipt() {
        System.out.println("Receipt printed.");
    }

    public void setCustomerSelectedPayCoins(boolean customerSelectedPayCoins, double amount) {
        this.customerSelectedPayCoins = customerSelectedPayCoins;
        this.amountOwed = Math.max(0, amount);
    }

    public double getAmountOwed() {
        return amountOwed;
    }
}
