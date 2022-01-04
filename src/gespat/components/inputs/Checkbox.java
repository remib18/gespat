package components.inputs;

import utils.Colors;

import javax.swing.*;

public class Checkbox extends JCheckBox {

    private static final long serialVersionUID = -2212090774608187733L;

    /**
     * Création d'une boite à cocher
     * @param string Texte descriptif
     */
    public Checkbox(String string) {
        super(string);
        init();
    }

    /**
     * Création d'une boite à cocher
     * @param string Texte descriptif
     * @param checked Etat par défaut
     */
    public Checkbox(String string, boolean checked) {
        super(string, checked);
        init();
    }

    private void init() {
        setForeground(Colors.TEXT_ON_PRIMARY);
    }

}
