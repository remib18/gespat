package views;

import components.template.Template;
import controllers.PatientController;
import models.Data;

import javax.swing.JFrame;

public class Stats extends JFrame{

    private static final long serialVersionUID = -1377226174334041975L;

    public Stats(PatientController patientController) {
        setTitle("GesPat — Statistiques");
        setSize(1080, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final Template<Data> template = new Template<>(978, 550);
        // template.setSearchBar(data, model);

        add(template);
        setVisible(true);
    }
}
