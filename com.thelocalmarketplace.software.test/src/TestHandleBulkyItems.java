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
 * description: test case for handling bulky items
 */

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.software.Session;

public class TestHandleBulkyItems {

    private Session session;
    private BarcodedProduct bulkyProduct;
	private AbstractSelfCheckoutStation AbstractSelfCheckoutStation;
	private Barcode b1;

    @Before
    public void setUp() {
        session = new Session(AbstractSelfCheckoutStation);
        b1 = new Barcode(new Numeral[] { Numeral.one });
        bulkyProduct = new BarcodedProduct(b1, "Bulky Item", 10, 5);
    }

    @Test
    public void testDoNotBagWithBulkyItem() {
        session.startSession();
        session.startAdding();

        session.addToTotalExpectedWeight(bulkyProduct);
        assertEquals(Session.BILL_NOT_EMPTY, session.getSessionState());
        assertEquals(Session.UNLOCK, session.getCheckoutState());

        session.doNotBag();

        // Check if the state is correctly updated after choosing "do not bag"
        assertEquals(Session.BILL_NOT_EMPTY, session.getSessionState());
        assertEquals(Session.UNLOCK, session.getCheckoutState());

        // Check if the total expected weight is updated after choosing "do not bag"
        assertEquals(Mass.ZERO, session.getTotalExpectedWeight());
    }
}
