package utils;

import exceptions.ProcessingException;
import views.popups.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire pour la gestion de l'état de l'application.
 * Permet notamment la sauvegarde du dernier index ajouté pour chaque type de donnée.
 */
public class StateManager {

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final StateManager STATE = new StateManager();

	// Données sauvegardées dans un fichier :
	private Integer lastPatientIndexInserted;
	private Integer lastConsultationIndexInserted;
	private Integer lastDeviceIndexInserted;

	/**
	 * Utilisez la méthode <code>getState</code>
	 *
	 * @see StateManager#getState
	 */
	protected StateManager() {
		try {
			logger.log(Level.INFO, "Application State : loading...;");
			load();
			logger.log(Level.INFO, "Application State : loaded.");
		} catch (ProcessingException err) {
			new UserMessage("Erreur lors du chargement de l'application.", UserMessage.LEVEL.Severe);
			logger.log(Level.SEVERE, "Application State : error during loading.");
		}
	}

	public static StateManager getState() {
		return STATE;
	}

	/**
	 * Obtenir l'index de la prochaine insertion pour le modèle de donnée renseigné
	 *
	 * @param type modèle de donnée
	 * @return l'index
	 */
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
			default:
				return null;
		}
	}

	/**
	 * Une erreur s'est produite et la donnée n'a pas été insérée.
	 *
	 * @param type modèle de donnée
	 */
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

	/**
	 * Reinitialisation de l'index
	 *
	 * @param type modèle de donnée
	 */
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

	/**
	 * Charge l'état de l'application à partir du fichier
	 *
	 * @throws ProcessingException en cas de problème lors du chargement des fichiers
	 */
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

	/**
	 * Enregistre l'état de l'application dans un fichier
	 */
	private void save() {
		List<Integer> data = new ArrayList<>();
		data.add(lastPatientIndexInserted);
		data.add(lastConsultationIndexInserted);
		data.add(lastDeviceIndexInserted);
		try {
			new File<Integer>().saveData(data, "app-state.txt");
		} catch (ProcessingException e) {
			new UserMessage("Attention, une erreur système est survenue." +
					" En cas de fermeture de l'application, des données risquent d'être perdue.",
					UserMessage.LEVEL.Severe);
			logger.log(Level.SEVERE, "Erreur lors de l'enregistrement de l'état de l'application.");
		}
	}

	public enum DataType {Patient, Consultation, Device}
}
