package controllers;

import exceptions.ConflictingDataException;
import exceptions.ProcessingException;
import models.Device;

public class DeviceController extends AbstractController<Device> {

	public static final String[] DEVICES = {"Béquille / Canne", "Béquilles (paire)", "Chaise roulant", "Prothèse"};

	public DeviceController() throws ProcessingException {
		storeFile = "devices.txt";

		load();
	}

	public Device add(Device.STATES state, String label) throws ConflictingDataException, ProcessingException {
		return add(new Device(
				getLastInsertedIndex() + 1,
				state,
				label
		));
	}

	@Override
	protected Device makeObjectFromString(String[] object) throws NumberFormatException {
		return new Device(
				Integer.parseInt(object[0]),
				Device.STATES.valueOf(object[1]),
				object[2]
		);
	}
}
