package com.thelocalmarketplace.software;

import java.io.IOException;
import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderListener;
import com.jjjwelectronics.card.CardReaderSilver;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.external.CardIssuer;

public class CardReaderNotifications implements CardReaderListener {
	private CardReaderGold goldReader = new CardReaderGold();
	private CardReaderSilver silverReader = new CardReaderSilver();
	private CardReaderBronze bronzeReader = new CardReaderBronze();
	
	private boolean cardDataRead = false;
	
	private Scanner scanner = new Scanner(System.in);
	
	private long paymentType(Card card, CardIssuer cardIssuer, long totalPrice, TransactionRecord record) {
		if(card.kind == "Visa" || card.kind == "Mastercard"||card.kind == "Debit") {
			
			System.out.print("Please enter your signature below: ");
			
			String signature = scanner.nextLine();
			long holdNumber = sendCardDetailsToBank(card, cardIssuer, totalPrice, signature);
			
			if (holdNumber == -1) {
				System.out.print("Hold not authorized. Please use a different payment method or try again.");
				return totalPrice;
			}
			
			record.updateHoldNumber(holdNumber);
			
			boolean transactionPosted = cardIssuer.postTransaction(card.number, holdNumber, totalPrice);
			
			if (transactionPosted) {
				transactionSuccessful();
				
				System.out.print("Transaction Successful");

				totalPrice = 0;
				return totalPrice;
					
			}else {
				transactionUnsuccessful();
				
				System.out.print("Transaction Unsuccessful. Please use a different payment method or try again.");
				return totalPrice;
			}
		}
		return totalPrice;
	}
	
	private long sendCardDetailsToBank(Card card, CardIssuer cardIssuer, long totalPrice, String signature) {
		if(card.cardholder == signature) {
			long holdNumber = cardIssuer.authorizeHold(card.number, totalPrice);
			return holdNumber;
		}else {
			return -1;
		}
	
		
	}
	
	
	
	public long cardSwipeGold(Card card, CardIssuer cardIssuer, long totalPrice, TransactionRecord record) throws IOException {
		goldReader.register(this);
		goldReader.swipe(card);
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice, record);
			return updatedAmount;
		}else {
			return totalPrice;
			//should never happen
		}
		
	
		
	}
    public long cardSwipeSilver(Card card, CardIssuer cardIssuer, long totalPrice, TransactionRecord record) throws IOException {
    	silverReader.register(this);
    	try {
    		silverReader.swipe(card);
    	}catch (MagneticStripeFailureException e) {
    		System.out.print("Card Swipe Failed. Please Try Again.");
    		return totalPrice;
    	}
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice, record);
			return updatedAmount;
		}else {
			return totalPrice;
			//should never happen
		}
		
	}
    public long cardSwipeBronze(Card card, CardIssuer cardIssuer, long totalPrice, TransactionRecord record) throws IOException {
    	bronzeReader.register(this);
    	try {
    		bronzeReader.swipe(card);
    	}catch (MagneticStripeFailureException e) {
    		System.out.print("Card Swipe Failed. Please Try Again.");
    		return totalPrice;
    	}
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice, record);
			return updatedAmount;
		}else {
			return totalPrice;
			//should never happen
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
	
	

}
