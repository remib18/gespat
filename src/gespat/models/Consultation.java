package models;

import utils.Date;
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
    private Device requiredEquipment;
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
            Device requiredEquipment,
            boolean granted
    ) {
        this.id = id;
        this.patient = patient;
        this.doctorName = doctorName;
        this.consultedAt = consultedAt;
        this.diagnosedPathologies = diagnosedPathologies;
        this.requiredEquipment = requiredEquipment;
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
        if (diagnosedPathologies == null)
            return new ArrayList<>(Arrays.asList(new String[0]));
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
    public Device getRequiredEquipment() {
        return requiredEquipment;
    }

    /**
     * @return les champs parmis lesquels il est possible d'effectuer une recherche
     */
    @Override
    public String getSearchableFields() {
        if (this.patient == null)
            return "xxx xxx";
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
     * @param requiredEquipment le matériel nécessaire à définir
     */
    public void setRequiredEquipment(Device requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    /**
     * @return la consultation sous forme de chaine de caractères prète pour l'enregistrement
     */
    @Override
    public String toString() {
        String consultedAt = this.consultedAt == null ? "null" : Date.convert(this.consultedAt);
        String diagnosedPathologies = this.diagnosedPathologies == null ? "null" : String.join("|", this.diagnosedPathologies);
        return id + fs +
               patient.getId() + fs +
               doctorName + fs +
               consultedAt + fs +
               diagnosedPathologies + fs +
               requiredEquipment + fs +
               granted;
    }

}
