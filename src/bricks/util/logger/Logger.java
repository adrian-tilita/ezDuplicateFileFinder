package bricks.util.logger;

import java.text.SimpleDateFormat;

/**
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 * @todo    Make logger observable on level logs
 * @todo    Multiple stream handlers
 */
public class Logger implements LoggerInterface {
    /**
     * StreamHandler container
     */
    protected StreamHandler streamHandler = null;

    /**
     * Inject a custom stream handler
     * @param handler 
     */
    @Override
    public void setStreamHandler(StreamHandler handler) {
        this.streamHandler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logEmergency(String message) {
        this.log(LoggerInterface.EMERGENCY, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logAlert(String message) {
        this.log(LoggerInterface.ALERT, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logCritical(String message) {
        this.log(LoggerInterface.CRITICAL, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logError(String message) {
        this.log(LoggerInterface.ERROR, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logWarning(String message) {
        this.log(LoggerInterface.WARNING, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logNotice(String message) {
        this.log(LoggerInterface.NOTICE, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logInfo(String message) {
        this.log(LoggerInterface.INFO, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logDebug(String message) {
        this.log(LoggerInterface.DEBUG, message);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void logDump(String message) {
        this.log(LoggerInterface.DEBUG_DUMP, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String level, String message) {
        this.streamHandler.stream("[" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(System.currentTimeMillis()) + "][" + level.toUpperCase() + "] - " + message);
    }
}