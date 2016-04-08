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

import java.util.ArrayList;
import java.io.FileFilter;

/**
 * Work Request for FileScanWorker
 *
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 * @see         bricks.duplicateFileFinderService.FileScanWorker;
 */
public interface FileScanRequest {
    /**
     * Add all directories that should be processed by the worker
     * @param directories 
     */
    public void addDirectories(ArrayList<String> directories);

    /**
     * Retrieves all directories to be processed
     * @return 
     */
    public ArrayList<String> getDirectories();

    /**
     * Add FileFilter to filter files from scanning
     * @param filter 
     */
    public void setFilter(FileFilter filter);

    /**
     * Retrieves the FileFilter
     * @return 
     */
    public FileFilter getFileFilter();

    /**
     * Add Worker result
     * @param file 
     */
    public void addFile(String file);
}