package bitbacktester;

/**
 *
 * @author jmaciak
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
