package components.inputs;

import utils.Colors;

import javax.swing.*;
import java.awt.*;

public class Text extends JTextField {

	private static final long serialVersionUID = 7022986861981657819L;

	/**
	 * Création d'un champ de type texte
	 *
	 */
	public Text(int width, int height) {
		EventQueue.invokeLater(() -> init(width, height));
	}

	/**
	 * Initialisation du composant
	 *
	 */
	private void init(int width, int height) {
		this.setMinimumSize(new Dimension(10, 20));
		this.setPreferredSize(new Dimension(width, height));

		this.setBackground(Colors.SECONDARY);
	}

}
