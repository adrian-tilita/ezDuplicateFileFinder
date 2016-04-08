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
package layout.helper;

import bricks.common.Observer;
import bricks.common.Observable;
import bricks.common.NotificationMessage;
import bricks.duplicateFileFinderService.NotificationMessageType;

/**
 * Loading Trigger on scan worker
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class ScanProgressObserver extends Observable implements Observer {
    /**
     * Progress bar and label
     */
    private javax.swing.JProgressBar progressbar = null;
    private javax.swing.JLabel label = null;

    /**
     * The item to be modified upon event notification
     * @param   progressbar
     */
    public void setLoader(javax.swing.JProgressBar progressbar) {
        this.progressbar = progressbar;
    }

    /**
     * Set the label where we write all the service activity
     * @param label 
     */
    public void setScanInfo(javax.swing.JLabel label) {
        this.label = label;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void catchNotification(NotificationMessage notificationMessage) {
        switch (notificationMessage.getType()) {
            case(NotificationMessageType.START): 
                this.progressbar.setIndeterminate(true);
                break;
            case(NotificationMessageType.COMPLETE): 
                this.progressbar.setIndeterminate(false);
                this.label.setText("Finished");
                this.dispatchMessage("FINISHED", "", this);
                break;
            case(NotificationMessageType.INFO): 
                this.label.setText(notificationMessage.getMessage());
                break;
        }
    }
}