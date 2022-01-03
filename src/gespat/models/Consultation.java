package models;

import utils.Date;
import utils.File;
import controllers.ConsultationController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Consultation extends Data {

    private int id;
    private Patient patient;
    private String doctorName;
    private LocalDate consultedAt;
    private String[] diagnosedPathologies;
    private String requiredEquiment;
    private boolean granted;

    /**
     * Nouvelle consultation.
     * Utiliser un ConsultationManager pour créer et enregistrer une consultation correctement.
     * @see ConsultationController
     *
     * @param id
     * @param patient
     * @param doctorName
     * @param consultedAt
     * @param diagnosedPathologies
     * @param requiredEquipment
     * @param granted
     */
    public Consultation(
            int id,
            Patient patient,
            String doctorName,
            LocalDate consultedAt,
            String[] diagnosedPathologies,
            String requiredEquipment,
            boolean granted
    ) {
        this.id = id;
        this.patient = patient;
        this.doctorName = doctorName;
        this.consultedAt = consultedAt;
        this.diagnosedPathologies = diagnosedPathologies;
        this.requiredEquiment = requiredEquipment;
        this.granted = granted;
    }

    /**
     * @return la date de consultation
     */
    public LocalDate getConsultedAt() {
        return consultedAt;
    }

    /**
     * @return les pathologies diagnostiqués
     */
    public List<String> getDiagnosedPathologies() {
        return new ArrayList<>(Arrays.asList(diagnosedPathologies));
    }

    /**
     * @return le nom du docteur
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @return l'identifiant de la consultation
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @return le patient correspondant
     */
    public Patient getPatient() {
        return this.patient;
    }

    /**
     * @return le matériel nécessaire
     */
    public String getRequiredEquiment() {
        return requiredEquiment;
    }

    /**
     * @return les champs parmis lesquels il est possible d'effectuer une recherche
     */
    @Override
    public String getSearchableFields() {
        return this.patient.getFullname();
    }

    /**
     * @return le statut de l'atribution du matériel
     */
    public boolean isGranted() {
        return granted;
    }

    /**
     * @param consultedAt la date de consultation à définir
     */
    public void setConsultedAt(LocalDate consultedAt) {
        this.consultedAt = consultedAt;
    }

    /**
     * @param diagnosedPathologies les pathologies diagnostiqués à définir
     */
    public void setDiagnosedPathologies(String[] diagnosedPathologies) {
        this.diagnosedPathologies = diagnosedPathologies;
    }

    /**
     * @param doctorName le nom du docteur à définir
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * @param granted le statut de l'atribution du matériel à définir
     */
    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    /**
     * @param patient le patient correspondant à définir
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * @param requiredEquiment le matériel nécessaire à définir
     */
    public void setRequiredEquiment(String requiredEquiment) {
        this.requiredEquiment = requiredEquiment;
    }

    /**
     * @return la consultation sous forme de chaine de caractères prète pour l'enregistrement
     */
    @Override
    public String toString() {
        final String fs = File.COLUMN_SEPARATOR;
        return this.id + fs +
                this.patient.getId() + fs +
                this.doctorName + fs +
                Date.convert(this.consultedAt) + fs +
                String.join("|", this.diagnosedPathologies) + fs +
                this.requiredEquiment + fs +
                this.granted;
    }

}
