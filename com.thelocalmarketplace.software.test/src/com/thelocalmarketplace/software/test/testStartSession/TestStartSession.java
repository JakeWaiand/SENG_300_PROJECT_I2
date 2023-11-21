/*
 * @author Enioluwafe Balogun - 30174298
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 * @author Rebecca Hamshaw - 30191086
 */
package testStartSession;

import org.junit.*;
import static org.junit.Assert.*;

import com.thelocalmarketplace.software.Main;
import com.thelocalmarketplace.software.State;


public class TestStartSession {
	@Before
	public void setup() {
		// no setup required
	}

	// calls on startSession when session is started or not have same input.
	// this method does not support ending session.
	@Test
	public void testStartSession() {
		Main.startSession();
		assertTrue(State.isSession());
	}
}
