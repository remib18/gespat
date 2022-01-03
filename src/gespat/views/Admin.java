package views;

import components.Button;
import components.Label;
import components.table.TableRowsFunctionsInterface;
import components.template.SidebarRow;
import components.template.Template;
import components.template.Template.In;
import controllers.PatientController;
import exceptions.ConflictingDataException;
import exceptions.FormatException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Patient;
import utils.PatientTableModel;
import views.popups.ConfirmSuppression;

import javax.swing.JFrame;
import javax.swing.Box;
import java.awt.Dimension;

public class Admin extends JFrame {

    private static final long serialVersionUID = -7747571669269392475L;

    private final Template<Patient> template = new Template<>(978, 550);
    private TableRowsFunctionsInterface<Patient> tableUtils;
    private PatientController patientCtrl;
    private Patient selectedPatient;
    private int activeRow;

    public Admin(PatientController patientController) {
        setTitle("GesPat — Personnel d'administration");
        setSize(1080, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(978, 550));

        this.patientCtrl = patientController;

        // Génération d'un template avec une barre de recherche lié sur les données des patients.
        template.setSearchBar(
                patientController.getAll(),
                new PatientTableModel(patientController),
                (patient, tUtils) -> {
                    this.tableUtils = tUtils;
                    setSelected(patient);
                }
        );

        Button create = new Button("Nouveau patient").setPosition(Button.Position.END);
        create.addActionListener(e -> create());
        template.add(create, In.MAIN_HEADER, "gapleft 0");

        Label tableTitle = new Label("Résultat de la recherche");
        template.add(tableTitle, In.MAIN_BODY);

        sidebarSetup();

        add(template);
        setVisible(true);
    }

    private void setSelected(Patient patient) {
        try {
            int row = tableUtils.getRowIndex(patient);
            selectedPatient = patient;
            activeRow = row;
            template.setResultTableSelectedRow(patient);
        } catch (NullPointerException e) {
            //TODO: handle exception
        } finally {
            updateGraphics();
        }
    }

    private void setSelected(int row) {
        try {
            Patient patient = tableUtils.getData(row);
            selectedPatient = patient;
            activeRow = row;
            template.setResultTableSelectedRow(patient);
        } catch (IndexOutOfBoundsException e) {
            template.setResultTableSelectedRow(row - 1);
        } finally {
            updateGraphics();
        }
    }

    private void updateGraphics() {
        template.clear(In.SIDEBAR_BODY);
        template.clear(In.SIDEBAR_FOOTER);
        sidebarSetup();
        template.updateGUI();
        repaint();
        revalidate();
    }

    private void sidebarSetup() {
        if (selectedPatient == null) {
            // Si aucun patient n'est selectionner, on affiche un message.
            template.add(new Label("Selectionnez un patient."), In.SIDEBAR_BODY);
            return;
        }

        SidebarRow lastname = new SidebarRow("Nom :", selectedPatient.getLastname(), true);
        template.add(lastname, In.SIDEBAR_BODY);

        SidebarRow firstname = new SidebarRow("Prénom", selectedPatient.getFirstname(), true);
        template.add(firstname, In.SIDEBAR_BODY);

        SidebarRow birthAt = new SidebarRow("Date de naissance", selectedPatient.getBirthAt(), true);
        template.add(birthAt, In.SIDEBAR_BODY);

        SidebarRow socialId = new SidebarRow("Numéro de sécurité sociale", "" + selectedPatient.getSocialId(), true);
        template.add(socialId, In.SIDEBAR_BODY);

        Button saveBtn = new Button("Enregistrer les modifications", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.SECONDARY);
        saveBtn.addActionListener(e -> save(lastname, firstname, birthAt, socialId));

        Button deleteBtn = new Button("Supprimer le patient", Button.Size.LARGE, Button.Style.FILLED, Button.Color.DANGER);
        deleteBtn.addActionListener(e -> delete());

        // template.add(saveBtn, In.SIDEBAR_FOOTER);
        // template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_FOOTER);
        // template.add(deleteBtn, In.SIDEBAR_FOOTER);

        // Temporary fix for testing
        template.add(saveBtn, In.SIDEBAR_BODY);
        template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_BODY);
        template.add(deleteBtn, In.SIDEBAR_BODY);
    }

    private void create() {
        try {
            Patient patient = new Patient("xxx", "xxx", 0, null);
            patientCtrl.add(patient);
            setSelected(patient);
        } catch (ConflictingDataException | ProcessingException err) {
            // TODO: Implement ErrorMessageView
            System.err.println("[ADMIN DASH VIEW]: " + err.getMessage());
        } catch (IllegalArgumentException err) {
            activeRow--;
        } finally {
            updateGraphics();
        }
    }

    private void save(
            SidebarRow lastname,
            SidebarRow firstname,
            SidebarRow birthAt,
            SidebarRow socialId
    ) {
        selectedPatient.setLastname(lastname.getText());
        selectedPatient.setFirstname(firstname.getText());
        try {
            selectedPatient.setBirthAt(birthAt.getDate());
        } catch (FormatException err) {
            // TODO: Implement ErrorMessageView
            System.err.println(err.getMessage());
        }
        selectedPatient.setSocialId(Integer.parseInt(socialId.getText()));
        try {
            patientCtrl.update(selectedPatient);
            updateGraphics();
        } catch (NotFoundException | ProcessingException err) {
            // TODO: Implement ErrorMessageView
            System.err.println(err.getMessage());
        }
    }

    private void delete() {
        try {
            patientCtrl.remove(selectedPatient, ConfirmSuppression.getPopup());
            setSelected(Math.max(activeRow - 1, 0));
        } catch (NotFoundException err) {
            /* The patient obviously exists */ } catch (ProcessingException err) {
            // TODO: Implement ErrorMessageView
            System.err.print(err.getMessage());
        }
    }

}
