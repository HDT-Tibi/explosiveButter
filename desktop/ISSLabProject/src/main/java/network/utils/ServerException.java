package network.utils;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class ServerException extends Exception {

    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
