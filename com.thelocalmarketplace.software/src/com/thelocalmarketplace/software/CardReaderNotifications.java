package com.thelocalmarketplace.software;


import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.external.CardIssuer;

public class CardReaderNotifications implements CardReaderListener {
	
	private boolean cardDataRead = false;
	
	private Scanner scanner = new Scanner(System.in);
	
	/**
	 * Makes sure the card type is supported, sends details to bank, updates the hold number in the record
	 * and returns the amount due after the payment is processed
	 * 
	 * @param card
	 * 		the card swiped
	 * @param cardIssuer
	 * 		the cardIssuer of the card swiped
	 * @param totalPrice
	 * 		the total amount due
	 * @param record
	 * 		the transaction record for the current session
	 * @return totalPrice if card or payment fails; otherwise return 0 if the payment is successful
	 * */
	
	public long paymentType(Card card, CardIssuer cardIssuer, long totalPrice, TransactionRecord record) {
		//Verifies the card type to make sure it is debit or credit **EXCLUSIVE TO OUR ITERATION**
		if(card.kind == "Visa" || card.kind == "Mastercard"||card.kind == "Debit") {
			//prompts the customer to provide their signature. 
			//In this simulation the signature is to be the same as the card holder name
			System.out.print("Please enter your signature below: ");
			String signature = scanner.nextLine();
			//sends details to the bank to be verified and receive hold number
			long holdNumber = sendCardDetailsToBank(card, cardIssuer, totalPrice, signature);
			
			//holdNumber returned will be -1 if the hold fails
			if (holdNumber == -1) {
				System.out.print("Hold not authorized. Please use a different payment method or try again.");
				//returns the unchanged amount due
				return totalPrice;
			}
			//the hold number is updated on the record
			record.updateHoldNumber(holdNumber);
			
			//transaction is called to be posted
			boolean transactionPosted = cardIssuer.postTransaction(card.number, holdNumber, totalPrice);
			
			if (transactionPosted) {
				//notifies the system that the transaction is successful
				transactionSuccessful();
				//notifies the customer that the transaction is successful
				System.out.print("Transaction Successful");
				//returns 0 as the updated amount because amount due is cleared
				return 0;
					
			}else {
				//notifies the system that the transaction is unsuccessful
				transactionUnsuccessful();
				//notifies the customer too
				System.out.print("Transaction Unsuccessful. Please use a different payment method or try again.");
				//returns the unchanged amount due
				return totalPrice;
			}
		}
		System.out.print("Invalid card type detected. Please use a different card or select another payment method.");
		//if card is invalid type will return the amount due unchanged
		return totalPrice;
	}
	
	/**
	 * Verifies signature and places a hold on the card, returns a hold number
	 * 
	 * @param card
	 * 		The card swiped
	 * @param cardIssuer
	 * 		The card issuer of the card swiped
	 * @param totalPrice
	 * 		The total amount due
	 * @param signature
	 * 		The signature provided by the customer
	 * @return -1 if hold fails or signature is invalid; otherwise return hold number given by the card issuer
	 * */
	
	private long sendCardDetailsToBank(Card card, CardIssuer cardIssuer, long totalPrice, String signature) {
		//verifies the signature by checking if it is the same as the card holder name
		if(card.cardholder == signature) {
			//attempts to authorize the hold
			long holdNumber = cardIssuer.authorizeHold(card.number, totalPrice);
			return holdNumber;
		}else {
			//return -1 if signature is invalid and above method will treat it the same as an unauthorized hold
			return -1;
		}
	
		
	}
	


	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenSwiped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theDataFromACardHasBeenRead(CardData data) {
		// TODO Auto-generated method stub
		cardDataRead = true;
		
	}
	
	public void transactionSuccessful() {
		
	}
	
	public void transactionUnsuccessful() {
		
	}
	
	public boolean getDataReadStatus() {
		return this.cardDataRead;
	}
	
	

}
