/*
 * TODO: add everyone's name + UCID
 * 
 * Description: session class keeps tract of session and checkout state.
 * Also tracks the total expected weight and bagging options (add bags, not not bag).
 * 
 * Since no GUI currently implemented, the following methods are used to simulate 
 * customer interacting with the GUI:
 * - startSession() 
 * - startRemoving()
 * - pay()
 * - startAddBags()
 * - doNotBag()
 * 
 * Session state summary:
 * - SESSION_INACTIVE = 0 	represents when the session hasn't started (initial state)
 * - BILL_EMPTY = 1 		represents when session is started but no items added (no action in progress)
 * - BILL_NOT_EMPTY = 2		represents when session is started and items exist on the bill (no action in progress)
 * - ADDING_ITEM = 3		represents when a customer is in the process of adding an item
 * - REMOVING_ITEM = 4		represents when a customer is in the process of removing an item
 * - PAY_FOR_BILL = 5		represents when a customer is in the process of paying for the bill
 * - PRINTING_RECEIPT = 6	represents when a the bill has been paid and the receipt is printing
 * 
 * Transition table format:
 * states       |methods  |
 * ------------------------
 * current state|end state|
 * 
 * i.e. if you run a method in the current state, it tells you what state you end in
 * 
 * state|startSession|startAdding|doneAdding|startRemoving|doneRemoving|pay|printReceipt|endSession|
 * -------------------------------------------------------------------------------------------------
 * 0 	|1           |0          |0         |0            |0           |0  |0           |0         |
 * 1 	|1           |3          |1         |1            |1           |1  |1           |1         |
 * 2 	|2           |3          |3         |4            |2           |5  |2           |2         |
 * 3 	|3           |3          |2         |3            |3           |3  |3           |3         |
 * 4 	|4           |4          |4         |4            |1 or 2      |4  |4           |4         |
 * 5 	|5           |5          |5         |5            |5           |5  |6           |5         |
 * 6 	|6           |6          |6         |6            |6           |6  |6           |0         |
 */

package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;

import java.io.IOException;
import java.math.BigDecimal;

public class Session {

    // constants for session state
    public static final int SESSION_INACTIVE = 0;
    public static final int BILL_EMPTY = 1;
    public static final int BILL_NOT_EMPTY = 2;
    public static final int ADDING_ITEM = 3;
    public static final int REMOVING_ITEM = 4;
    public static final int PAY_FOR_BILL = 5;
    public static final int PRINTING_RECEIPT = 6;

    // constants for checkout state
    public static final boolean LOCK = true;
    public static final boolean UNLOCK = false;
    
    // default variables
    private static int sessionState = SESSION_INACTIVE;
    private static boolean checkoutState = LOCK;
    private static Mass totalExpectedWeight = Mass.ZERO;
    private static boolean addBagsSelected = false; 		// flag for "add own bags" feature

    //initialize 
    public AbstractSelfCheckoutStation station;

    // instantiate classes
    TransactionRecord record = new TransactionRecord();
    private long amountDue;
    
    private Card card;
    private CardIssuer cardIssuer;
    private Coin coin;
    
    private CardReaderNotifications instance = new CardReaderNotifications(); 

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
        if (sessionState == BILL_NOT_EMPTY && checkoutState == UNLOCK) {
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

    // since no GUI, method simulates when a customer chooses to pay for bill	
    public void pay(long totalPrice, int paymentType) throws IOException, DisabledException, CashOverloadException {
        if (sessionState == BILL_NOT_EMPTY && checkoutState == UNLOCK) {
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
		
	    if (paymentType == TransactionRecord.COIN) {
                // set flag and amount in PayWithCoins class
                PayWithCoins.setCustomerSelectedPayCoins(true, (double) record.getTotalPrice());

                // signal to customer they can start entering coins
                System.out.println("Please insert coins");
                
                PayWithCoins pwc = new PayWithCoins(this);
                station.coinValidator.attach(pwc);
                station.coinSlot.receive(coin);
                station.coinValidator.receive(coin);
                
                
            }
            
            //determines if the payment type is by card
            if (paymentType == TransactionRecord.CREDIT ||paymentType == TransactionRecord.DEBIT) {
            	station.cardReader.register(instance);
            	//checks for failure from the card reader to support bronze and silver implementations
            	try {
            		station.cardReader.swipe(card);
            	}catch(MagneticStripeFailureException e) {
            		System.out.print("Card Swipe Failed. Please Try Again.");
            	}
            	if (instance.getDataReadStatus()) {
            		long updatedAmount = instance.paymentType(card, cardIssuer, totalPrice, record);
            		//updates amount owed on the record and displays it
            		record.setAmountOwed(updatedAmount);
            		amountDue=updatedAmount;
            		System.out.printf("Amount owed: %d\n", updatedAmount);
            		
            		//if the new amount owed is 0 transaction is complete and receipt is called to be printed
            		if (updatedAmount == 0) {
            			printReceipt();
            			System.out.print("Please Take Your Receipt");
            		//if there is a remaining balance, the customer will be notified and told to pay the remaining balance
            		}else {
            			System.out.printf("Please Select Paymond Method. Pending Amount is: %d\n", updatedAmount);
                		
                	
            		}
            	}
            		
            }

        }
        // else, ignore pay request
     
        
    }
    

    public void printReceipt() {
	if (sessionState == PAY_FOR_BILL && amountDue==0) {
		sessionState = PRINTING_RECEIPT;
		
		
		endSession();
		}
		// else, do not transition
    }

    public void endSession() {
    	// assuming a receipt is printed for every session 
        if (sessionState == PRINTING_RECEIPT) {
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

    // updates total expected weight to the given mass
    public void updateTotalExpectedWeight(Mass mass) {
    	totalExpectedWeight = mass;
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
            checkoutState = UNLOCK;
        }
        // else, do nothing

    }
    
    public Session(AbstractSelfCheckoutStation station) {
    	this.station = station;
    }

    // getter methods
    public int getSessionState() {
        return sessionState;
    }

    public boolean getCheckoutState() {
        return checkoutState;
    }
    
    public Mass getTotalExpectedWeight() {
        return totalExpectedWeight;
    }

    public boolean isAddBagsSelected() {
        return addBagsSelected;
    }
}
