/**
 * Copyright (c) 2016 Adrian Tilita <adrian@tilita.ro>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 * @todo        Create handler for multi-threading concurrent writing
 *              by adding the message to a queue and building a lock mechanism
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
