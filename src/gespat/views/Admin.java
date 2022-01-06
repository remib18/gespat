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
import utils.table.listeners.PatientTableModel;
import views.popups.ConfirmSuppression;
import views.popups.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin extends JFrame {

	private static final long serialVersionUID = -7747571669269392475L;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private final Template<Patient> template = new Template<>(978, 550);
	private final PatientController patientCtrl;
	private TableRowsFunctionsInterface<Patient> tableUtils;
	private Patient selectedPatient;
	private int activeRow;

	/**
	 * Création d'une page pour les administrateurs
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public Admin(PatientController patientController) {
		setTitle("GesPat — Personnel d'administration");
		setSize(1080, 575);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(978, 575));

		UserMessage.setActiveWindow(this);

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
		create.addActionListener(this::create);
		template.add(create, In.MAIN_HEADER, "gapleft 0");

		Label tableTitle = new Label("Résultat de la recherche");
		template.add(tableTitle, In.MAIN_BODY);

		sidebarSetup();

		add(template);
		setVisible(true);
	}

	/**
	 * Définit le patient sélectionné
	 *
	 * @param patient patient à sélectionner
	 */
	private void setSelected(Patient patient) {
		try {
			int row = tableUtils.getRowIndex(patient);
			selectedPatient = patient;
			activeRow = row;
			template.setResultTableSelectedRow(patient);
		} catch (NullPointerException ignore) {
		} finally {
			updateGraphics();
		}
	}

	/**
	 * Définit le patient à sélectionner
	 *
	 * @param row ligne du tableau des résultats
	 */
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

	/**
	 * Met à jour l'interface après une modification
	 */
	private void updateGraphics() {
		template.clear(In.SIDEBAR_BODY);
		template.clear(In.SIDEBAR_FOOTER);
		sidebarSetup();
		template.updateGUI();
		repaint();
		revalidate();
	}

	/**
	 * Mise en place de l'interface latérale
	 */
	private void sidebarSetup() {
		if (selectedPatient == null) {
			// Si aucun patient n'est selectionner, on affiche un message.
			template.add(new Label("Sélectionnez un patient."), In.SIDEBAR_BODY);
			return;
		}

		SidebarRow lastname = new SidebarRow("Nom :", selectedPatient.getLastname(), true);
		template.add(lastname, In.SIDEBAR_BODY);

		SidebarRow firstname = new SidebarRow("Prénom :", selectedPatient.getFirstname(), true);
		template.add(firstname, In.SIDEBAR_BODY);

		SidebarRow birthAt = new SidebarRow("Date de naissance :", selectedPatient.getBirthAt(), true);
		template.add(birthAt, In.SIDEBAR_BODY);

		SidebarRow socialId = new SidebarRow("Numéro de sécurité sociale :", "" + selectedPatient.getSocialId(), true);
		template.add(socialId, In.SIDEBAR_BODY);

		Button saveBtn = new Button("Enregistrer les modifications", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.SECONDARY);
		saveBtn.addActionListener(e -> save(lastname, firstname, birthAt, socialId));

		Button deleteBtn = new Button("Supprimer le patient", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.DANGER);
		deleteBtn.addActionListener(this::delete);

		// template.add(saveBtn, In.SIDEBAR_FOOTER);
		// template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_FOOTER);
		// template.add(deleteBtn, In.SIDEBAR_FOOTER);

		// Temporary fix for testing
		template.add(saveBtn, In.SIDEBAR_BODY);
		template.add(Box.createRigidArea(new Dimension(0, 5)), In.SIDEBAR_BODY);
		template.add(deleteBtn, In.SIDEBAR_BODY);
	}

	/**
	 * Permet de créer un nouveau formulaire patient
	 */
	private void create(ActionEvent e) {
		try {
			Patient patient = patientCtrl.add("xxx", "xxx", 0, null);
			setSelected(patient);
			new UserMessage("Patient créer avec succès. N'oubliez pas de le modifier avec des données valides", UserMessage.LEVEL.Info);
		} catch (ProcessingException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		} catch (ConflictingDataException err) {
			logger.log(Level.WARNING, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Warning);
		} catch (IllegalArgumentException err) {
			activeRow--;
		} finally {
			updateGraphics();
		}
	}

	/**
	 * Permet d'enregistrer les modifications apporter au formulaire patient
	 *
	 * @param lastname  nom de famille du patient
	 * @param firstname prénom du patient
	 * @param birthAt   date de naissance du patient
	 * @param socialId  numéro de sécurité sociale du patient
	 */
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
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		}
		selectedPatient.setSocialId(Integer.parseInt(socialId.getText()));
		try {
			patientCtrl.update(selectedPatient);
			updateGraphics();
			new UserMessage("Patient sauvegardé avec succès.", UserMessage.LEVEL.Info);
		} catch (ProcessingException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		} catch (NotFoundException err) {
			logger.log(Level.WARNING, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Warning);
		}
	}

	/**
	 * Permet de supprimer un dossier patient
	 */
	private void delete(ActionEvent e) {
		if (!ConfirmSuppression.getPopup(this)) return;
		try {
			patientCtrl.remove(selectedPatient, true);
			setSelected(Math.max(activeRow - 1, 0));
			new UserMessage("Patient supprimé.", UserMessage.LEVEL.Info);
		} catch (NotFoundException ignore) {
		} catch (ProcessingException err) {
			logger.log(Level.SEVERE, err.getMessage());
			new UserMessage(err.getMessage(), UserMessage.LEVEL.Severe);
		}
	}

}
