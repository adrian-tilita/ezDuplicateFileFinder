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
package bricks.duplicateFileFinderService;

import bricks.duplicateFileFinderService.Request.FileScanRequest;
import bricks.common.Observable;
import bricks.util.logger.LoggerAware;
import bricks.util.logger.LoggerInterface;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;

/**
 * Retrieves a list off files according to the Work Request and set the response
 * in the Work Request
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class FileScanWorker extends Observable implements WorkerInterface, LoggerAware  {
    /**
     * Directory container
     */
    private Queue<String> directories = null;

    /**
     * The request that defines what to scan
     */
    private FileScanRequest request = null;

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

    /**
     * Constructor - Initialize members
     * @param request
     */
    public FileScanWorker(FileScanRequest request) {
        this.directories = new LinkedList<>();
        this.request = request;
        this.directories.addAll(request.getDirectories());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.scanDirectories();
        this.dispatchMessage(NotificationMessageType.COMPLETE, null, this);
    }

    /**
     * List each directories and send for scanning
     */
    private void scanDirectories() {
        while (this.directories.isEmpty() == false) {
            this.dispatchMessage(NotificationMessageType.INFO, "Scanning " + this.directories.element(), this);
            this.listFiles(this.directories.element());
            this.directories.remove();
        }
    }

    /**
     * List each file found according to the request
     * @param path 
     */
    private void listFiles(String path) {
        File dir_path = new File(path);
        this.dispatchMessage("Scanning " + NotificationMessageType.INFO, path, this);
        File[] list = null;
        try {
            if (this.request.getFileFilter() == null) {
                list = dir_path.listFiles();
            } else {
                list = dir_path.listFiles(this.request.getFileFilter());
            }
        } catch (NullPointerException e) {
            this.getLogger().logDebug("Null for " + path + " {Exception: " + e.getMessage() + "}");
            return;
        } catch (SecurityException e) {
            this.getLogger().logDebug("Cannot read the following path " + path + " {Exception: " + e.getMessage() + "}");
            return;
        }
        if (list == null || list.length == 0) {
            return;
        }
        for (File file:list) {
            if (file.isDirectory()) {
                this.directories.add(file.getAbsolutePath());
                continue;
            }
            this.request.addFile(file.getAbsolutePath());
        }
    }
}
