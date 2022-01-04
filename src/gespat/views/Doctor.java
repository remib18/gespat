package views;

import components.Button;
import components.Separator;
import components.inputs.Checkbox;
import components.Label;
import components.inputs.TextArea;
import components.table.TableRowsFunctionsInterface;
import components.template.SidebarRow;
import components.template.Template;
import components.template.Template.In;
import components.template.XSpaceBetween;
import controllers.ConsultationController;
import controllers.DeviceController;
import controllers.PatientController;
import exceptions.ConflictingDataException;
import exceptions.FormatException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Consultation;
import models.Device;
import models.Patient;
import utils.Colors;
import utils.ConsultationTableModel;
import views.popups.ConfirmSuppression;
import net.miginfocom.swing.MigLayout;
import views.popups.ErrorMessage;
import views.popups.SelectPatient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Doctor extends JFrame {

    private static final long serialVersionUID = -3405741868946411966L;
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final Template<Consultation> template = new Template<>(978, 550);
    private TableRowsFunctionsInterface<Consultation> tableUtils;
    private final PatientController patientCtrl;
    private final ConsultationController consultCtrl;
    private Consultation selectedConsultation;
    private int activeRow;

    private final Button createBtn;

    public Doctor(
            PatientController patientController,
            ConsultationController consultationController,
            DeviceController deviceController
    ) {
        setTitle("GesPat — Personnel Médical");
        setSize(1080, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(978, 550));

        ErrorMessage.setActiveWindow(this);

        patientCtrl = patientController;
        consultCtrl = consultationController;

        template.setSearchBar(
                consultationController.getAll(),
                new ConsultationTableModel(),
                (consultation, tUtils) -> {
                    this.tableUtils = tUtils;
                    setSelected(consultation);
                }
        );

        createBtn = new Button("Nouvelle consultation").setPosition(Button.Position.END);
        createBtn.addActionListener(e -> create(createBtn));
        template.add(createBtn, In.MAIN_HEADER, "gapleft 0");

        Label tableTitle = new Label("Liste des consultations liées au patient", Label.Styles.TITLE);
        template.add(tableTitle, In.MAIN_BODY);

        Button exportBtn = new Button("Exporter", Button.Size.SMALL, Button.Style.OUTLINED, Button.Color.SECONDARY);
        exportBtn.addActionListener(e -> export(exportBtn));
        template.add(exportBtn.setPosition(Button.Position.END), In.SIDEBAR_HEADER);

        sidebarSetup();

        add(template);
        setVisible(true);
    }

    private Component getDevicesCheckboxes() {
        final JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1, insets 0"));
        panel.setBackground(Colors.SECONDARY);

        panel.add(new Label("Appareillage :"));

        String[] devices = DeviceController.DEVICES;

        for (String device : devices) {
            Checkbox deviceChkbx = new Checkbox(device, device.equals(selectedConsultation.getRequiredEquipment().getLabel()));
            deviceChkbx.addActionListener(e -> {
                Checkbox cp = (Checkbox) e.getSource();
                if (cp.isSelected()) {
                    Device equipment = selectedConsultation.getRequiredEquipment();
                    equipment.setLabel(device);
                    equipment.setState(Device.STATES.PENDING);
                    selectedConsultation.setRequiredEquipment(equipment);
                    updateGraphics();
                }
            });
            panel.add(deviceChkbx);
        }

        return panel;
    }

    private Component getPathologiesCheckboxes() {
        final JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1, insets 0"));
        panel.setBackground(Colors.SECONDARY);

        Label nomPath = new Label("Pathologies :");
        panel.add(nomPath);

        final List<String> patientPathologies = selectedConsultation.getDiagnosedPathologies();
        final List<String> pathologies = consultCtrl.getAllPathologies();

        for (String pathologyString : pathologies) {
            Checkbox pathology = new Checkbox(pathologyString, patientPathologies.contains(pathologyString));
            pathology.addActionListener(e -> setPathology(pathologyString, ((Checkbox) e.getSource()).isSelected()));
            panel.add(pathology);
        }

        return panel;
    }

    private void setSelected(Consultation consultation) {
        try {
            int row = tableUtils.getRowIndex(consultation);
            selectedConsultation = consultation;
            activeRow = row;
            template.setResultTableSelectedRow(consultation);
        } catch (NullPointerException ignored) {} finally {
            updateGraphics();
        }
    }

    private void setSelected(int row) {
        try {
            Consultation consultation = tableUtils.getData(row);
            selectedConsultation = consultation;
            activeRow = row;
            template.setResultTableSelectedRow(consultation);
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
        if (selectedConsultation == null) {
            // Si aucun patient n'est selectionner, on affiche un message.
            template.add(new Label("Selectionnez un patient."), In.SIDEBAR_BODY);
            return;
        }
        final Patient patient = selectedConsultation.getPatient();

        SidebarRow socialId = new SidebarRow("Patient : ", patient.getFullname());
        template.add(socialId, In.SIDEBAR_BODY);

        template.add(new SidebarRow("Sécurité sociale : ", "" + patient.getSocialId()), In.SIDEBAR_BODY);

        Separator separator = new Separator();
        template.add(separator, In.SIDEBAR_BODY);

        SidebarRow consultedAt = new SidebarRow("Date de la consultation :", selectedConsultation.getConsultedAt(), true);
        template.add(consultedAt, In.SIDEBAR_BODY);

        SidebarRow doctorName = new SidebarRow("Docteur : ", selectedConsultation.getDoctorName(), true);
        template.add(doctorName, In.SIDEBAR_BODY);

        XSpaceBetween cols = new XSpaceBetween(getPathologiesCheckboxes(), getDevicesCheckboxes());
        cols.setBorder(BorderFactory.createEmptyBorder(4, 0,0,0));
        template.add(cols, In.SIDEBAR_BODY);

        TextArea details = new TextArea(325, 100);
        template.add(details, In.SIDEBAR_BODY);

        Button saveBtn = new Button("Enregistrer les modifications", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.SECONDARY);
        saveBtn.addActionListener(e -> save(doctorName, consultedAt));

        Button deleteBtn = new Button("Supprimer la consultation", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.DANGER);
        deleteBtn.addActionListener(e -> delete());

        // template.add(saveBtn, In.SIDEBAR_FOOTER);
        // template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_FOOTER);
        // template.add(deleteBtn, In.SIDEBAR_FOOTER);

        // Temporary fix for testing
        template.add(saveBtn, In.SIDEBAR_BODY);
        template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_BODY);
        template.add(deleteBtn, In.SIDEBAR_BODY);
    }

    private void create(Button btn) {
        (new SelectPatient(
                patientCtrl.getAll(),
                template,
                btn.getTrueX(),
                btn.getY() + btn.getHeight()
        )).subscribe(patient -> {
            try {
                Consultation consultation = consultCtrl.add(patient, "", LocalDate.now(), new String[0], null, false);
                setSelected(consultation);
            } catch (ProcessingException err) {
                logger.log(Level.SEVERE, err.getMessage());
                new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.System);
            } catch (ConflictingDataException err) {
                logger.log(Level.WARNING, err.getMessage());
                new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.User);
            } finally {
                updateGraphics();
            }
        });
    }

    private void save(SidebarRow doctorName,
                      SidebarRow consultedAt
		) {
            try {
                selectedConsultation.setConsultedAt(consultedAt.getDate());
            } catch (FormatException err) {
                logger.log(Level.SEVERE, err.getMessage());
                new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.System);
            }
            selectedConsultation.setDoctorName(doctorName.getText());
            try {
                consultCtrl.update(selectedConsultation);
                setSelected(selectedConsultation); // To update the view
            } catch (ProcessingException err) {
                logger.log(Level.SEVERE, err.getMessage());
                new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.System);
            } catch (NotFoundException err) {
                logger.log(Level.WARNING, err.getMessage());
                new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.User);
            }
            updateGraphics();
    }

    private void delete() {
        try {
            consultCtrl.remove(selectedConsultation, ConfirmSuppression.getPopup());
            setSelected(Math.max(activeRow - 1, 0));
        } catch (NotFoundException err) {
            /* The patient obviously exists */ } catch (ProcessingException err) {
            logger.log(Level.SEVERE, err.getMessage());
            new ErrorMessage(err.getMessage(), ErrorMessage.LEVEL.System);
        } finally {
            updateGraphics();
        }
    }

    private void export(Button btn) { 
        try {
            FileWriter file = new FileWriter("PatientExport.txt");
            //file.write(selectedConsultation.getPatient());
            file.write(Integer.toString(selectedConsultation.getId()));
            //file.write(Date.toString(selectedConsultation.getConsultedAt()));
            file.write(selectedConsultation.getDoctorName());
            //file.write(selectedConsultation.getDiagnosedPathologies());
            //file.write(selectedConsultation.getRequiredEquipment());
            file.close();
        } catch (IOException  err){
            err.printStackTrace();
        }
    } //nom patient, num secu, date consult, nom docteur, path et app, details


    private void setPathology(String pathology, boolean checkboxState) {
        final List<String> pathologies = selectedConsultation.getDiagnosedPathologies();
        final boolean contains = pathologies.contains(pathology);
        if (checkboxState && !contains) {
            pathologies.add(pathology);
        } else if (!checkboxState && contains) {
            pathologies.remove(pathology);
        }
        selectedConsultation.setDiagnosedPathologies(pathologies.toArray(new String[0]));
    }
}
