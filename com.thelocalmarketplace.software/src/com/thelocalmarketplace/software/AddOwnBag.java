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
 * Description: add own bag class simulates possible actions that occur
 * when a customer chooses to add their own bags during a session.
 */

package com.thelocalmarketplace.software;

public class AddOwnBag {

    private static final double ALLOWED_WEIGHT_LIMIT = 5.0; // Adjust as needed
    private boolean isSystemReady = true; // Placeholder for system readiness check

    public void addOwnBagsProcess() {
        if (!isSystemReady) {
            System.out.println("System is not ready to note weight discrepancies.");
            return;
        }

        try {
            waitForCustomerInput();
            promptToAddBags();
            confirmBagsAdded();
            double weight = checkWeightChange();
            if (weight > ALLOWED_WEIGHT_LIMIT) {
                bagsTooHeavyExtension(weight);
            } else {
                signalToContinue();
            }
        } catch (Exception e) {
            handleExceptions(e);
        }
    }

    private void waitForCustomerInput() {
        // Implementation: Wait for customer to signal the desire to add their own bags
        System.out.println("Waiting for customer input...");
    }

    private void promptToAddBags() {
        // Implementation: Indicate to the customer that they should add their own bags now
        System.out.println("Please add your bags now.");
    }

    private void confirmBagsAdded() {
        // Implementation: Wait for customer to signal that the bags have been added
        System.out.println("Confirming bags have been added...");
    }

    private double checkWeightChange() {
        // Implementation: Interface with the weight sensor to detect weight change
        double detectedWeight = AddItemByBarcodeScanner.expectedWeight;
        System.out.println("Detected weight change: " + detectedWeight + " kg");
        return detectedWeight;
    }

    private void bagsTooHeavyExtension(double weight) {
        // Handle the case where the added bags are too heavy
        System.out.println("Bags are too heavy! Weight: " + weight + " kg");
    }

    private void signalToContinue() {
        // Signal to the customer that they may now continue
        System.out.println("All set! You may continue.");
    }

    private void handleExceptions(Exception e) {
        // Handle any exceptions
        System.out.println("Exception occurred: " + e.getMessage());
    }

    // Main method for demonstration purposes
    public static void main(String[] args) {
        AddOwnBag checkoutSystem = new AddOwnBag();
        System.out.println("AddOwnBag process started");
        checkoutSystem.addOwnBagsProcess();
        System.out.println("AddOwnBag process ended");
    }
}
