package controllers;

import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Consultation;
import models.Device;
import models.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationController extends AbstractController<Consultation> {

    /** Instance du PatientController */
    private final PatientController patientCtrl;
    private final DeviceController deviceCtrl;

    /**
     * Crée un objet permettant la manimulation des consultations
     * @param patientCtrl
     * @throws ProcessingException en cas d'erreur lors du chargement des fichiers
     */
    public ConsultationController(PatientController patientCtrl, DeviceController deviceCtrl)
            throws ProcessingException {
        this.patientCtrl = patientCtrl;
        this.deviceCtrl = deviceCtrl;
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
            DeviceController deviceCtrl,
            String storageFile
    ) throws ProcessingException {
        this.patientCtrl = patientCtrl;
        this.deviceCtrl = deviceCtrl;
        this.storeFile = storageFile;

        load();
    }

    /**
     * Crée une nouvelle consultation
     * @param patient
     * @param doctorName
     * @param consultedAt
     * @param diagnosedPathologies
     * @param requiredEquipment
     * @param granted
     * @throws ConflictingDataException
     * @throws ProcessingException
     */
    public Consultation add(
            Patient patient,
            String doctorName,
            LocalDate consultedAt,
            String[] diagnosedPathologies,
            Device requiredEquipment,
            boolean granted
    ) throws ConflictingDataException, ProcessingException {
        Device device = requiredEquipment == null ? deviceCtrl.add(Device.STATES.UNDEFINED, null) : requiredEquipment;
        Consultation consult = new Consultation(
                getLastInsertedIndex() + 1,
                patient,
                doctorName,
                consultedAt,
                diagnosedPathologies,
                device,
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

    @Override
    protected Consultation makeObjectFromString(String[] object)
            throws NotFoundException, NumberFormatException, ProcessingException {
        String[] device = {object[5], object[6], object[7]};
        return new Consultation(
                Integer.parseInt(object[0]),
                patientCtrl.get(Integer.parseInt(object[1])),
                object[2],
                LocalDate.parse(object[3]),
                object[4].split("|"),
                deviceCtrl.makeObjectFromString(device),
                object[8].equals("true")
        );
    }
}
