package bricks.util.logger;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLoggerAwareTest {
    /**
     * Test of setLogger method, of class FileScanService.
     */
    @Test
    public void testaSetLogger() {
        class LoggerAwareImplementantion extends AbstractLoggerAware {
        }
        AbstractLoggerAware instance = new LoggerAwareImplementantion();
        Logger logger = new Logger();
        instance.setLogger(logger);
        assertEquals(instance.getLogger(), logger);
    }

    /**
     * Test of getLoggerException, of class FileScanService.
     */
    @Test(expected=NullPointerException.class)
    public void testGetLoggerException() {
        class LoggerAwareImplementantion extends AbstractLoggerAware {
        }
        AbstractLoggerAware instance = new LoggerAwareImplementantion();
        instance.getLogger();
    }
}
