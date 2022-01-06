package exceptions;

public class ConflictingDataException extends Exception {

	private static final long serialVersionUID = 7394597305708439886L;

	/**
	 * Renvoie un message d'erreur si des données rentrent en confrontation
	 * @param message message d'erreur
	 */
	public ConflictingDataException(String message) {
		super(message);
	}

}
