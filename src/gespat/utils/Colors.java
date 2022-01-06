package utils;

import java.awt.*;

public class Colors {

	public static final Color PRIMARY = new Color(71, 85, 74);


	// ----- THEME ----- \\
	public static final Color SECONDARY = new Color(108, 119, 110);
	public static final Color TEXT_ON_PRIMARY = new Color(213, 229, 217);


	// ----- TEXTES ----- \\
	public static final Color TEXT_ON_SECONDARY = new Color(255, 255, 255);
	public static final Color BUTTON_PRIMARY = PRIMARY;


	// ----- BOUTONS ----- \\
	public static final Color BUTTON_SECONDARY = new Color(255, 255, 255, 60);
	public static final Color BUTTON_DANGER = new Color(249, 201, 201);

	private Colors() {
		throw new IllegalStateException("[COLORS]: Classe utilitaire.");
	}

}
