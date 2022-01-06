package views;

import components.Button;
import components.Label;
import components.Separator;
import components.inputs.Checkbox;
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
import net.miginfocom.swing.MigLayout;
import utils.Colors;
import utils.table.listeners.ConsultationTableModel;
import utils.Date;
import utils.File;
import views.popups.ConfirmSuppression;
import views.popups.SelectPatient;
import views.popups.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Doctor extends JFrame {

	private static final long serialVersionUID = -3405741868946411966L;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final Template<Consultation> template = new Template<>(978, 550);
	private final PatientController patientCtrl;
	private final ConsultationController consultCtrl;
	private final Button exportBtn;
	private final Button createBtn;
	private TableRowsFunctionsInterface<Consultation> tableUtils;
	private Consultation selectedConsultation;
	private int activeRow;

	public Doctor(
			PatientController patientController,
			ConsultationController consultationController,
			DeviceController deviceController
	) {
		setTitle("GesPat — Personnel Médical");
		setSize(1080, 575);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(978, 575));

		UserMessage.setActiveWindow(this);

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

		exportBtn = new Button("Exporter", Button.Size.SMALL, Button.Style.OUTLINED, Button.Color.SECONDARY);
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

		Button newPathology = new Button("Autre", Button.Size.SMALL, Button.Style.OUTLINED, Button.Color.SECONDARY);
		newPathology.addActionListener(this::addPathology);
		panel.add(newPathology);

		return panel;
	}

	private void addPathology(ActionEvent e) {
		String pathology = JOptionPane.showInputDialog(this, "Saisissez le nom de la pathologie :");
		setPathology(pathology, true);
		save(false);
	}

	private void setSelected(Consultation consultation) {
		try {
			int row = tableUtils.getRowIndex(consultation);
			selectedConsultation = consultation;
			activeRow = row;
			template.setResultTableSelectedRow(consultation);
		} catch (NullPointerException ignored) {
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
		template.updateGUI();
		repaint();
		revalidate();
	}

	private void sidebarSetup() {
		if (selectedConsultation == null) {
			// Si aucun patient n'est selectionner, on affiche un message et on masque le bouton d'export.
			template.add(new Label("Selectionnez un patient."), In.SIDEBAR_BODY);
			exportBtn.setVisible(false);

			return;
		}
		// On re-définit le bouton d'export visible.
		exportBtn.setVisible(true);

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
		cols.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		template.add(cols, In.SIDEBAR_BODY);

		TextArea details = new TextArea(10000, 100);
		details.setText(selectedConsultation.getObservations());
		template.add(details, In.SIDEBAR_BODY);

		Button saveBtn = new Button("Enregistrer les modifications", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.SECONDARY);
		saveBtn.addActionListener(e -> save(doctorName, consultedAt, details));

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
				Consultation consultation = consultCtrl.add(patient, "Nom", LocalDate.now(), new String[0], null, "Observations");
				setSelected(consultation);
				new UserMessage("Consultation créer avec succès. N'oubliez pas d'attribuer un docteur.", UserMessage.LEVEL.Info);
			} catch (ProcessingException err) {
				logger.log(Level.SEVERE, err.getMessage());
				new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
			} catch (ConflictingDataException err) {
				logger.log(Level.WARNING, err.getMessage());
				new UserMessage(err.getMessage(), UserMessage.LEVEL.Warning);
			} finally {
				updateGraphics();
			}
		});
	}

	private void save(SidebarRow doctorName,
	                  SidebarRow consultedAt,
	                  TextArea observations
	) {
		try {
			selectedConsultation.setConsultedAt(consultedAt.getDate());
		} catch (FormatException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		}
		selectedConsultation.setDoctorName(doctorName.getText());
		selectedConsultation.setObservations(observations.getText());
		save(true);
	}

	private void save(boolean showSuccessMessage) {
		try {
			consultCtrl.update(selectedConsultation);
			setSelected(selectedConsultation); // To update the view
			if (showSuccessMessage)
				new UserMessage("Consultation enregistrée avec succès.", UserMessage.LEVEL.Info);
		} catch (ProcessingException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		} catch (NotFoundException err) {
			logger.log(Level.WARNING, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Warning);
		}
		updateGraphics();
	}

	private void delete() {
		if (!ConfirmSuppression.getPopup(this)) return;
		try {
			consultCtrl.remove(selectedConsultation, true);
			setSelected(Math.max(activeRow - 1, 0));
			new UserMessage("Consultation supprimée avec succès.", UserMessage.LEVEL.Info);
		} catch (NotFoundException err) {
			/* The patient obviously exists */
		} catch (ProcessingException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		} finally {
			updateGraphics();
		}
	}

	private void export(Button btn) {
		List<String> msg = new ArrayList<>();
		msg.add("Détails de la consultation");
		msg.add("");
		msg.add("");
		msg.add("Consultation numéro : " + selectedConsultation.getId());
		msg.add("Date de la consultation : " + Date.getDisplayString(selectedConsultation.getConsultedAt()));
		msg.add("Ausculté par le•a docteur•esse : " + selectedConsultation.getDoctorName());
		msg.add("");
		msg.add("Associé au patient :");
		msg.add("Nom et prénom : " + selectedConsultation.getPatient().getFullname());
		msg.add("Numéro de sécurité sociale : " + selectedConsultation.getPatient().getSocialId());
		msg.add("");
		msg.add("Liste des pathologies diagnostiquées :");
		for (String pathology : selectedConsultation.getDiagnosedPathologies()) {
			msg.add("  — " + pathology + "\n");
		}
		msg.add("");
		msg.add("Observations du docteur :");
		msg.add(selectedConsultation.getObservations());
		msg.add("");
		msg.add("Appareil nécessaire :");
		Device equipment = selectedConsultation.getRequiredEquipment();
		msg.add("  — " + equipment.getLabel() + " (statut : " + equipment.getState().name().toLowerCase() + ")");

		String file = "export-" + selectedConsultation.getId() + ".txt";

		try {
			(new File<String>()).saveData(msg, file);
			new UserMessage("Fiche de consultation exportée sous " + file + ".", UserMessage.LEVEL.Info);
		} catch (ProcessingException e) {
			new UserMessage("Erreur lors de l'enregistrement du fichier " + file + ".", UserMessage.LEVEL.Severe);
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
		selectedConsultation.setDiagnosedPathologies(pathologies.toArray(new String[0]));
	}
}
