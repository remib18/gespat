package controllers;

import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Consultation;
import models.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationController extends AbstractController<Consultation> {

    /** Instance du PatientController */
    private PatientController patientCtrl;

    /** Liste du matériel disponible */
    private final String[] devices = {"Béquille / Canne", "Béquilles (paire)", "Chaise roulant", "Prothèse"};

    /**
     * Crée un objet permettant la manimulation des consultations
     * @param patientCtrl
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public ConsultationController(PatientController patientCtrl)
            throws ProcessingException {
        this.patientCtrl = patientCtrl;
        this.storeFile = "consultations.txt";

        load();
    }

    /**
     * Crée un objet permettant la manimulation des consultations
     * @param patientCtrl
     * @param storageFile - Utilisation d'un fichier de stockage différent.
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public ConsultationController(
            PatientController patientCtrl,
            String storageFile
    ) throws ProcessingException {
        this.patientCtrl = patientCtrl;
        this.storeFile = storageFile;

        load();
    }

    /**
     * Crée une nouvelle consultation
     * @param patient
     * @param doctorName
     * @param consultedAt
     * @param diagnosedPathologies
     * @param requiredEquiment
     * @param granted
     * @throws ConflictingDataException
     * @throws ProcessingException
     */
    public Consultation add(
            Patient patient,
            String doctorName,
            LocalDate consultedAt,
            String[] diagnosedPathologies,
            String requiredEquiment,
            boolean granted
    ) throws ConflictingDataException, ProcessingException {
        Consultation consult = new Consultation(
                getLastInsertedIndex() + 1,
                patient,
                doctorName,
                consultedAt,
                diagnosedPathologies,
                requiredEquiment,
                granted
        );
        super.add(consult);
        return consult;
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
     * @return l'ensemble des appareils disponibles
     */
    public String[] getDevices() {
        return devices;
    }

    @Override
    protected Consultation makeObjectFromString(String[] object)
            throws NotFoundException, NumberFormatException, ProcessingException {
        return new Consultation(
                Integer.parseInt(object[0]),
                patientCtrl.get(Integer.parseInt(object[1])),
                object[2],
                LocalDate.parse(object[3]),
                object[4].split("|"),
                object[5],
                object[6].equals("true")
        );
    }
}
