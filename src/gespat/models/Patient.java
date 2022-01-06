package models;

import controllers.PatientController;
import utils.Date;

import java.time.LocalDate;

public class Patient extends AbstractData {

	private String firstname;
	private String lastname;
	private int socialId;
	private LocalDate birthAt;

	/**
	 * Crée un nouveau patient.
	 * Utiliser un PatientManager pour instancier et sauvegarder des patients.
	 *
	 * @see PatientController
	 */
	public Patient(int id, String firstname, String lastname, int socialId, LocalDate birthAt) {
		this.id = id;
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
	 * @param birthAt la date de naissance à définir
	 */
	public void setBirthAt(LocalDate birthAt) {
		this.birthAt = birthAt;
	}

	/**
	 * @return le prénom
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname le prénom à définir
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return le nom complet du patient
	 */
	public String getFullname() {
		return this.lastname + ' ' + this.firstname;
	}

	/**
	 * @return le nom de famille
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname le nom de famille à définir
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return les champs parmi lesquels il est possible d'effectuer une recherche
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
	 * @param socialId le numéro de sécurité sociale à définir
	 */
	public void setSocialId(int socialId) {
		this.socialId = socialId;
	}

	/**
	 * Renvoie le patient sous forme de chaine de caractères prêt pour la sauvegarde.
	 *
	 */
	@Override
	public String toString() {
		final String date = birthAt == null ? "null" : Date.convert(this.birthAt);
		return this.id + fs + this.lastname + fs + this.firstname + fs + this.socialId + fs + date;
	}

}
