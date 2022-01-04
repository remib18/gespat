package controllers;

import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;

import java.time.LocalDate;

public class PatientController extends AbstractController<Patient> {

    /**
     * Crée un objet permettant la manimulation des consultations
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public PatientController() throws ProcessingException {
        this.storeFile = "patients.txt";

        load();
    }

    /**
     * Crée un objet permettant la manimulation des consultations
     * @param storageFile - Utilisation d'un fichier de stockage différent.
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public PatientController(String storageFile)
            throws ProcessingException {
        this.storeFile = storageFile;

        load();
    }

    @Override
    protected Patient makeObjectFromString(String[] object)
            throws NumberFormatException {
        return new Patient(
                object[0],
                object[1],
                Integer.parseInt(object[2]),
                LocalDate.parse(object[3])
        );
    }

}
