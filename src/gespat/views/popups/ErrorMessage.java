package views.popups;

import javax.swing.*;
import java.awt.*;

public class ErrorMessage {

    private static Component component = null;

    public enum LEVEL {
        User, System, Info
    }

    /**
     * Génère une interface pour afficher les potentielles erreurs.
     * @param message
     */
    @SuppressWarnings("MagicConstant")
    public ErrorMessage(String message, LEVEL level) {
        // TODO Auto-generated constructor stub
        JOptionPane.showMessageDialog(component, message, getTitle(level), getMessageType(level));

    }

    public static void setActiveWindow(Component cp) {
        component = cp;
    }

    private String getTitle(LEVEL level) {
        switch (level) {
            case User: return "Oups, il semberait que vous ayez oublié quelque chose !";
            case System: return "Un problème s'est produit...";
            default: return "Message";
        }
    }

    private int getMessageType(LEVEL level) {
        switch (level) {
            case User: return JOptionPane.WARNING_MESSAGE;
            case System: return JOptionPane.ERROR_MESSAGE;
            default: return JOptionPane.INFORMATION_MESSAGE;
        }
    }

}
