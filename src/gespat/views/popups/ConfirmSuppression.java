package views.popups;

import javax.swing.*;
import java.awt.*;

public class ConfirmSuppression {

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean getPopup(Container parent) {
		int value = JOptionPane.showConfirmDialog(parent, "Confirmer la suppression ? :");
		return value == 0;
	}

}
