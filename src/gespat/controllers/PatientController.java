package controllers;

import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;
import utils.StateManager;

import java.time.LocalDate;

public class PatientController extends AbstractController<Patient> {

	/** Instance du <code>ConsultationController</code> */
	private ConsultationController consultCtrl = null;

	/**
	 * Crée un objet permettant la manipulation des consultations
	 *
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public PatientController() throws ProcessingException {
		storeFile = "patients.txt";
		stateDataType = StateManager.DataType.Patient;

		load();
	}

	/**
	 * Permet de définir le <code>ConsultationController</code>
	 *
	 * @param consultCtrl le <code>ConsultationController</code>
	 * @see ConsultationController
	 */
	public void setConsultCtrl(ConsultationController consultCtrl) {
		this.consultCtrl = consultCtrl;
	}

	/**
	 * Crée un nouveau patient
	 *
	 * @param firstname prénom
	 * @param lastname nom de famille
	 * @param socialId numéro de sécurité sociale
	 * @param birthAt  date de naissance
	 * @return le patient
	 * @throws ConflictingDataException si la donnée existe déjà
	 * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
	 */
	public Patient add(String firstname, String lastname, int socialId, LocalDate birthAt) throws ConflictingDataException, ProcessingException {
		return super.add(new Patient(
				StateManager.getState().getNextInsertionIndex(StateManager.DataType.Patient),
				firstname,
				lastname,
				socialId,
				birthAt
		));
	}

	@Override
	public void remove(int id, boolean confirmation) throws NotFoundException, ProcessingException {
		remove(get(id), confirmation);
	}

	@Override
	public void remove(Patient object, boolean confirmation) throws NotFoundException, ProcessingException {
		consultCtrl.batchRemove(consultCtrl.getAll(object));
		super.remove(object, confirmation);
	}

	@Override
	protected Patient makeObjectFromString(String[] object)
			throws NumberFormatException {
		return new Patient(
				Integer.parseInt(object[0]),
				object[1],
				object[2],
				Integer.parseInt(object[3]),
				LocalDate.parse(object[4])
		);
	}

}
