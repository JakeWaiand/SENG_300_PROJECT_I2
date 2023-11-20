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
import com.thelocalmarketplace.hardware.external.CardIssuer;

public class CardReaderNotifications implements CardReaderListener {
	private CardReaderGold goldReader = new CardReaderGold();
	private CardReaderSilver silverReader = new CardReaderSilver();
	private CardReaderBronze bronzeReader = new CardReaderBronze();
	
	private boolean cardDataRead = false;
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static long paymentType(Card card, CardIssuer cardIssuer, long totalPrice) {
		if(card.kind == "Visa" || card.kind == "Mastercard"||card.kind == "Debit") {
			System.out.print("Please enter your signature below: ");
			String signature = scanner.nextLine();
			long holdNumber = sendCardDetailsToBank(card, cardIssuer, totalPrice, signature);
			//update transaction record
			boolean transactionPosted = cardIssuer.postTransaction(card.number, holdNumber, totalPrice);
			if (transactionPosted) {
				totalPrice = 0;
				return totalPrice;
				
				
			}
		}
		return totalPrice;
	}
	
	private static long sendCardDetailsToBank(Card card, CardIssuer cardIssuer, long totalPrice, String signature) {
		if(card.cardholder == signature) {
			long holdNumber = cardIssuer.authorizeHold(card.number, totalPrice);
			return holdNumber;
		}else {
			return -1;
		}
	
		
	}
	
	public long cardSwipeGold(Card card, CardIssuer cardIssuer, long totalPrice) throws IOException {
		goldReader.register(this);
		goldReader.swipe(card);
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice);
			return updatedAmount;
		}else {
			return totalPrice;
		}
		
		/*
		 * TODO:
		 * -add record parameter to update hold number
		 * */
		
	}
    public long cardSwipeSilver(Card card, CardIssuer cardIssuer, long totalPrice) throws IOException {
    	silverReader.register(this);
    	silverReader.swipe(card);
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice);
			return updatedAmount;
		}else {
			return totalPrice;
		}
		
	}
    public long cardSwipeBronze(Card card, CardIssuer cardIssuer, long totalPrice) throws IOException {
    	bronzeReader.register(this);
    	bronzeReader.swipe(card);
		if (cardDataRead) {
			long updatedAmount = paymentType(card, cardIssuer, totalPrice);
			return updatedAmount;
		}else {
			return totalPrice;
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
	
	

}
