package com.thelocalmarketplace.software;

import java.util.ArrayList;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.thelocalmarketplace.hardware.BarcodedProduct;

public class Session {

    // constants for session state
    public static final int SESSION_INACTIVE = 0;
    public static final int BILL_EMPTY = 1;
    public static final int BILL_NOT_EMPTY = 2;
    public static final int ADDING_ITEM = 3;
    public static final int REMOVING_ITEM = 4;
    public static final int PAY_FOR_BILL = 5;

    // constants for checkout state
    public static final boolean LOCK = true;
    public static final boolean UNLOCK = false;
    
    // default variables
    private static int sessionState = SESSION_INACTIVE;
    private static boolean checkoutState = LOCK;
    private static Mass totalExpectedWeight = Mass.ZERO;
    private static boolean addBagsSelected = false; 		// flag for "add own bags" feature

    // instantiate classes
    TransactionRecord record = new TransactionRecord();

    // methods to transition between session states

    // since no GUI, method simulates when a customer presses start session button
    public void startSession() {
        if (sessionState == SESSION_INACTIVE) {
            sessionState = BILL_EMPTY;
            checkoutState = UNLOCK;
        }
        // else, do not transition
    }

    public void startAdding() {
        if (sessionState == BILL_EMPTY || sessionState == BILL_NOT_EMPTY) {
            sessionState = ADDING_ITEM;
        }
        // else, do not transition
    }

    public void doneAdding() {
        if (sessionState == ADDING_ITEM) {
            sessionState = BILL_NOT_EMPTY;
            checkoutState = UNLOCK;
        }
        // else, do not transition
    }

    // since no GUI, method simulates when a customer chooses to remove an item
    public void startRemoving(BarcodedProduct product) {
        if (sessionState == BILL_NOT_EMPTY) {
            sessionState = REMOVING_ITEM;
            checkoutState = LOCK;

            /*
             * TODO: add "remove item" logic
             * - remove product from bill
             * - remove price from total
             * - remove expected weight from total
             * - signal to customer item was successfully removed
             * - signal to customer to remove item from bagging area
             * - (logic continues in scale notification class)
             *
             */

        }
        // else, do not transition
    }

    public void doneRemoving() {
        if (sessionState == REMOVING_ITEM && record.getBillItemCount() > 0) {
            sessionState = BILL_NOT_EMPTY;
            checkoutState = UNLOCK;
        } else if (sessionState == REMOVING_ITEM && record.getBillItemCount() == 0) {
            sessionState = BILL_EMPTY;
            checkoutState = UNLOCK;
        }
        // else, do not transition
    }

    public void pay(long totalPrice, int paymentType) {
        if (sessionState == BILL_NOT_EMPTY) {
            sessionState = PAY_FOR_BILL;

            // set payment type
            switch (paymentType) {
                case TransactionRecord.COIN:
                    record.setPaymentToCoin();
                    break;
                case TransactionRecord.BANKNOTE:
                    record.setPaymentToBanknote();
                    break;
                case TransactionRecord.CASH:
                    record.setPaymentToCash();
                    break;
                case TransactionRecord.CREDIT:
                    record.setPaymentToCredit();
                    break;
                case TransactionRecord.DEBIT:
                    record.setPaymentToDebit();
                    break;
                case TransactionRecord.CRYPTO:
                    record.setPaymentToCrypto();
                    break;
            }

            // update amount owed
            record.setAmountOwed(record.getTotalPrice());

            // signal to customer the amount owed
            System.out.printf("The amount owed is: %d\n", record.getAmountOwed());

        }
        // else, ignore pay request
    }


    public void endSession() {
        if (sessionState == PAY_FOR_BILL) {
            sessionState = SESSION_INACTIVE;
            checkoutState = LOCK;

            // clear everything back to default state
            totalExpectedWeight = Mass.ZERO;
            record.clearBill();
            record.clearTotalPrice();
            record.clearHoldNumber();
            record.clearPaymentType();
        }
        // else, do not transition
    }

    // methods that modify checkout state
    public void lockCheckout() {
        checkoutState = LOCK;
    }

    public void unlockCheckout() {
        checkoutState = UNLOCK;
    }
    
    // method that modify expected weight
    public void addToTotalExpectedWeight(BarcodedProduct product) {
        // convert product weight of type double into Mass type
        Mass productExpectedWeight = new Mass(product.getExpectedWeight());

        totalExpectedWeight = productExpectedWeight.sum(totalExpectedWeight);
    }

    // methods for dealing with bagging options

    // since no GUI, method simulates when a customer chooses "add own bags"
    public void startAddBags() {
        // bags can only be added when session is active and
        // customer isn't in the process of an action
        // (i.e. adding/removing item or paying)?
        if (sessionState == BILL_EMPTY || sessionState == BILL_NOT_EMPTY) {

            /*
             * TODO: add "add own bags" logic
             * - set flag to true (flag used in theMassOnTheScaleHasChanged() in scaleNotifications class)
             * - signal to customer to add to the bagging area
             *
             */
        }
    }

    public void doneAddBags() {

        /*
         * TODO: add "add own bags" logic
         * - set flag to false
         */

    }

    // since no GUI, method simulates when a customer chooses "do not bag"
    public void doNotBag() {
        // this option is only available when adding an item
        if (sessionState == ADDING_ITEM) {
            // don't need to implement in this iteration:
            // - signal to attendant
            // - get attendant approval

            // get index of last item on bill
            int indexOfLastItem = record.getBillItemCount() - 1;

            // get product from bill list
            BarcodedProduct product = record.getItemFromBill(indexOfLastItem);

            // get expected weight from product
            Mass productExpectedWeight = new Mass(product.getExpectedWeight());

            // remove expected weight from total
            MassDifference remainingWeight = totalExpectedWeight.difference(productExpectedWeight);
            totalExpectedWeight = remainingWeight.abs();

            // re-allow customer interaction
            sessionState = BILL_NOT_EMPTY;
        }
        // else, do nothing

    }

    // getter methods
    public int getSessionState() {
        return sessionState;
    }

    public Mass getTotalExpectedWeight() {
        return totalExpectedWeight;
    }

    public boolean isAddBagsSelected() {
        return addBagsSelected;
    }
}
