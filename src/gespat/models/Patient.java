package models;

import controllers.PatientController;
import utils.Date;
import utils.File;

import java.time.LocalDate;

public class Patient extends Data {

    private String firstname;
    private String lastname;
    private int socialId;
    private LocalDate birthAt;

    /**
     * Crée un nouveau patient.
     * Utiliser un PatientManager pour instancier et sauvegarder des patients.
     * @see PatientController
     *
     * @param firstname
     * @param lastname
     * @param socialId
     * @param birthAt
     */
    public Patient(String firstname, String lastname, int socialId, LocalDate birthAt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.socialId = socialId;
        this.birthAt = birthAt;
    }

    /**
     * @return la date de naissance
     */
    public LocalDate getBirthAt() {
        return birthAt;
    }

    /**
     * @return le prénom
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @return le nom complet du patient
     */
    public String getFullname() {
        return this.lastname + ' ' + this.firstname;
    }

    /**
     * @return l'identifiant du patient (son numéro de sécurité sociale en somme)
     */
    @Override
    public int getId() {
        return socialId;
    }

    /**
     * @return le nom de famille
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @return les champs parmis lesquels il est possible d'effectuer une recherche
     */
    @Override
    public String getSearchableFields() {
        return this.getFullname();
    }

    /**
     * @return le numéro de sécurité sociale
     */
    public int getSocialId() {
        return socialId;
    }

    /**
     * @param birthAt la date de naissance à définir
     */
    public void setBirthAt(LocalDate birthAt) {
        this.birthAt = birthAt;
    }

    /**
     * @param firstname le prénom à définir
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @param lastname le nom de famille à définir
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @param socialId le numéro de sécurité sociale à définir
     */
    public void setSocialId(int socialId) {
        this.socialId = socialId;
    }

    /**
     * Renvoie le patient sous forme de chaine de caractères prèt pour la sauvegarde.
     * @return
     */
    @Override
    public String toString() {
        final String fs = File.COLUMN_SEPARATOR;
        return this.lastname + fs + this.firstname + fs + this.socialId + fs + Date.convert(this.birthAt);
    }

}
