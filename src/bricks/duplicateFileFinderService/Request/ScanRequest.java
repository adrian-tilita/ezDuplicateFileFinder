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
package bricks.duplicateFileFinderService.Request;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation and Segregation off all necessary Request data for Workers
 * initialized by MasterService
 *
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0
 * @since       2016-03
 * @see         bricks.duplicateFileFinderService.MasterService;
 */
public class ScanRequest implements FileScanRequest, FileChecksumRequest, DuplicateFileRequest {
    /**
     * Store all necessary directories that needs to be scanned
     * @see     #addDirectories(java.util.ArrayList) 
     * @see     #getDirectories() 
     */
    private final ArrayList<String> directories = new ArrayList<>();

    /**
     * Store FileFilter used for scan Workers
     * @see     #setFilter(java.io.FileFilter)
     * @see     #getFileFilter()
     */
    private FileFilter filter = null;

    /**
     * Store all files received from FileScanWorker
     * @see     #addFile(java.lang.String) 
     * @see     #getFileList() 
     */
    private final ArrayList<String> files = new ArrayList<>();

    /**
     * Store all files containing their generated unique id
     * @see     #addChecksumResult(java.lang.String, java.lang.String) 
     * @see     #getHashedFiles() 
     */
    private Map<String, String> hashedFiles = new HashMap<>();

    /**
     * Store all found duplicates
     * @see     #addDuplicateList(java.util.Map) 
     * @see     #getDuplicates()
     */
    private Map<String, ArrayList<String>> duplicates = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDirectories(ArrayList<String> directories) {
        this.directories.addAll(directories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getDirectories() {
        return this.directories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileFilter getFileFilter() {
        return this.filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFile(String file) {
        this.files.add(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getFileList() {
        return this.files;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChecksumResult(String file, String hash) {
        this.hashedFiles.put(file, hash);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getHashedFiles() {
        return this.hashedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDuplicateList(Map<String, ArrayList<String>> duplicates) {
        this.duplicates.putAll(duplicates);
    }

    /**
     * Retrieves all duplicate files found
     * @return 
     */
    public Map<String, ArrayList<String>> getDuplicates() {
        // by doing so, we send a copy off the result making this request
        // absolete and leave the GC chance to remove it by not referrencing
        // it to any 3rd party handler off the result
        return new HashMap<>(this.duplicates);
    }
}