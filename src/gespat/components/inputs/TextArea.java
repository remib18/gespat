package components.inputs;

import utils.Colors;

import javax.swing.*;
import java.awt.*;

public class TextArea extends JTextArea {

	private static final long serialVersionUID = 4892749904730741971L;

	/**
	 * Création d'un champ de type TextArea
	 *
	 * @param width  largeur
	 * @param height hauteur
	 */
	public TextArea(int width, int height) {
		EventQueue.invokeLater(() -> init(width, height));
	}

	/**
	 * Initisation du composant
	 *
	 */
	private void init(int width, int height) {
		this.setMinimumSize(new Dimension(100, 20));
		this.setPreferredSize(new Dimension(width, height));

		this.setBackground(Colors.PRIMARY);
	}
}
