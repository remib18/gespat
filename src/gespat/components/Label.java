package components;

import utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Label extends JLabel {

	private static final long serialVersionUID = -1843845123937225747L;

	/**
	 * Crée un nouveau label
	 *
	 * @param text contenu du label
	 */
	public Label(String text) {
		super(text);
		init(Styles.DEFAULT, Colors.TEXT_ON_PRIMARY);
	}

	/**
	 * Crée un nouveau label
	 *
	 * @param text  contenu du label
	 * @param style style du label
	 */
	public Label(String text, Styles style) {
		super(text);
		init(style, Colors.TEXT_ON_PRIMARY);
	}

	/**
	 * Crée un nouveau label
	 *
	 * @param text  contenu du label
	 * @param style style du label
	 * @param color couleur du label
	 */
	public Label(String text, Styles style, Color color) {
		super(text);
		init(style, color);
	}

	/**
	 * Crée un nouveau label
	 *
	 * @param text  contenu du label
	 * @param style style du label
	 * @param name  nom du label
	 */
	public Label(String text, Styles style, String name) {
		super(text);
		init(style, Colors.TEXT_ON_PRIMARY);
		setName(name);
	}

	/**
	 * Création du label
	 *
	 */
	private void init(Styles style, Color color) {
		// Choix de la couleur du texte
		this.setForeground(color);

		// Choix de la taille de la police
		switch (style) {
			case TITLE:
				setFont(new Font("Arial", Font.PLAIN, 20));
				break;

			case DEFAULT:
				setFont(new Font("Arial", Font.PLAIN, 16));
				break;
			default:
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Style non supporté.");
				break;
		}
	}

	/**
	 * Les différents styles de textes supportés
	 */
	public enum Styles {
		DEFAULT, TITLE
	}

}
