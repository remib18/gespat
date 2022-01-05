package components.search;

import components.inputs.Text;
import utils.Colors;
import utils.threads.ThreadUtils;

import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SearchBar extends Text {

    private static final long serialVersionUID = -8637552053887129055L;

    /**
     * Crée une barre de recherche
     */
    public SearchBar() {
        super(10000, 41);

        EventQueue.invokeLater(() -> ThreadUtils.run(this::init, "SearchBar UI Load"));
    }

    /**
     * Ajoute un écouteur sur l'évènement de recherche
     * @param clb
     */
    public void addSearchListener(DocumentListener clb) {
        this.getDocument().addDocumentListener(clb);
    }

    /**
     * Construit la barre de recherche
     */
    private void init() {
        // Choix de la couleur du texte
        this.setForeground(Colors.TEXT_ON_SECONDARY);

        // Paramétrage des dimensions
        this.setMinimumSize(new Dimension(100, 20));
        this.setPreferredSize(new Dimension(10000, 41));

        // Réglage de la couleur de fond
        this.setBackground(Colors.SECONDARY);
        this.setOpaque(false);
    }

    /**
     * Dessine une barre de recherche avec des coins arrondis
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(hints);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, (double) getWidth() - 1, (double) getHeight() - 1, 15, 15));
        g2d.setColor(getForeground());
        super.paintComponent(g2d);
        g2d.dispose();
    }

}

