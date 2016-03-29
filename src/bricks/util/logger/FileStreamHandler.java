package bricks.util.logger;

import java.io.File;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

/**
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 * @todo    Create handler for multi-threading concurrent writing
 *          by adding the message to a queue and building a lock mechanism
 */
public class FileStreamHandler implements StreamHandler {
    /**
     * Log filename
     */
    private String logFilename = null;

    /**
     * Log path directory
     */
    private String logPath = null;

    /**
     * @param filename What name should the log file have
     */
    public void setLogFilename(String filename) {
        this.logFilename = filename;
    }

    /**
     * @param path Where to write the log file
     */
    public void setLogPath(String path) {
        this.logPath = path;
    }

    /**
    private String getCurrentWorkingPath() {
        return Paths.get(".").toAbsolutePath().normalize().toString() + File.separatorChar;
    }     */

    /**
     * Writes the message to file
     * @param message 
     */
    @Override
    public void stream(String message) {
        try {
            Path log_file = Paths.get(this.logPath + File.separatorChar + this.logFilename);
            if (Files.exists(log_file) == false) {
                Files.createFile(log_file);
            }
            Files.write(log_file, Arrays.asList(message), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }
    }
}
