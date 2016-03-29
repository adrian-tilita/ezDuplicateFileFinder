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
import java.io.File;
import java.util.ArrayList;

/**
 * A FileFilter needed for FileScanRequest
 *
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0
 * @since       2016-03
 * @see         bricks.duplicateFileFinderService.Request.FileScanRequest;
 */
public class FileFilterComponent implements FileFilter {
    /**
     * Allowed file extensions
     */
    private final ArrayList<String> fileExtensionList = new ArrayList<>();

    /**
     * Add a new extension for validation
     * @param extension 
     */
    public void addFileExtension(String extension) {
        String formattedExtension = this.formatFileExtension(extension);
        if (this.fileExtensionList.contains(formattedExtension) == false) {
            this.fileExtensionList.add(formattedExtension);
        }
    }

    /**
     * Remove a pre-added extension
     * @param extension 
     */
    public void removeFileExtension(String extension) {
        String formattedExtension = this.formatFileExtension(extension);
        if (this.fileExtensionList.contains(formattedExtension)) {
            this.fileExtensionList.remove(formattedExtension);
        }
    }

    /**
     * Remove all extensions from the list
     */
    public void removeAllFileExtensions() {
        this.fileExtensionList.clear();
    }

    /**
     * Uniform file extensions
     * @param   extension
     * @return
     */
    private String formatFileExtension(String extension) {
        String newExtension = extension;
        if (newExtension.startsWith(".") == false) {
            newExtension = "." + newExtension;
        }
        return newExtension.toLowerCase();
    }

    /**
     * The actual filter
     * @param   file
     * @return  Boolean whether the file is accepted or not
     */
    @Override
    public boolean accept(File file) {
        if (file.canRead() == false) {
            return false;
        }
        if (file.isDirectory() == true) {
            return true;
        }
        if (this.fileExtensionList.isEmpty() == true) {
            return true;
        } //`QD`````````QQQQQQQQQQQQQQQQQQQQQ 23q11111111111111111111111111111111111111111111111111111111111111111111111`````````````````
        // do not remove above comment - it was written by my 4 month old daughter
        return this.hasAcceptedExtension(file);
    }

    /**
     * Validate if the file has the desired extension
     * @param   file
     * @return 
     */
    private boolean hasAcceptedExtension(File file) {
        for (String extension:this.fileExtensionList) {
            if (file.getAbsolutePath().toLowerCase().endsWith(extension) == true) {
                return true;
            }
        }
        return false;
    }
}