/*
 * Favour Adah - 30140479
 * Farouq Arafeh - 30258214
 * Adam Attwood - 30185194
 * Lucas Carvalho - 30113633
 * Tiffany Hung - 10149429
 * Mann Patel - 30182233
 * Arumakankani Sayuru Silva - 30190402
 * Jake Waiand - 30179510
 * 
 * description: test case for add own bag use case
 */

import com.thelocalmarketplace.software.AddOwnBag;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

public class TestAddOwnBags {

    @Test
    public void testAddOwnBagsProcess() throws Exception {
        AddOwnBag checkoutSystem = new AddOwnBag();

        invokePrivateMethod(checkoutSystem, "waitForCustomerInput");
        invokePrivateMethod(checkoutSystem, "promptToAddBags");
        invokePrivateMethod(checkoutSystem, "confirmBagsAdded");

        double mockWeight = 4.0;
        assertEquals(mockWeight, invokePrivateMethodWithReturnValue(checkoutSystem, "checkWeightChange"), 0.01);

        double heavyWeight = 6.0;
        try {
            invokePrivateMethod(checkoutSystem, "bagsTooHeavyExtension", heavyWeight);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            // Expected exception, do nothing
        }

        double normalWeight = 3.0;
        try {
            invokePrivateMethod(checkoutSystem, "bagsTooHeavyExtension", normalWeight);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        invokePrivateMethod(checkoutSystem, "signalToContinue");
    }

    private void invokePrivateMethod(AddOwnBag instance, String methodName, Object... args) throws Exception {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        Method method = AddOwnBag.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        method.invoke(instance, args);
    }

    private double invokePrivateMethodWithReturnValue(AddOwnBag instance, String methodName, Object... args) throws Exception {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        Method method = AddOwnBag.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return (double) method.invoke(instance, args);
    }
}
