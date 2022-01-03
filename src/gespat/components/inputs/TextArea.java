package components.inputs;

import utils.Colors;

import javax.swing.JTextArea;
import java.awt.EventQueue;
import java.awt.Dimension;

public class TextArea extends JTextArea {

    private static final long serialVersionUID = 4892749904730741971L;

    /**
     * Création d'un champ de type TextArea
     * @param width largeur
     * @param heigth hauteur
     */
    public TextArea(int width, int heigth) {
        EventQueue.invokeLater(() -> init(width, heigth));
    }

    /**
     * Initisation du composant
     * @param width
     * @param heigth
     */
    private void init(int width, int heigth) {
        this.setMinimumSize(new Dimension(100, 20));
        this.setPreferredSize(new Dimension(width, heigth));

        this.setBackground(Colors.PRIMARY);
    }
}
