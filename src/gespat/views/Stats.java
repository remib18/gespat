package views;

import components.template.Template;
import controllers.PatientController;
import models.AbstractData;
import views.popups.ErrorMessage;

import javax.swing.JFrame;

public class Stats extends JFrame{

    private static final long serialVersionUID = -1377226174334041975L;

    public Stats(PatientController patientController) {
        setTitle("GesPat — Statistiques");
        setSize(1080, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ErrorMessage.setActiveWindow(this);

        final Template<AbstractData> template = new Template<>(978, 550);
        // template.setSearchBar(data, model);

        add(template);
        setVisible(true);
    }
}
