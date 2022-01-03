
import controllers.ConsultationController;
import controllers.PatientController;
import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;
import views.Login;

import java.time.LocalDate;

public class Main {

    private static PatientController patientController;
    private static ConsultationController consultationController;

    /**
     * Génère des données pour tester l'application
     * @throws ProcessingException
     */
    public static void genData() throws ProcessingException {
        patientController.clear();
        consultationController.clear();

        try {
            patientController.add(new Patient("Jean", "Jack", 123, LocalDate.of(2000, 1, 21)));
            patientController.add(new Patient("François", "Jack", 12, LocalDate.of(2001, 1, 21)));
            patientController.add(new Patient("Jeanne", "France", 1, LocalDate.of(2003, 1, 21)));
            patientController.add(new Patient("Franck", "Morice", 1234, LocalDate.of(2004, 1, 21)));
        } catch (ConflictingDataException e1) { /**/ }

        String[] pathologies1 = {"death"};
        String[] pathologies2 = {"cancer", "weird"};
        String[] pathologies3 = {"weird"};
        String[] devices = consultationController.getDevices();

        try {
            consultationController.add(
                    patientController.get(123),
                    "Gérard",
                    LocalDate.now(),
                    pathologies1,
                    null,
                    false
            );

            consultationController.add(
                    patientController.get(123),
                    "Darmanin",
                    LocalDate.of(2020, 1, 5),
                    pathologies2,
                    devices[2],
                    false
            );

            consultationController.add(
                    patientController.get(12),
                    "Rodolph",
                    LocalDate.of(2000, 5, 22),
                    pathologies3,
                    devices[3],
                    false
            );
        } catch (NotFoundException | ConflictingDataException e) { /**/ }

    }

    /** Point de démarage de l'application */
    public static void main(String[] args) {
        try {
            // On charge les données
            patientController = new PatientController();
            consultationController = new ConsultationController(patientController);

            // Simulation de données pour le développement
            genData();

            // On lance la fenêtre de connexion
            new Login(patientController, consultationController);
        } catch (ProcessingException e) {
            // En cas de problème lors de la gestion des fichiers
            System.err.println(e.getMessage());
        }
    }

}
