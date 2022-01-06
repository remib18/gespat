package controllers;

import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Consultation;
import models.Device;
import models.Patient;
import utils.Regex;
import utils.StateManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationController extends AbstractController<Consultation> {

	/** Instance du <code>PatientController</code> */
	private final PatientController patientCtrl;

	/** Instance du <code>DeviceController</code> */
	private final DeviceController deviceCtrl;

	/**
	 * Crée un objet permettant la manipulation des consultations
	 *
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public ConsultationController(PatientController patientCtrl, DeviceController deviceCtrl)
			throws ProcessingException {
		this.patientCtrl = patientCtrl;
		this.deviceCtrl = deviceCtrl;
		storeFile = "consultations.txt";
		stateDataType = StateManager.DataType.Patient;

		load();
	}

	/**
	 * Crée une nouvelle consultation
	 *
	 * @param patient              le patient lié à la consultation
	 * @param consultedAt          la date de la consultation
	 * @param doctorName           le nom du docteur
	 * @param diagnosedPathologies les pathologies diagnostiquées
	 * @param requiredEquipment    l'équipement nécessaire
	 * @param observations         les observations
	 * @return                     la consultation
	 * @throws ConflictingDataException si la donnée existe déjà
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public Consultation add(
			Patient patient,
			String doctorName,
			LocalDate consultedAt,
			String[] diagnosedPathologies,
			Device requiredEquipment,
			String observations
	) throws ConflictingDataException, ProcessingException {
		Device device = requiredEquipment == null ? deviceCtrl.add(null, Device.STATES.UNDEFINED) : requiredEquipment;
		return add(new Consultation(
				StateManager.getState().getNextInsertionIndex(StateManager.DataType.Consultation),
				patient,
				doctorName,
				consultedAt,
				diagnosedPathologies,
				device,
				observations
		));
	}

	/**
	 * @return l'ensemble des pathologies enregistrées
	 */
	public List<String> getAllPathologies() {
		final List<String> pathologies = new ArrayList<>();
		for (Consultation consultation : data) {
			for (String pathology : consultation.getDiagnosedPathologies()) {
				if (!pathologies.contains(pathology)) {
					pathologies.add(pathology);
				}
			}
		}
		return pathologies;
	}

	/**
	 * Retourne l'ensemble des consultations liées à un patient
	 * @param  patient le patient
	 * @return les consultations liées
	 */
	public List<Consultation> getAll(Patient patient) {
		List<Consultation> consultations = new ArrayList<>();
		for (Consultation c : data) {
			if (c.getPatient().equals(patient))
				consultations.add(c);
		}
		return consultations;
	}

	/**
	 * Supprime des consultations en batterie
	 *
	 * @param consultations la liste des consultations à supprimer
	 * @throws NotFoundException si une consultation n'existe pas
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	void batchRemove(List<Consultation> consultations) throws NotFoundException, ProcessingException {
		for (Consultation consultation : consultations) {
			remove(consultation, true);
		}
	}

	@Override
	protected Consultation makeObjectFromString(String[] object)
			throws NotFoundException, NumberFormatException {
		String[] device = {object[5], object[6], object[7]};
		List<String> pathologies = Regex.getMatches("[^|]+", object[4]);
		//noinspection EmpryBranchInAlteration
		return new Consultation(
				Integer.parseInt(object[0]),
				patientCtrl.get(Integer.parseInt(object[1])),
				object[2],
				LocalDate.parse(object[3]),
				pathologies.toArray(new String[0]),
				deviceCtrl.makeObjectFromString(device),
				object[8]
		);
	}
}
