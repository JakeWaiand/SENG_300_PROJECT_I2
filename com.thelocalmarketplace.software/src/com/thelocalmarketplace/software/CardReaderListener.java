package com.thelocalmarketplace.software;

public interface CardReaderListener {
	
	public default void aDeviceHasBeenEnabled() {}
	
	public default void aDeviceHasBeenDisabled() {}
	
	public default void aDeviceHasBeenTurnedOn() {}
	
	public default void aDeviceHasBeenTurnedOff() {}
	
	public default void aCardHasBeenSwiped() {}
	
	public default void theDataFromTheCardHasBeenRead() {}

}
