package utils;

import exceptions.ProcessingException;
import views.popups.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateManager {

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final StateManager STATE = new StateManager();

	public enum DataType { Patient, Consultation, Device }

	private Integer lastPatientIndexInserted;
	private Integer lastConsultationIndexInserted;
	private Integer lastDeviceIndexInserted;

	public static StateManager getState() {
		return STATE;
	}

	public Integer getNextInsertionIndex(DataType type) {
		switch (type) {
			case Patient:
				lastPatientIndexInserted++;
				save();
				return lastPatientIndexInserted;
			case Consultation:
				lastConsultationIndexInserted++;
				save();
				return lastConsultationIndexInserted;
			case Device:
				lastDeviceIndexInserted++;
				save();
				return lastDeviceIndexInserted;
			default: return null;
		}
	}

	public void narrowNextInsertionIndex(DataType type) {
		switch (type) {
			case Patient:
				lastPatientIndexInserted--;
			case Consultation:
				lastConsultationIndexInserted--;
			case Device:
				lastDeviceIndexInserted--;
		}
		save();
	}

	public void clearData(DataType type) {
		switch (type) {
			case Patient:
				lastPatientIndexInserted = 0;
			case Consultation:
				lastConsultationIndexInserted = 0;
			case Device:
				lastDeviceIndexInserted = 0;
		}
		save();
	}

	protected StateManager() {
		try {
			logger.log(Level.INFO, "Application State : loading...;");
			load();
			logger.log(Level.INFO, "Application State : loaded.");
		} catch (ProcessingException err) {
			new ErrorMessage("Erreur lors du chargement de l'application.", ErrorMessage.LEVEL.System);
			logger.log(Level.SEVERE, "Application State : error during loading.");
		}
	}

	private void load() throws ProcessingException {
		String[][] dataPats = new File<Integer>().getData("app-state.txt");
		if (dataPats == null) {
			lastPatientIndexInserted = 0;
			lastConsultationIndexInserted = 0;
			lastDeviceIndexInserted = 0;
			return;
		}
		lastPatientIndexInserted = Integer.parseInt(dataPats[0][0]);
		lastConsultationIndexInserted = Integer.parseInt(dataPats[1][0]);
		lastDeviceIndexInserted = Integer.parseInt(dataPats[2][0]);
	}

	private void save() {
		List<Integer> data = new ArrayList<>();
		data.add(lastPatientIndexInserted);
		data.add(lastConsultationIndexInserted);
		data.add(lastDeviceIndexInserted);
		try {
			new File<Integer>().saveData(data, "app-state.txt");
		} catch (ProcessingException e) {
			new ErrorMessage("Attention, une erreur système est survenue." +
					" En cas de fermeture de l'application, des données risquent d'être perdue.",
					ErrorMessage.LEVEL.System);
			logger.log(Level.SEVERE, "Erreur lors de l'enregistrement de l'état de l'application.");
		}
	}
}
