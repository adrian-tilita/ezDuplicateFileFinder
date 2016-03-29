package bricks.util.logger;

/**
 * Forcing logger awareness by setting an interface
 * 
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 */
public interface LoggerAware {
    /**
     * Injects the logger
     * @param logger 
     */
    public void setLogger(LoggerInterface logger);

    /**
     * Retrieves the injected Logger
     * @return
     * @throws NullPointerException 
     */
    public LoggerInterface getLogger() throws NullPointerException;
}
