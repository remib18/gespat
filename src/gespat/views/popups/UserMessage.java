package views.popups;

import javax.swing.*;
import java.awt.*;

public class UserMessage {

	private static Component component = null;

	/**
	 * Génère une interface pour afficher les potentielles erreurs.
	 *
	 */
	@SuppressWarnings("MagicConstant")
	public UserMessage(String message, LEVEL level) {
		JOptionPane.showMessageDialog(component, message, getTitle(level), getMessageType(level));

	}

	public static void setActiveWindow(Component cp) {
		component = cp;
	}

	private String getTitle(LEVEL level) {
		switch (level) {
			case Warning:
				return "Oups, un problème est survenu...";
			case Severe:
				return "Une erreur s'est produite...";
			default:
				return "Message";
		}
	}

	/**
	 * Permet d'obtenir le type de message en fonction de la gravité du message
	 */
	private int getMessageType(LEVEL level) {
		switch (level) {
			case Warning:
				return JOptionPane.WARNING_MESSAGE;
			case Severe:
				return JOptionPane.ERROR_MESSAGE;
			default:
				return JOptionPane.INFORMATION_MESSAGE;
		}
	}

	public enum LEVEL {
		Info, Warning, Severe
	}

}
