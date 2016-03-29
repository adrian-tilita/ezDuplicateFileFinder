package bricks.util.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoggerTest {
    private StreamHandler streamHandler = null;
    private Logger logger = null;
    public String message = null;
    public String testMessage = "A test message";

    @Before
    public void setUp() {
        // create mockup stream handler
        class MockStreamHandler implements StreamHandler {
            public LoggerTest instance = null;
            public MockStreamHandler(LoggerTest instace) {
                this.instance = instace;
            }
            @Override
            public void stream(String message) {
                this.instance.message = message;
            }
        }
        this.streamHandler = new MockStreamHandler(this);
        this.logger = new Logger();
        this.logger.setStreamHandler(streamHandler);
    }
    
    @After
    public void tearDown() {
        this.streamHandler = null;
        this.logger = null;
        this.message = null;
    }

    @Test
    public void testStreamHandlerSetter() {
        class MockLogger extends Logger {
            public StreamHandler getHandler() {
                return this.streamHandler;
            }
        }
        LoggerInterface mockedLogger = new MockLogger();
        mockedLogger.setStreamHandler(this.streamHandler);
        assertTrue(this.streamHandler.equals(((MockLogger)mockedLogger).getHandler()));
    }

    @Test(expected=NullPointerException.class)
    public void testNullStreamHandler() {
        LoggerInterface logger = new Logger();
        logger.logAlert("Message");
    }
    
    @Test
    public void testLogEmergency() {
        this.logger.logEmergency(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.EMERGENCY.toUpperCase() + "]"));
    }

    @Test
    public void testLogAlert() {
        this.logger.logAlert(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.ALERT.toUpperCase() + "]"));
    }

    @Test
    public void testLogCritical() {
        this.logger.logCritical(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.CRITICAL.toUpperCase() + "]"));
    }

    @Test
    public void testLogError() {
        this.logger.logError(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.ERROR.toUpperCase() + "]"));
    }

    @Test
    public void testLogWarning() {
        this.logger.logWarning(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.WARNING.toUpperCase() + "]"));
    }

    @Test
    public void testLogNotice() {
        this.logger.logNotice(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.NOTICE.toUpperCase() + "]"));
    }

    @Test
    public void testLogInfo() {
        this.logger.logInfo(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.INFO.toUpperCase() + "]"));
    }

    @Test
    public void testLogDebug() {
        this.logger.logDebug(this.testMessage);
        assertTrue(this.message.contains("[" + LoggerInterface.DEBUG.toUpperCase() + "]"));
    }

    @Test
    public void testLogMessage() {
        String customLogLevel = "custom_level";
        this.logger.log(customLogLevel, this.testMessage);
        assertTrue(this.message.endsWith("[" + customLogLevel.toUpperCase() + "] - " + this.testMessage));
    }
}
