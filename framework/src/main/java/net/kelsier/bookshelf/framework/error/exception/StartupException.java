package net.kelsier.bookshelf.framework.error.exception;

/**
 * Throw an exception without a stack trace
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public class StartupException extends RuntimeException {

    /**
     * Constructor
     *
     * @param message Message to include
     */
    public StartupException(final String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message Message to include
     * @param e Exception
     */
    public StartupException(final String message, final Exception e) {
        super(message, e);
    }

    /**
     * Do not include a stack trace
     * @return this
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
