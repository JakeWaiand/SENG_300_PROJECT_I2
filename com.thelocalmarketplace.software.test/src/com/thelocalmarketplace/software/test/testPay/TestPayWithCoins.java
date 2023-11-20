package testPay;

import org.junit.After;
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
    private ByteArrayOutputStream outputStreamCaptor;

    @Before
    public void setUp() {
        selfCheckoutStation = new SelfCheckoutStation(); 
        coinValue = new BigDecimal("1.0");
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        State.setSession(true);
    }

    private void setupForStationType(String type) {
        payWithCoins = new PayWithCoins(selfCheckoutStation, type);
        selfCheckoutStation.coinValidator.attach(payWithCoins);
    }

    @Test
    public void testValidCoinDetectedGold() {
        setupForStationType("Gold");
        double initialAmountOwed = 5.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
        double expected = initialAmountOwed - coinValue.doubleValue();
        double actual = payWithCoins.getAmountOwed();
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testInvalidCoinDetectedGold() {
        setupForStationType("Gold");
        payWithCoins.setCustomerSelectedPayCoins(true, 5.0);
        payWithCoins.invalidCoinDetected(selfCheckoutStation.coinValidator);
        assertEquals("Invalid coin detected. Please try another coin.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testDispenseChangeGold() {
        setupForStationType("Gold");
        double initialAmountOwed = 3.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        BigDecimal overpaidValue = new BigDecimal("5.0");
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, overpaidValue);
        assertTrue(outputStreamCaptor.toString().trim().contains("Dispensing change"));
    }

    @Test
    public void testCompletePaymentGold() {
        setupForStationType("Gold");
        double initialAmountOwed = 1.0;
        payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
        payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
        assertTrue(outputStreamCaptor.toString().trim().contains("Receipt printed."));
    }

   @Test
public void testValidCoinDetectedSilver() {
    setupForStationType("Silver");
    double initialAmountOwed = 5.0;
    payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
    payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
    double expected = initialAmountOwed - coinValue.doubleValue();
    double actual = payWithCoins.getAmountOwed();
    assertEquals(expected, actual, 0.01);
}

@Test
public void testValidCoinDetectedBronze() {
    setupForStationType("Bronze");
    double initialAmountOwed = 5.0;
    payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
    payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, coinValue);
    double expected = initialAmountOwed - coinValue.doubleValue();
    double actual = payWithCoins.getAmountOwed();
    assertEquals(expected, actual, 0.01);
}

@Test
public void testDispenseChangeSilver() {
    setupForStationType("Silver");
    double initialAmountOwed = 2.0;
    payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
    payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, new BigDecimal("5.0"));
    assertTrue(outputStreamCaptor.toString().trim().contains("Dispensing change"));
}

@Test
public void testDispenseChangeBronze() {
    setupForStationType("Bronze");
    double initialAmountOwed = 2.0;
    payWithCoins.setCustomerSelectedPayCoins(true, initialAmountOwed);
    payWithCoins.validCoinDetected(selfCheckoutStation.coinValidator, new BigDecimal("5.0"));
    assertTrue(outputStreamCaptor.toString().trim().contains("Dispensing change"));
}
    @After
    public void tearDown() {
        System.setOut(System.out);
    }
}

