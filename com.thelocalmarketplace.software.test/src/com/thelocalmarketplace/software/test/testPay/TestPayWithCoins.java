package testPay;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.PayWithCoins;
import com.thelocalmarketplace.software.State;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

public class TestPayWithCoins {
    private PayWithCoins payWithCoins;
    private SelfCheckoutStation selfCheckoutStation;
    private BigDecimal coinValue;

    @Before
    public void setUp() {
       
        selfCheckoutStation = new SelfCheckoutStation(); // Assuming constructor exists
        coinValue = new BigDecimal("1.0");
        payWithCoins = new PayWithCoins(selfCheckoutStation);
        selfCheckoutStation.coinValidator.attach(payWithCoins);
        State.setSession(true);
    }

    @Test
    public void testValidCoinDetected() {
        double initialAmountOwed = 5.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
        double expected = initialAmountOwed - coinValue.doubleValue();
        double actual = payWithCoins.getAmountOwed();
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testInvalidCoinDetected() {
        double initialAmountOwed = 5.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        payWithCoins.invalidCoinDetected(selfCheckoutStation.coinValidator);
        double actual = payWithCoins.getAmountOwed();
        assertEquals(initialAmountOwed, actual, 0.01);
    }

    @Test
    public void testDispenseChange() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        double initialAmountOwed = 3.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        BigDecimal overpaidValue = new BigDecimal("5.0");
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, overpaidValue);
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Dispensing change"));

        PrintStream standardOut = System.out;
        System.setOut(standardOut);
    }

    @Test
    public void testCompletePayment() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        double initialAmountOwed = 1.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Receipt printed."));

        PrintStream standardOut = System.out;
        System.setOut(standardOut);
    }

 
}
