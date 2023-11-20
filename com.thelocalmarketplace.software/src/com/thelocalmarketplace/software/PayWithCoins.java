package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.*;

public class PayWithCoins implements CoinValidatorObserver {
    private SelfCheckoutStation sm;
    private static boolean customerSelectedPayCoins = false;
    private static double amountOwed = 0;
    private String stationType;

    public PayWithCoins(SelfCheckoutStation sm, String stationType) {
        this.sm = sm;
        this.stationType = stationType;
        this.sm.coinValidator.attach(this);
    }

    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value) {
        if (amountOwed > 0 && customerSelectedPayCoins && State.isSession()) {
            amountOwed -= value.doubleValue();
            displayMessageToScreen(String.format("Amount Due: $%.2f", Math.max(amountOwed, 0)));
        }
        if (amountOwed <= 0) {
            dispenseChange();
            printReceipt();
            resetPayment();
        }
    }

    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        displayMessageToScreen("Invalid coin detected. Please try another coin.");
    }

    private void dispenseChange() {
        double change = Math.abs(amountOwed);
        if (change > 0) {
            switch (stationType) {
                case "Gold":
                    
                    break;
                case "Silver":
                    
                    break;
                case "Bronze":
                    
                    break;
            }
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
}
