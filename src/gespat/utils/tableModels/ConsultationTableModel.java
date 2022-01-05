package utils.tableModels;

import components.table.AbstractTableModel;
import models.Consultation;
import models.Patient;
import utils.Date;

import java.time.LocalDate;

/**
 * Modèle de tableau pour les consultations
 */
public class ConsultationTableModel extends AbstractTableModel<Consultation> {

	private static final long serialVersionUID = -958545185021886987L;

	/**
	 * Crée un modèle de tableau pour la vue DoctorDash
	 */
	public ConsultationTableModel() {
		// Définition des entêtes
		this.headers.add("Nom");
		this.headers.add("Date de consultation");
		this.headers.add("Numéro de sécurité sociale");
	}

	/**
	 * Définit les valeurs à la ligne <code>rowIndex</code> et la colonne <code>columnIndex</code>
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Cas des headers
		if (rowIndex == 0) return this.getColumnName(columnIndex);

		// Traitement, le test null permet d'indiquer à l'utilisateur que la donnée est à remplir
		Patient patient = data.get(rowIndex - 1).getPatient();
		switch (columnIndex) {
			case 0:
				if (patient == null)
					return "xxx xxx";
				return patient.getFullname();
			case 1:
				LocalDate consultedAt = data.get(rowIndex - 1).getConsultedAt();
				if (consultedAt == null)
					return "xx/xx/xxx";
				return Date.getDisplayString(consultedAt);
			case 2:
				if (patient == null)
					return "xxx";
				return String.valueOf(patient.getSocialId());

			default:
				// La colonne n'est pas censée être affichée donc on met un repère visuel
				return "###";
		}
	}

}
