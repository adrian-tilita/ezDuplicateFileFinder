package bricks.util.logger;

/**
 * Define LOGGER levels and methods
 * Based on RC5424 specifications + Extra debug levels
 * 
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 */
public interface LoggerInterface {
    /**
     * EMERGENCY level is usual when the application
     * has reached an unusable state
     */
    public static final String EMERGENCY = "emergency";

    /**
     * ALERT level is usually a situation when the
     * application has unusable components and should
     * take action immediately
     */
    public static final String ALERT = "alert";

    /**
     * CRITICAL level is usually the situation when the application
     * has broken components and cannot continue accurately
     */
    public static final String CRITICAL = "critical";

    /**
     * ERROR should suggest runtime issues
     * that do not require immediate attention
     */
    public static final String ERROR = "error";
    
    /**
     * WARNING level should suggest a situation that
     * may have not acted as expected but does not
     * cause application malfunction
     */
    public static final String WARNING = "warning";

    /**
     * NOTICE level should flag small issues that
     * occur without affecting the system
     */
    public static String NOTICE = "notice";

    /**
     * INFO level should contain only informational
     * messages like process steps
     */
    public static String INFO = "info";

    /**
     * DEBUG level should contain only messages that
     * can verify and help debugging of certain steps
     */
    public static String DEBUG = "debug";

    /**
     * DEBUG dump should contain all dumps (large quantity off data)
     * than can help debugging
     */
    public static String DEBUG_DUMP = "dump";

    /**
     * Inject a stream handler
     * @param handler 
     */
    public void setStreamHandler(StreamHandler handler);

    /**
     * Log a message of EMERGENCY level
     * @see     log()
     * @param   message 
     */
    public void logEmergency(String message);

    /**
     * Log a message of ALERT level
     * @see     log()
     * @param   message 
     */
    public void logAlert(String message);

    /**
     * Log a message of CRITICAL level
     * @see     log()
     * @param   message 
     */
    public void logCritical(String message);

    /**
     * Log an message of ERROR level
     * @see     log()
     * @param   message 
     */
    public void logError(String message);

    /**
     * Log an message of WARNING level
     * @see     log()
     * @param   message 
     */
    public void logWarning(String message);

    /**
     * Log an message of NOTICE level
     * @see     log()
     * @param   message 
     */
    public void logNotice(String message);

    /**
     * Log an message of INFO level
     * @see     log()
     * @param   message 
     */
    public void logInfo(String message);

    /**
     * Log an message of DEBUG level
     * @see     log()
     * @param   message 
     */
    public void logDebug(String message);

    /**
     * Log an message of DEBUG_DUMP level
     * @see     log()
     * @param   message 
     */
    public void logDump(String message);

    /**
     * Log a custom level or a defined one
     * @param level
     * @param message 
     */
    public void log(String level, String message);
}
