package controllers;

import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;
import utils.StateManager;

import java.time.LocalDate;

public class PatientController extends AbstractController<Patient> {

    /**
     * Crée un objet permettant la manimulation des consultations
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public PatientController() throws ProcessingException {
        storeFile = "patients.txt";
        stateDataType = StateManager.DataType.Patient;

        load();
    }

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
