package components.template;

import javax.swing.*;
import java.awt.*;

public class XSpaceBetween extends JPanel {

	public static final String WEST = BorderLayout.WEST;
	public static final String EAST = BorderLayout.EAST;

	/**
	 * Crée un composant permettant d'espacer au maximum deux colonnes
	 *
	 * @param a colonne de gauche
	 * @param b colonne de droite
	 */
	public XSpaceBetween(Component a, Component b) {
		init();

		setLayout(new BorderLayout());
		add(a, BorderLayout.WEST);
		add(b, BorderLayout.EAST);
	}

	/**
	 * Initialise le composant
	 */
	private void init() {
		setLayout(new BorderLayout());
		setOpaque(true);
		EventQueue.invokeLater(() -> {
			try {
				setPreferredSize(new Dimension(10000, getPreferredSize().height));
				setMinimumSize(new Dimension(getParent().getSize().width - 32, getPreferredSize().height));
				setBackground(getParent().getBackground());
			} catch (NullPointerException err) { /**/ }
		});
	}

	@Override
	public void add(Component comp, Object constraints) {
		switch ((String) constraints) {
			case WEST:
			case EAST:
				super.add(comp, constraints);
				break;
		}
	}
}
