package components.inputs;

import utils.Colors;
import utils.threads.ThreadUtils;

import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.EventQueue;

public class Text extends JTextField {

    private static final long serialVersionUID = 7022986861981657819L;

    /**
     * Création d'un champ de type texte
     * @param width
     * @param heigth
     */
    public Text(int width, int heigth) {

        EventQueue.invokeLater(() -> ThreadUtils.run(() -> init(width, heigth), "Text UI Load"));
    }

    /**
     * Initialisation du composant
     * @param width
     * @param heigth
     */
    private void init(int width, int heigth) {
        this.setMinimumSize(new Dimension(10, 20));
        this.setPreferredSize(new Dimension(width, heigth));

        this.setBackground(Colors.SECONDARY);
    }

}
