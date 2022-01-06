package exceptions;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = 3081170288725760352L;

	/**
	 * Renvoie un message d'erreur si une donnée n'est pas trouvée.
	 * @param message message d'erreur
	 */
	public NotFoundException(String message) {
		super(message);
	}

}
