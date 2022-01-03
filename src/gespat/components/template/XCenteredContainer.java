package components.template;

import net.miginfocom.swing.MigLayout;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;

public class XCenteredContainer extends JPanel {

    private static final long serialVersionUID = 4994977914050994476L;
    private final JPanel mig = new JPanel();

    /**
     * Crée un conteneur vertical dont les éléments sont centrés
     * @param background couleur de fond
     */
    public XCenteredContainer(Color background) {
        // Alignement des éléments les uns par rapport aux autres
        mig.setLayout(new MigLayout("wrap 1", "c", "c"));
        mig.setBackground(background);

        // Alignement sur la place disponible
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(background);
        super.add(mig);
    }

    /**
     * Crée un conteneur vertical dont les éléments sont centrés
     * @param background couleur de fond
     */
    public XCenteredContainer(Color background, boolean insets) {
        // Alignement des éléments les uns par rapport aux autres
        String params = "wrap 1";
        if (!insets) params += ", insets 0";
        mig.setLayout(new MigLayout(params, "center", "center"));
        mig.setBackground(background);

        // Alignement sur la place disponible
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(background);
        super.add(mig);
    }

    /**
     * Ajoute un <code>Component</code> au conteneur et le retourne
     * @param comp composant à ajouter
     * @return le composant passé en paramètre
     * @see Component
     */
    @Override
    public Component add(Component comp) {
        mig.add(comp);

        return comp;
    }

}
