package com.thelocalmarketplace.software;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.*;
import com.thelocalmarketplace.hardware.*;

import java.math.BigDecimal;
import java.util.Currency;

public class PayWithBanknote implements BanknoteValidatorObserver, BanknoteDispenserObserver {

    private AbstractSelfCheckoutStation sm;
    private Session session;

    public PayWithBanknote(Session session) {
        this.sm = session.station;
        this.session = session;
        this.sm.banknoteValidator.attach(this);
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
    public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
        BigDecimal amountOwned = session.record.getAmountOwed();
        BigDecimal newAmount = amountOwned.subtract(denomination);
        if (newAmount.compareTo(BigDecimal.ZERO) == 1) {
            session.record.setAmountOwed(newAmount.longValue());
            System.out.println("Amount Remaining: " +newAmount);
        }
        else if (newAmount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Amount Remaining: " +newAmount);
        } 
        else {
            System.out.println("Change due: "+newAmount.negate());
        }
    }

    @Override
    public void badBanknote(BanknoteValidator validator) {
        System.out.println("Invalid banknote. Please try again");
    }

    @Override
    public void moneyFull(IBanknoteDispenser dispenser) {

    }

    @Override
    public void banknotesEmpty(IBanknoteDispenser dispenser) {

    }

    @Override
    public void banknoteAdded(IBanknoteDispenser dispenser, Banknote banknote) {

    }

    @Override
    public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {

    }

    @Override
    public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {

    }

    @Override
    public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {

    }
}
