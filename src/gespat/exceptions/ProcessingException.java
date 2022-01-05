package exceptions;

public class ProcessingException extends Exception {

    private static final long serialVersionUID = -8351429584722880162L;

    /**
     * Renvoie une erreur en cas de problème lors du traitement des données.
     * @see java.io.IOException
     * @param message message d'erreur
     */
    public ProcessingException(String message) {
        super(message);
    }

}
