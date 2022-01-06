package models;

import controllers.ConsultationController;
import utils.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Consultation extends AbstractData {

	private final Patient patient;
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

	public LocalDate getConsultedAt() {
		return consultedAt;
	}

	public void setConsultedAt(LocalDate consultedAt) {
		this.consultedAt = consultedAt;
	}

	public List<String> getDiagnosedPathologies() {
		if (diagnosedPathologies == null)
			return new ArrayList<>(Collections.emptyList());
		return new ArrayList<>(Arrays.asList(diagnosedPathologies));
	}

	public void setDiagnosedPathologies(String[] diagnosedPathologies) {
		this.diagnosedPathologies = diagnosedPathologies;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public Device getRequiredEquipment() {
		return requiredEquipment;
	}

	public void setRequiredEquipment(Device requiredEquipment) {
		this.requiredEquipment = requiredEquipment;
	}

	@Override
	public String getSearchableFields() {
		if (this.patient == null)
			return "xxx xxx";
		return this.patient.getFullname();
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

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
