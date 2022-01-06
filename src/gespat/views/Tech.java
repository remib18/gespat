package views;

import components.Button;
import components.inputs.Checkbox;
import components.Label;
import components.search.FilterInterface;
import components.template.SidebarRow;
import components.template.Template;
import components.template.Template.In;
import controllers.ConsultationController;
import models.Consultation;
import models.Device;
import models.Patient;
import utils.Colors;
import utils.ConsultationTableModel;
import net.miginfocom.swing.MigLayout;
import views.popups.UserMessage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class Tech extends JFrame {

    private static final long serialVersionUID = 7342776234650502381L;
    private final Template<Consultation> template = new Template<>(978, 550);
    private Consultation selectedConsultation = null;
    private Checkbox equipmentChkbx;

    /**
     * Création de la page pour les techniciens
     */
    public Tech(ConsultationController consultationController) {
        setTitle("GesPat — Technicien");
        setSize(1080, 575);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(978, 575));

        UserMessage.setActiveWindow(this);

        List<FilterInterface<Consultation>> filters = new ArrayList<>();
        filters.add(new SearchFilter());

        template.setSearchBar(
                consultationController.getAll(),
                new ConsultationTableModel(),
                (consultation, tUtils) -> setSelected(consultation),
                filters);

        Label tableTitle = new Label("Résultat de la recherche", Label.Styles.TITLE);
        template.add(tableTitle, In.MAIN_BODY);

        sidebarSetup();

        add(template);
        setVisible(true);
    }

    /**
     * Permet de récupérer les appareils attribués aux patients
     */
    private Component getDevicesCheckboxes() {
        final JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1, insets 0"));
        panel.setBackground(Colors.SECONDARY);

        panel.add(new Label("Appareillage attribué :"));

        final Device equipment = selectedConsultation.getRequiredEquipment();

        equipmentChkbx = new Checkbox(equipment.getLabel(), equipment.getState() == Device.STATES.ASSIGNED);
        equipmentChkbx.addActionListener(e -> save());
        panel.add(equipmentChkbx);

        return panel;
    }

    /**
     * Définit la consultation sélectionnée
     *
     * @param consultation
     */
    private void setSelected(Consultation consultation) {

        selectedConsultation = consultation;
        updateGraphics();
    }

    /**
     * Mets à jour l'interface
     *
     * @param consultation
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
        if (selectedConsultation == null) {
            // Si aucun patient n'est selectionner, on affiche un message.
            template.add(new Label("Selectionnez un patient."), In.SIDEBAR_BODY);
            return;
        }
        Patient patient = selectedConsultation.getPatient();

        template.add(new SidebarRow("Patient :", patient.getFullname()), In.SIDEBAR_BODY);

        template.add(new SidebarRow("Sécurité sociale :", "" + patient.getSocialId()), In.SIDEBAR_BODY);

        template.add(new SidebarRow("Date de consultation :", selectedConsultation.getConsultedAt()), In.SIDEBAR_BODY);

        template.add(getDevicesCheckboxes(), In.SIDEBAR_BODY);

        Button saveBtn = new Button("Enregistrer les modifications", Button.Size.LARGE, Button.Style.OUTLINED, Button.Color.SECONDARY);
        saveBtn.addActionListener(e -> save());
        template.add(saveBtn, In.SIDEBAR_FOOTER);
    }

    /**
     * Permet d'enregistrer les modifications après une attribution d'appareillage
     */
    private void save() {
        selectedConsultation.getRequiredEquipment().setState(equipmentChkbx.isSelected());
        updateGraphics();
    }
}

/**
 * Filtre permettant d'afficher au technicien seulement les patients ayant besoin d'un appareil
 */
final class SearchFilter implements FilterInterface<Consultation> {

    @Override
    public List<Consultation> newFilter(List<Consultation> data) {
        List<Consultation> result = new ArrayList<>();

        for (Consultation consultation : data) {
            if (
                    consultation != null
                            && consultation.getRequiredEquipment().getLabel() != null
                            && !consultation.getRequiredEquipment().getLabel().equals("null")
                            && consultation.getRequiredEquipment().getState() != Device.STATES.ASSIGNED
            ) {
                result.add(consultation);
            }
        }

        return result;
    }

}
