package views;

import components.Button;
import components.Checkbox;
import components.Label;
import components.inputs.TextArea;
import components.table.TableRowsFunctionsInterface;
import components.template.SidebarRow;
import components.template.Template;
import components.template.Template.In;
import controllers.ConsultationController;
import exceptions.ConflictingDataException;
import exceptions.FormatException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Consultation;
import models.Patient;
import utils.Colors;
import utils.ConsultationTableModel;
import views.popups.ConfirmSuppression;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.util.List;

public class Doctor extends JFrame {

    private static final long serialVersionUID = -3405741868946411966L;
    private final Template<Consultation> template = new Template<>(978, 550);
    private TableRowsFunctionsInterface<Consultation> tableUtils;
    private ConsultationController consultCtrl;
    private Consultation selectedConsultation;
    private int activeRow;

    public Doctor(ConsultationController consultationController) {
        setTitle("GesPat — Personnel Médical");
        setSize(1080, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(978, 550));

        consultCtrl = consultationController;

        template.setSearchBar(
                consultationController.getAll(),
                new ConsultationTableModel(),
                (consultation, tUtils) -> {
                    this.tableUtils = tUtils;
                    setSelected(consultation);
                }
        );

        Button createBtn = new Button("Nouvelle consultation").setPosition(Button.Position.END);
        createBtn.addActionListener(e -> create());
        template.add(createBtn, In.MAIN_HEADER, "gapleft 0");

        Label tableTitle = new Label("Liste des consultations liées au patient", Label.Styles.TITLE);
        template.add(tableTitle, In.MAIN_BODY);

        Button exportBtn = new Button("Exporter", Button.Size.SMALL, Button.Style.OUTLINED, Button.Color.SECONDARY);
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

        String[] devices = consultCtrl.getDevices();

        for (int i = 0; i < devices.length; i++) {
            Checkbox device = new Checkbox(devices[i], devices[i].equals(selectedConsultation.getRequiredEquiment()));
            int finalI = i;
            device.addActionListener(e -> {
                Checkbox cp = (Checkbox) e.getSource();
                if (cp.isSelected()) {
                    selectedConsultation.setRequiredEquiment(devices[finalI]);
                    updateGraphics();
                }
            });
            panel.add(device);
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

        for (int i = 0; i < pathologies.size(); i++) {
            String pathologyString = pathologies.get(i);
            Checkbox pathology = new Checkbox(pathologies.get(i), patientPathologies.contains(pathologyString));
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
        } catch (NullPointerException e) {
            // TODO: handle exception
        } finally {
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

        template.add(new SidebarRow("Patient : ", patient.getFullname(), false), In.SIDEBAR_BODY);

        template.add(new SidebarRow("Sécurité sociale : ", "" + patient.getSocialId(), false), In.SIDEBAR_BODY);

        SidebarRow consultedAt = new SidebarRow("Date de la consultation", selectedConsultation.getConsultedAt());
        template.add(consultedAt, In.SIDEBAR_BODY);

        SidebarRow doctorName = new SidebarRow("Docteur : ", patient.getFullname());
        template.add(doctorName, In.SIDEBAR_BODY);

        JPanel pathApp = new JPanel();
        pathApp.setLayout(new BorderLayout());
        pathApp.setOpaque(false);

        pathApp.add(getPathologiesCheckboxes(), BorderLayout.WEST);
        pathApp.add(getDevicesCheckboxes(), BorderLayout.EAST);

        template.add(pathApp, In.SIDEBAR_BODY);

        TextArea details = new TextArea(325, 200);
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

    private void create() {
        try {
            // TODO: @evanchr
            Consultation consultation = null;
            consultCtrl.add(consultation);
            setSelected(consultation);
        } catch (ConflictingDataException | ProcessingException err) {
            // TODO: Implement ErrorMessageView
            System.err.println("[DOCTOR DASH VIEW]: " + err.getMessage());
        }
    }

    private void save(SidebarRow doctorName,
                      SidebarRow consultedAt
		) {
            try {
                selectedConsultation.setConsultedAt(consultedAt.getDate());
            } catch (FormatException err) {
                // TODO: Implement ErrorMessageView
                System.err.println(err.getMessage());
            }
            selectedConsultation.setDoctorName(doctorName.getText());
            try {
                consultCtrl.update(selectedConsultation);
                setSelected(selectedConsultation); // To update the view
            } catch (NotFoundException | ProcessingException err) {
                // TODO: Implement ErrorMessageView
                System.err.println(err.getMessage());
            }
    }

    private void delete() {
        try {
            consultCtrl.remove(selectedConsultation, ConfirmSuppression.getPopup());
            setSelected(Math.max(activeRow - 1, 0));
        } catch (NotFoundException err) {
            /* The patient obviously exists */ } catch (ProcessingException err) {
            // TODO: Implement ErrorMessageView
            System.err.print(err.getMessage());
        }
    }

    private void setPathology(String pathology, boolean checkboxState) {
        final List<String> pathologies = selectedConsultation.getDiagnosedPathologies();
        final boolean contains = pathologies.contains(pathology);
        if (checkboxState && !contains) {
            pathologies.add(pathology);
        } else if (!checkboxState && contains) {
            pathologies.remove(pathology);
        }
        selectedConsultation.setDiagnosedPathologies(pathologies.toArray(new String[pathologies.size()]));
    }
}
