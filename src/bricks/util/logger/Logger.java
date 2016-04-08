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

import java.text.SimpleDateFormat;

/**
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 * @todo        Make logger observable on level logs
 * @todo        Multiple stream handlers
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