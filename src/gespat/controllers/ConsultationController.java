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

    /** Instance du PatientController */
    private final PatientController patientCtrl;
    private final DeviceController deviceCtrl;

    /**
     * Crée un objet permettant la manipulation des consultations
     * @param patientCtrl
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
     * @param patient
     * @param doctorName
     * @param consultedAt
     * @param diagnosedPathologies
     * @param requiredEquipment
     * @param observations
     * @throws ConflictingDataException
     * @throws ProcessingException
     */
    public Consultation add(
            Patient patient,
            String doctorName,
            LocalDate consultedAt,
            String[] diagnosedPathologies,
            Device requiredEquipment,
            String observations
    ) throws ConflictingDataException, ProcessingException {
        Device device = requiredEquipment == null ? deviceCtrl.add(Device.STATES.UNDEFINED, null) : requiredEquipment;
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

    public List<Consultation> getAll(Patient patient) {
        List<Consultation> consultations = new ArrayList<>();
        for (Consultation c : data) {
            if (c.getPatient().equals(patient))
                consultations.add(c);
        }
        return consultations;
    }

    void batchRemove(List<Consultation> consultations) throws NotFoundException, ProcessingException {
        for (Consultation consultation: consultations) {
            remove(consultation, true);
        }
    }

    @Override
    protected Consultation makeObjectFromString(String[] object)
            throws NotFoundException, NumberFormatException, ProcessingException {
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
