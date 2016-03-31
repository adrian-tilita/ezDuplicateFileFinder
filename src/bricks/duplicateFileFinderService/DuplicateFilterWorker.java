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

import bricks.common.Observable;
import bricks.duplicateFileFinderService.Request.DuplicateFileRequest;
import bricks.util.logger.LoggerAware;
import bricks.util.logger.LoggerInterface;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Group and returns all duplicate files referenced by a unique criteria (hash)
 * retrieved from the Work Request and respond in Work Request
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class DuplicateFilterWorker extends Observable implements WorkerInterface, LoggerAware {
    /**
     * The request that defines what to scan
     */
    private DuplicateFileRequest request = null;

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
    public DuplicateFilterWorker(DuplicateFileRequest request) {
        this.request = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        Map<String, ArrayList<String>> groupedFiles = this.groupHashedFiles(this.request.getHashedFiles());
        Map<String, ArrayList<String>> foundDuplicates = this.filterDuplicates(groupedFiles);
        this.dispatchMessage(NotificationMessageType.COMPLETE, null, this);
    }

    /**
     * Group received files by hash
     * @param   list
     * @return 
     */
    protected Map<String, ArrayList<String>> groupHashedFiles(Map<String, String> list) {
        this.getLogger().logDebug("Grouping " + list.size() + " hashed files");
        Map<String, ArrayList<String>> duplicates = new HashMap<>();
        list.forEach(
            (String file, String hash) -> {
                if (duplicates.containsKey(hash)) {
                    duplicates.get(hash).add(file);
                } else {
                    ArrayList<String> fileList = new ArrayList<>();
                    fileList.add(file);
                    duplicates.put(hash, fileList);
                }
            }
        );
        return duplicates;
    }

    /**
     * Remove all groups that has only item with the same hash
     * @param   list
     * @return
     */
    protected Map<String, ArrayList<String>> filterDuplicates(Map<String, ArrayList<String>> list) {
        this.getLogger().logDebug("Removing elements with one entry from a set of " + list.size() + " groups");
        Iterator<Map.Entry<String,ArrayList<String>>> iterator = list.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,ArrayList<String>> currentLine = iterator.next();
            if (currentLine.getValue().size() == 1) {
                this.getLogger().logDump("Removed hash list: " + currentLine.getKey() + ". Only one file has this hash");
                iterator.remove();
                continue;
            }
            this.getLogger().logDebug("Found a group of " + currentLine.getValue().size() + " files with the hash " + currentLine.getKey());
        }
        return list;
    }
}