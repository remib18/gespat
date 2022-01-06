package controllers;

import exceptions.ConflictingDataException;
import exceptions.ProcessingException;
import models.Device;
import utils.StateManager;

public class DeviceController extends AbstractController<Device> {

	/** Liste des appareils disponibles */
	public static final String[] DEVICES = {"Béquille / Canne", "Béquilles (paire)", "Chaise roulant", "Prothèse"};

	/**
	 * Crée un objet permettant la manipulation des appareils
	 *
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public DeviceController() throws ProcessingException {
		storeFile = "devices.txt";
		stateDataType = StateManager.DataType.Device;

		load();
	}

	/**
	 * Crée un appareil
	 *
	 * @param label nom de l'appareil
	 * @param state état d'attribution
	 * @return l'appareil
	 * @throws ConflictingDataException si la donnée existe déjà
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public Device add(String label, Device.STATES state) throws ConflictingDataException, ProcessingException {
		return add(new Device(
				StateManager.getState().getNextInsertionIndex(StateManager.DataType.Device),
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
