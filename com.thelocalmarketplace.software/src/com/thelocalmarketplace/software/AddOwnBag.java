package com.thelocalmarketplace.software;

public class AddOwnBag{

    private static final double ALLOWED_WEIGHT_LIMIT = 5.0; // Adjust as needed

    public void addOwnBagsProcess() {
        try {
            waitForCustomerInput();
            promptToAddBags();
            confirmBagsAdded();
            double weight = checkWeightChange();
            if (weight > ALLOWED_WEIGHT_LIMIT) {
                bagsTooHeavyExtension(weight);
            }
            signalToContinue();
        } catch (Exception e) {
            handleExceptions(e);
        }
    }

    private void waitForCustomerInput() {
        // Wait for customer to signal the desire to add their own bags
        // Implementation depends on the UI system
    }

    private void promptToAddBags() {
        // Indicate to the customer that they should add their own bags now
        // Implementation depends on the UI system
    }

    private void confirmBagsAdded() {
        // Wait for customer to signal that the bags have been added
        // Implementation depends on the UI system
    }

    private double checkWeightChange() {
        // Detect the weight change
        // This method should interface with the weight sensor
        // Return the detected weight
        return 0.0; // Placeholder
    }

    private void bagsTooHeavyExtension(double weight) {
        // Handle the case where the added bags are too heavy
        // Additional logic to be implemented as per requirement
    }

    private void signalToContinue() {
        // Signal to the customer that they may now continue
        // Implementation depends on the UI system
    }

    private void handleExceptions(Exception e) {
        // Handle any exceptions, such as the system not being ready to note weight discrepancies
        System.out.println("Exception occurred: " + e.getMessage());
        // Additional exception handling logic as needed
    }

    // Main method for demonstration purposes
    public static void main(String[] args) {
        AddOwnBag checkoutSystem = new AddOwnBag();
        System.out.println("AddOwnBag process started");
        checkoutSystem.addOwnBagsProcess();
        System.out.println("AddOwnBag process ended");
    }
}


