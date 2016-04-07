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
package layout.helper.component;

import bricks.common.Observable;
import layout.helper.JTreeHelper;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Swing worker for data-retrieval process so we will not block Swing repainting
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 * @todo        Better organize setters and dependencies
 */
public class TreeColapseSwingWorker extends SwingWorker<Integer,Integer> {
    /**
     * The Observer that should notify
     */
    private Observable parent = null;

    /**
     * DataRetrieval to be called
     */
    private DataRetrieval dataRetrieval = null;

    /**
     * The node to be processed
     */
    private DefaultMutableTreeNode node = null;

    /**
     * Level parameter to be used in dataRetrieval call
     */
    private int level = 1;

    /**
     * Constructor
     * @param parent
     * @param dataRetrieval 
     */
    public TreeColapseSwingWorker(Observable parent, DataRetrieval dataRetrieval) {
        this.parent = parent;
        this.dataRetrieval = dataRetrieval;
    }

    /**
     * Set data for data retrieval call
     * @param currentNode
     * @param level 
     */
    public void setData(DefaultMutableTreeNode currentNode, int level) {
        this.node = currentNode;
        this.level = level;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public Integer doInBackground() {
        ((JTreeHelper)this.parent).dispatchMessage(JTreeNotificationMessage.START, this.node.toString());
        this.dataRetrieval.addChildren(this.node, 0, this.level);
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void done() {
        ((JTreeHelper)this.parent).dispatchMessage(JTreeNotificationMessage.COMPLETE, this.node.toString());
        ((JTreeHelper)this.parent).updateFrame();
    }
}