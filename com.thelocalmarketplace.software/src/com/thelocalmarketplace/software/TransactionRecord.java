/*
 * Favour Adah - 30140479
 * Farouq Arafeh - 30258214
 * Adam Attwood - 30185194
 * Lucas Carvalho - 30113633
 * Tiffany Hung - 10149429
 * Mann Patel - 30182233
 * Sneh Patel - 30086076
 * Arumakankani Sayuru Silva - 30190402
 * Jake Waiand - 30179510
 * 
 * Description: transaction record class keeps tract of everything on the bill.
 * 
 */

package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.thelocalmarketplace.hardware.BarcodedProduct;

public class TransactionRecord {

    // constants for payment types
    public static final int NONE = 0;
    public static final int COIN = 1;
    public static final int BANKNOTE = 2;
    public static final int CASH = 3;
    public static final int CREDIT = 4;
    public static final int DEBIT = 5;
    public static final int CRYPTO = 6;

    // default variables
    private static long totalPrice = 0;
    private static ArrayList<BarcodedProduct> bill = new ArrayList<BarcodedProduct>();
    private static long holdNumber = 0;
    private static int paymentType = NONE;
    private static BigDecimal amountOwed = new BigDecimal("0");

    // methods that modify total price
    public void addToTotalPrice(BarcodedProduct product) {
        totalPrice += product.getPrice();
    }

    public void removeFromTotalPrice(BarcodedProduct product) {
        totalPrice -= product.getPrice();
    }
    
    public void clearTotalPrice() {
        totalPrice = 0;
    }

    // methods that modify bill
    public void addToBill(BarcodedProduct product) {
        bill.add(product);
    }

    public void removeFromBill(BarcodedProduct product) {
        bill.remove(product);
    }

    public void clearBill() {
        bill.clear();
    }

    // methods that modify hold number
    public void updateHoldNumber(long number) {
        holdNumber = number;
    }

    public void clearHoldNumber() {
        holdNumber = 0;
    }

    // methods that modify payment type
    public void setPaymentToCoin() {
        paymentType = COIN;
    }

    public void setPaymentToBanknote() {
        paymentType = BANKNOTE;
    }

    public void setPaymentToCash() {
        paymentType = CASH;
    }

    public void setPaymentToCredit() {
        paymentType = CREDIT;
    }

    public void setPaymentToDebit() {
        paymentType = DEBIT;
    }

    public void setPaymentToCrypto() {
        paymentType = CRYPTO;
    }

    public void clearPaymentType() {
        paymentType = NONE;
    }

    // methods that modify amount owed
    public void setAmountOwed(long amount) {
        // convert long to BigDecimal
        amountOwed = new BigDecimal(amount);
    }

    public void clearAmountOwed() {
        amountOwed = new BigDecimal("0");
    }

    // getter methods
    public long getTotalPrice() {
        return totalPrice;
    }

    public int getBillItemCount() {
        return bill.size();
    }

    public BarcodedProduct getItemFromBill(int index) {
        return bill.get(index);
    }

    public long getHoldNumber() {
        return holdNumber;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }
}
