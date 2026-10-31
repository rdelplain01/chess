package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    private final int statusCode;

    public DataAccessException(String message) {
        this(500, message);
    }

    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
        this.statusCode = 500;
    }

    public DataAccessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
