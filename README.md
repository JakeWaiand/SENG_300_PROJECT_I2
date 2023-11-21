# SENG300 Group Project - Software & Software Testing Directories

This README provides an overview of the contents found in the `software` and `software testing` directories of the SENG300 Group Project. These directories contain the main application code and the associated unit tests.

## Software Directory (`com.thelocalmarketplace.software`)

### Source Code (`src`)

- **Main.java**
  - Description: This is the main entry point of the application. It initializes the application and sets up the necessary environment for it to run.

- **AddItemByBarcodeScanner.java**
  - Description: Handles the functionality of adding items to a shopping list or cart via a barcode scanner. It processes the scanned barcode and retrieves the corresponding item details.

- **PayViaCash.java**
  - Description: Implements the cash payment process. This class is responsible for handling transactions where the payment mode is cash.

- **State.java**
  - Description: Represents the various states of the application or a session. This could include states like 'Session Started', 'Item Added', 'Payment Initiated', etc.

- **WeightDiscrepancy.java**
  - Description: Manages scenarios where there is a discrepancy in the weight of an item. This might be used in cases where the weight measured does not match the expected weight of the item.

## Software Testing Directory (`com.thelocalmarketplace.software.test`)

### Unit Test Code (`src`)

- **TestAddItemByBarcode**
  - **TestAddItemByBarcode.java**
    - Description: Unit tests for `AddItemByBarcodeScanner.java`. Tests the functionality of adding items via barcode scanning.

- **TestPayViaCash**
  - **TestPayViaCash.java**
    - Description: Unit tests for `PayViaCash.java`. Verifies the correctness of cash payment processing.

- **TestStartSession**
  - **TestStartSession.java**
    - Description: Tests for session management, ensuring that session states are handled correctly.

- **TestWeightDiscrepancy**
  - **TestWeightDiscrepancy.java**
    - Description: Tests the functionality of the `WeightDiscrepancy.java` class, ensuring that weight discrepancies are identified and handled appropriately.

---