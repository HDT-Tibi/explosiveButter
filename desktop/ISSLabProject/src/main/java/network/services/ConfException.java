package network.services;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class ConfException extends Exception {

    public ConfException() {
        super();
    }

    public ConfException(String message) {
        super(message);
    }

    public ConfException(String message, Throwable cause) {
        super(message, cause);
    }

}
