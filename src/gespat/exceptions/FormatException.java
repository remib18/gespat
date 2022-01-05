package exceptions;

public class FormatException extends Exception {

    private static final long serialVersionUID = 1988229217009792666L;

    /**
     * Renvoie un message d'erreur si un format de données ne correspond pas, peut être utilisé pour la date.
     * @see java.time.LocalDate
     * @param message message d'erreur
     */
    public FormatException(String message) {
        super(message);
    }

}
