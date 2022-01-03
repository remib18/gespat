package exceptions;

public class ConflictingDataException extends Exception {

    private static final long serialVersionUID = 7394597305708439886L;

    public ConflictingDataException(String message) {
        super(message);
    }

}
