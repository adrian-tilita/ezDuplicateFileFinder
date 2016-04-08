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
import bricks.duplicateFileFinderService.Request.FileChecksumRequest;
import bricks.util.logger.LoggerInterface;
import bricks.util.logger.LoggerAware;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate a unique identifier for a set off files marked in the Work Request
 * and builds a list with an associated unique Id -> file and pass the response
 * in the Work Request. In the current development the unique id is the file's
 * checksum
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class FileChecksumWorker extends Observable implements WorkerInterface, LoggerAware {
    /**
     * The request that defines what to scan
     */
    private FileChecksumRequest request = null;

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
    public FileChecksumWorker(FileChecksumRequest request) {
        this.request = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        Iterator<String> files = this.request.getFileList().iterator();
        while (files.hasNext()) {
            String filename = files.next();
            String hash = null;
            try {
                hash = this.hashFile(filename);
            } catch (Exception e) {
                this.getLogger().logWarning("Could not hash " + filename + " {Exception: " + e.getMessage() + "}");
            }
            if (hash != null) {
                this.dispatchMessage(NotificationMessageType.INFO, "Verifing file " + filename, this);
                this.request.addChecksumResult(filename, hash);
            }
        }
        this.dispatchMessage(NotificationMessageType.COMPLETE, null, this);
    }

    /**
     * Hash a given full path filename
     * @param filename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException 
     */
    private String hashFile(String filename) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        InputStream fileStream =  new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fileStream.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fileStream.close();
        byte[] result = complete.digest();
        String hash = "";
        for (int i=0; i < result.length; i++) {
            hash += Integer.toString( ( result[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hash; 
    }
}