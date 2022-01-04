
import controllers.ConsultationController;
import controllers.DeviceController;
import controllers.PatientController;
import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Device;
import models.Patient;
import views.Login;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static PatientController patientController;
    private static ConsultationController consultationController;
    private static DeviceController deviceController;

    /**
     * Génère des données pour tester l'application
     * @throws ProcessingException
     */
    public static void genData() throws ProcessingException {
        logger.log(Level.INFO, "Génération de données test");

        patientController.clear();
        consultationController.clear();

        try {
            patientController.add(new Patient("Jean", "Jack", 123, LocalDate.of(2000, 1, 21)));
            patientController.add(new Patient("François", "Jack", 12, LocalDate.of(2001, 1, 21)));
            patientController.add(new Patient("Jeanne", "France", 1, LocalDate.of(2003, 1, 21)));
            patientController.add(new Patient("Franck", "Morice", 1234, LocalDate.of(2004, 1, 21)));
        } catch (ConflictingDataException err) {
            logger.log(Level.SEVERE, "Erreur lors de la génération des patients");
        }

        String[] pathologies1 = {"death"};
        String[] pathologies2 = {"cancer", "weird"};
        String[] pathologies3 = {"weird"};
        Device device1 = null;
        Device device2 = null;
        Device device3 = null;
        try {
            device1 = deviceController.add(Device.STATES.UNDEFINED, null);
            device2 = deviceController.add(Device.STATES.PENDING, DeviceController.DEVICES[2]);
            device3 = deviceController.add(Device.STATES.ASSIGNED, DeviceController.DEVICES[3]);
        } catch (ConflictingDataException err) {
            logger.log(Level.SEVERE, "Erreur lors de la génération des patients");
        }

        try {
            consultationController.add(
                    patientController.get(123),
                    "Gérard",
                    LocalDate.now(),
                    pathologies1,
                    device1,
                    false
            );

            consultationController.add(
                    patientController.get(123),
                    "Darmanin",
                    LocalDate.of(2020, 1, 5),
                    pathologies2,
                    device2,
                    false
            );

            consultationController.add(
                    patientController.get(12),
                    "Rodolphe",
                    LocalDate.of(2000, 5, 22),
                    pathologies3,
                    device3,
                    false
            );
        } catch (NotFoundException | ConflictingDataException e) {
            logger.log(Level.SEVERE, "Erreur lors de la génération des patients");
        }
    }

    /** Point de démarage de l'application */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Lancement de l'application");
        try {
            // On charge les données
            patientController = new PatientController();
            deviceController = new DeviceController();
            consultationController = new ConsultationController(patientController, deviceController);

            // Simulation de données pour le développement
            genData();

            logger.log(Level.INFO, "Application démarée");
            // On lance la fenêtre de connexion
            new Login(patientController, consultationController, deviceController);
        } catch (ProcessingException e) {
            // En cas de problème lors de la gestion des fichiers
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}
