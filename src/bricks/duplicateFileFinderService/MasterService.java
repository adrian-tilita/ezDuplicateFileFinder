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

import java.util.concurrent.ConcurrentLinkedQueue;
import bricks.util.logger.LoggerAware;
import bricks.util.logger.LoggerInterface;
import bricks.duplicateFileFinderService.Request.ScanRequest;
import bricks.common.Observable;
import bricks.common.Observer;
import bricks.common.NotificationMessage;

/**
 * Define a master service. Any client is coupled only with this service
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03-28
 * @todo        Benefit from multi-thread capabilities and implement
 *              ConsumerPolling for INFO messages
 */
public class MasterService extends Observable implements WorkerInterface, Observer, LoggerAware  {
    /**
     * WorkerList container
     */
    private ConcurrentLinkedQueue<WorkerInterface> workerList = null;

    /**
     * Request to be "worked" on
     */
    private ScanRequest request = null;

    /**
     * Logger container
     */
    private LoggerInterface logger = null;
    
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
     * Constructor
     * @param request 
     */
    public MasterService(ScanRequest request) {
        this.request = request;
        this.workerList = new ConcurrentLinkedQueue<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void catchNotification(NotificationMessage notificationMessage) {
        String messageType = notificationMessage.getType();
        switch (messageType) {
            case (NotificationMessageType.COMPLETE):
                this.getLogger().logDebug("Worker " + notificationMessage.getTarget().getClass().getCanonicalName() + " notified completion!");
                this.work();
             break;
            case (NotificationMessageType.INFO):
                this.getLogger().logDump("Worker " + notificationMessage.getTarget().getClass().getCanonicalName() + " sent an info with the message: " + notificationMessage.getMessage());
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        // build all workers and attach to list
        this.initWorkers();
        this.getLogger().logDebug("Started Master Worker with " + this.workerList.size() + " workers!");
        this.work();
    }

    /**
     * Initialize all workers and add them to Queue
     */
    public void initWorkers() {
        // FileScan service
        WorkerInterface fileScanWorker = new FileScanWorker(this.request);
        WorkerInterface checkSumWorker = new FileChecksumWorker(this.request);
        WorkerInterface duplicateFilterWorker = new DuplicateFilterWorker(this.request);

        ((LoggerAware)fileScanWorker).setLogger(this.getLogger());
        ((Observable)fileScanWorker).addObserver(this);
        // add the current service as observer for the child processes

        ((LoggerAware)checkSumWorker).setLogger(this.getLogger());
        ((Observable)checkSumWorker).addObserver(this);

        ((LoggerAware)duplicateFilterWorker).setLogger(this.getLogger());
        ((Observable)duplicateFilterWorker).addObserver(this);

        // add workers
        this.workerList.add(fileScanWorker);
        this.workerList.add(checkSumWorker);
        this.workerList.add(duplicateFilterWorker);
    }

    /**
     * Run each worker
     */
    private void work() {
        if (this.workerList.isEmpty() == true) {
            return;
        }
        WorkerInterface currentWorker = this.workerList.poll();
        this.getLogger().logDebug("Started worker " + currentWorker.getClass().getCanonicalName() + " and have other " + this.workerList.size() + " workers in pending");
        currentWorker.start();
    }
}