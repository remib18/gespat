package views;

import components.template.Template;
import models.AbstractData;
import views.popups.UserMessage;

import javax.swing.*;

public class Stats extends JFrame {

	private static final long serialVersionUID = -1377226174334041975L;

	/**
	 * Création de la page contenant les statistiques
	 */
	public Stats() {
		setTitle("GesPat — Statistiques");
		setSize(1080, 575);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		UserMessage.setActiveWindow(this);

		final Template<AbstractData> template = new Template<>(978, 575);

		add(template);
		setVisible(true);
	}
}
