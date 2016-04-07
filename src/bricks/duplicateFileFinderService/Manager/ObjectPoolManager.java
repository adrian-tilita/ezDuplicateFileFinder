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
package bricks.duplicateFileFinderService.Manager;

import bricks.util.logger.Logger;
import bricks.util.logger.LoggerInterface;
import bricks.util.logger.StreamHandler;
import bricks.util.logger.FileStreamHandler;

/**
 * Manager responsible for instantiating reusable or needed dependency objects
 * to maintain a certain fluency in object usage (avoid Singleton Syndrome for
 * other services and making it a Singleton)
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class ObjectPoolManager {
    /**
     * Itself reference
     */
    private static ObjectPoolManager instance = null;

    /**
     * Logger string reference as a service
     */
    private final static String Logger = "logger";

    /**
     * Logger reference
     */
    private LoggerInterface logger = null;

    /**
     * Build the constructor private so it cannot be initialized "outside"
     */
    private ObjectPoolManager() {
        // private constructor
    }

    /**
     * Get the instance off the Object
     * @return 
     */
    public static ObjectPoolManager getInstance() {
        if (ObjectPoolManager.instance == null) {
            ObjectPoolManager.instance = new ObjectPoolManager();
        }
        return ObjectPoolManager.instance;
    }

    /**
     * Return the builded instance off the logger
     * @return 
     */
    public LoggerInterface getLogger() {
        if (this.logger == null) {
            this.build(ObjectPoolManager.Logger);
        }
        return this.logger;
    }

    /**
     * Build any service requested (if it can handle it)
     * @param whom 
     */
    private void build(String whom) {
        switch (whom) {
            /**
             * Build Logger
             */
            case (ObjectPoolManager.Logger):
                StreamHandler handler = new FileStreamHandler();
                class OuputStreamHandler implements bricks.util.logger.StreamHandler {
                    public void stream(String message) {
                        System.out.println(message);
                    }
                }
                ((FileStreamHandler)handler).setLogFilename("app.log");
                ((FileStreamHandler)handler).setLogPath(System.getProperty("user.dir"));
                Logger loggerInstance = new Logger();
                loggerInstance.setStreamHandler(new OuputStreamHandler());
                this.logger = loggerInstance;
                break;
        }
    }
}