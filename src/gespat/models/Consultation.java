package models;

import utils.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Consultation extends AbstractData {

	private Patient patient;
	private String doctorName;
	private LocalDate consultedAt;
	private String[] diagnosedPathologies;
	private Device requiredEquipment;
	private String observations;

	/**
	 * Modèle de donnée représentant une consultation.
	 * Utiliser un <code>ConsultationManager</code> pour créer et enregistrer une consultation correctement.
	 *
	 * @param id                   identifiant de la consultation, doit être généré à partir du <code>StateManager</code>
	 * @param patient              le patient lié à la consultation, doit être enregistré dans le <code>PatientController</code>
	 * @param doctorName           nom du docteur ayant traité le patient
	 * @param consultedAt          date de la consultation
	 * @param diagnosedPathologies pathologies diagnostiquées
	 * @param requiredEquipment    equipment requis, null déconseiller
	 * @param observations         observations du docteur
	 * @see controllers.ConsultationController
	 * @see utils.StateManager
	 * @see controllers.PatientController
	 */
	public Consultation(
			int id,
			Patient patient,
			String doctorName,
			LocalDate consultedAt,
			String[] diagnosedPathologies,
			Device requiredEquipment,
			String observations
	) {
		this.id = id;
		this.patient = patient;
		this.doctorName = doctorName;
		this.consultedAt = consultedAt;
		this.diagnosedPathologies = diagnosedPathologies;
		this.requiredEquipment = requiredEquipment;
		this.observations = observations;
	}

	/**
	 * @return la date de consultation
	 */
	public LocalDate getConsultedAt() {
		return consultedAt;
	}

	/**
	 * @return les pathologies diagnostiquées
	 */
	public List<String> getDiagnosedPathologies() {
		if (diagnosedPathologies == null)
			return new ArrayList<>(Collections.emptyList());
		return new ArrayList<>(Arrays.asList(diagnosedPathologies));
	}

	/**
	 * @return le nom du docteur
	 */
	public String getDoctorName() {
		return doctorName;
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
	 * @return les champs parmi lesquels il est possible d'effectuer une recherche
	 */
	@Override
	public String getSearchableFields() {
		if (this.patient == null)
			return "xxx xxx";
		return this.patient.getFullname();
	}

	/**
	 * @param consultedAt la date de consultation à définir
	 */
	public void setConsultedAt(LocalDate consultedAt) {
		this.consultedAt = consultedAt;
	}

	/**
	 * @param diagnosedPathologies les pathologies diagnostiquées à définir
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
	 * @return les observations du docteur
	 */
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations les observations du docteur
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return la consultation sous forme de chaine de caractères prête pour l'enregistrement
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
				observations;
	}

}
