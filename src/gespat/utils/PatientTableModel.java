package utils;

import components.table.AbstractTableModel;
import controllers.PatientController;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;

import java.time.LocalDate;

public class PatientTableModel extends AbstractTableModel<Patient> {

    private static final long serialVersionUID = 7024694580497279586L;
    PatientController ctrl;

    /**
     * Crée un modèle de tableau pour la vue AdminDash
     */
    public PatientTableModel(PatientController patientController) {
        this.ctrl = patientController;
        this.headers.add("Nom");
        this.headers.add("Prénom");
        this.headers.add("Date de naissance");
        this.headers.add("Numéro de sécurité sociale");
        this.headers.add("Action");
    }

    /**
     * Définit les valeurs à la ligne <code>rowIndex</code> et la colone
     * <code>columnIndex</code>
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0)
            return this.getColumnName(columnIndex);
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
                    // Cas création d'un patient.
                    return "xx/xx/xxxx";
                }
            case 3:
                int socialId = data.get(rowIndex - 1).getSocialId();
                if (socialId == 0)
                    return "xxx";
                return String.valueOf(socialId);

            default:
                // TODO: Add actions
                int id = data.get(rowIndex - 1).getId();
                delete.addActionListener(e -> {
                    try {
                        ctrl.remove(ctrl.get(id), true);
                        setDataSet(ctrl.getAll());
                    } catch (NotFoundException | ProcessingException err) {
                        // TODO: Implement ErrorMessageView
                        System.err.println(
                                "[PATIENT TABLE MODEL – GET VALUE AT (ActionListener)]: Erreur lors du chargement des données / de la suppression.");
                    }
                });
                return delete;
        }
    }

}
