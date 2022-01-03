package components;

import utils.Colors;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {

    private static final long serialVersionUID = -1843845123937225747L;

    /** Les différents styles de textes supportés */
    public enum Styles {
        DEFAULT, TITLE
    }

    /**
     * @param text contenu du label
     */
    public Label(String text) {
        super(text);
        this.init(Styles.DEFAULT, Colors.TEXT_ON_PRIMARY);
    }

    /**
     * @param text contenu du label
     * @param style
     */
    public Label(String text, Styles style) {
        super(text);
        this.init(style, Colors.TEXT_ON_PRIMARY);
    }

    /**
     * @param text contenu du label
     * @param style
     */
    public Label(String text, Styles style, Color color) {
        super(text);
        this.init(style, color);
    }

    /**
     * @param text contenu du label
     * @param style
     * @param name nom du label
     */
    public Label(String text, Styles style, String name) {
        super(text);
        this.init(style, Colors.TEXT_ON_PRIMARY);
        this.setName(name);
    }

    /**
     * Création du label
     * @param style
     * @param style
     */
    private void init(Styles style, Color color) {
        // Choix de la couleur du texte
        this.setForeground(color);

        // Choix de la taille de la police
        switch (style) {
            case TITLE:
                this.setFont(new Font("Arial", Font.PLAIN, 20));
                break;

            case DEFAULT:
                this.setFont(new Font("Arial", Font.PLAIN, 16));
                break;
            default:
                System.err.println("[LABEL — INIT]: Style non supporté.");
                break;
        }
    }

}
