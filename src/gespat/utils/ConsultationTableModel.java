package utils;

import components.table.AbstractTableModel;
import models.Consultation;
import models.Patient;

public class ConsultationTableModel extends AbstractTableModel<Consultation> {

    private static final long serialVersionUID = -958545185021886987L;

    /**
     * Crée un modèle de tableau pour la vue DoctorDash
     */
    public ConsultationTableModel() {
        this.headers.add("Nom");
        this.headers.add("DateUtil de consultation");
        this.headers.add("Numéro de sécurité sociale");
        this.headers.add("Action");
    }

    /**
     * Définit les valeurs à la ligne <code>rowIndex</code> et la colone <code>columnIndex</code>
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) return this.getColumnName(columnIndex);
        Patient patient = data.get(rowIndex - 1).getPatient();
        switch (columnIndex) {
            case 0:
                return patient.getFullname();
            case 1:
                return data.get(rowIndex - 1).getConsultedAt().toString();
            case 2:
                return String.valueOf(patient.getSocialId());

            default:
                return data.get(rowIndex - 1).getId();
        }
    }

}
