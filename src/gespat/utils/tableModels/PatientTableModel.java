package utils.tableModels;

import components.table.AbstractTableModel;
import controllers.PatientController;
import models.Patient;
import utils.Date;

import java.time.LocalDate;

/**
 * Modèle de tableau pour les patients
 */
public class PatientTableModel extends AbstractTableModel<Patient> {

	private static final long serialVersionUID = 7024694580497279586L;
	final PatientController ctrl;

	/**
	 * Crée un modèle de tableau pour la vue AdminDash
	 */
	public PatientTableModel(PatientController patientController) {
		this.ctrl = patientController;

		// Définition des entêtes
		this.headers.add("Nom");
		this.headers.add("Prénom");
		this.headers.add("Date de naissance");
		this.headers.add("Numéro de sécurité sociale");
	}

	/**
	 * Définit les valeurs à la ligne <code>rowIndex</code> et la colonne
	 * <code>columnIndex</code>
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Gestion des entêtes
		if (rowIndex == 0)
			return this.getColumnName(columnIndex);

		// Traitement, le test null/0 permet d'indiquer à l'utilisateur que la donnée est à remplir
		switch (columnIndex) {
			case 0:
				return data.get(rowIndex - 1).getLastname();
			case 1:
				return data.get(rowIndex - 1).getFirstname();
			case 2:
				try {
					LocalDate date = data.get(rowIndex - 1).getBirthAt();
					return Date.getDisplayString(date);
				} catch (NullPointerException err) {
					return "xx/xx/xxxx";
				}
			case 3:
				int socialId = data.get(rowIndex - 1).getSocialId();
				if (socialId == 0)
					return "xxx";
				return String.valueOf(socialId);

			default:
				return "###";
		}
	}

}
