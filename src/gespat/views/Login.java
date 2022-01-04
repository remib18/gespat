package views;

import components.Button;
import components.Label;
import components.template.XCenteredContainer;
import controllers.ConsultationController;
import controllers.PatientController;
import utils.Colors;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.Dimension;

public class Login extends JFrame {

    private static final long serialVersionUID = -2283950897638045412L;
    PatientController patientController;
    ConsultationController consultationController;

    public Login(PatientController patientController, ConsultationController consultationController) {
        setTitle("GesPat");
        setSize(580, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Colors.PRIMARY);

        this.patientController = patientController;
        this.consultationController = consultationController;



        add(this.getContent());
        setVisible(true);
    }

    private JPanel getButtons() {
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new MigLayout());
        buttonContainer.setBackground(Colors.PRIMARY);

        // Creating buttons
        Button button1 = new Button("Agent d’administration");
        Button button2 = new Button("Personnel médical");
        Button button3 = new Button("Technicien");

        // Adding ClickListener to buttons
        button1.addActionListener(e -> new Admin(patientController));

        button2.addActionListener(e -> new Doctor(patientController, consultationController));

        button3.addActionListener(e -> new Tech(consultationController));

        // Adding buttons to the container
        buttonContainer.add(button1);
        buttonContainer.add(button2, "gapleft 20");
        buttonContainer.add(button3, "gapleft 20");

        return buttonContainer;
    }

    private XCenteredContainer getContent() {
        final XCenteredContainer container = new XCenteredContainer(Colors.PRIMARY);

        container.add(new Label("GesPat", Label.Styles.TITLE));
        container.add(new Label("Veuillez vous identifier."));
        container.add(this.getButtons());
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(new Label("Ou accédez directement aux statistiques"));
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        Button goToStats = new Button("Y aller", Button.Size.SMALL);
        goToStats.addActionListener(e -> new Stats(patientController));
        container.add(goToStats);

        return container;
    }

}
