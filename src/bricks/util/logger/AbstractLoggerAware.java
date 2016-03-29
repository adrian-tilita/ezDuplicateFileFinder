package bricks.util.logger;

/**
 * Inherit logger setter awareness
 * 
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 */
public class AbstractLoggerAware implements LoggerAware {
    /**
     * Logger container
     */
    protected LoggerInterface logger = null;
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void setLogger(LoggerInterface logger) {
        this.logger = logger;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public LoggerInterface getLogger() throws NullPointerException {
        if (this.logger == null) {
            throw new NullPointerException("No logger service was set!");
        }
        return this.logger;
    }
}
