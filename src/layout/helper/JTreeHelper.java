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

import bricks.common.Observable;
import layout.helper.component.DataRetrieval;
import layout.helper.component.DirectoryDataRetrieval;
import layout.helper.component.TreeColapseSwingWorker;
import layout.helper.component.JTreeNotificationMessage;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.SwingUtilities;

/**
 * Handle and manage a Swing's JTree Component
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class JTreeHelper extends Observable implements TreeWillExpandListener {
    /**
     * Configurate if the JTree should have the root node visible
     */
    final private boolean VISIBLE_ROOT_NODE = true;

    /**
     * The depth of items to load on node expand - 0 for no limit
     */
    final private int LEVEL_DEPTH_LOAD = 2;

    /**
     * The actual JTree to be "helped"
     */
    private JTree JTree = null;

    /**
     * The parent frame
     */
    private JFrame frame = null;

    /**
     * RootNode Reference
     */
    private DefaultMutableTreeNode rootNode = null;

    /**
     * Data handler - the Strategy class that populate with data
     */
    private DataRetrieval dataRetrieval = null;

    /**
     * Stores the nodes already loaded
     */
    private ArrayList<String> cachedNodes = new ArrayList<>();

    /**
     * Service constructor - class cannot be instantiated without
     * the necessary elements injected
     * 
     * @param   JTree 
     * @param   frame
     */
    public JTreeHelper(JTree JTree, JFrame frame) {
        this.JTree = JTree;
        this.frame = frame;
    }

    /**
     * Injects a strategy of data retrieval
     * @param   value
     * @return  JTreeHelper
     */
    public JTreeHelper setDataRetrieval(DataRetrieval value) {
        this.dataRetrieval = value;
        return this;
    }

    /**
     * Return the DataRetrieval strategy
     * If not set, a default one will be instantiated
     * 
     * @return  DataRetrieval 
     */
    public DataRetrieval getDataRetrieval() {
        if (this.dataRetrieval == null) {
            this.dataRetrieval = new DirectoryDataRetrieval();
        }
        return this.dataRetrieval;
    }

    /**
     * Apply the helper service
     */
    public void apply() {
        this.emptyTree();
        this.addInitialData();
        this.attachEvents();
    }

    /**
     * Return the current selected item value
     * @return String
     */
    public String getSelectedValue() {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)this.JTree.getLastSelectedPathComponent();
        if (currentNode == null) {
            return "";
        }
        return this.dataRetrieval.getNodeValue(currentNode);
    }

    /**
     * Empties the tree from the default data
     */
    private void emptyTree() {
        this.rootNode = new DefaultMutableTreeNode(this.getDataRetrieval().getRootTextValue());
        DefaultTreeModel treeModel = new DefaultTreeModel(this.rootNode);
        this.JTree.setModel(treeModel);
    }

    /**
     * Add The initial data to load
     */
    private void addInitialData() {
        Thread initialTreeData = new Thread(() -> {
            try {
                this.dataRetrieval.addRootNodes(this.rootNode);
                // get child count
                int childCount = this.rootNode.getChildCount();
                if (childCount != 0) {
                    this.dispatchMessage(JTreeNotificationMessage.START, this.rootNode.toString(), this);
                    for (int i = 0; i < childCount; i++) {
                        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)this.rootNode.getChildAt(i);
                        this.dataRetrieval.addChildren(currentNode, 0, LEVEL_DEPTH_LOAD);
                        this.cacheNode(currentNode, 0);
                    }
                    this.dispatchMessage(JTreeNotificationMessage.COMPLETE, this.rootNode.toString(), this);
                }
                this.JTree.setRootVisible(this.VISIBLE_ROOT_NODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        initialTreeData.start();            
    }

    /**
     * Attach the events
     */
    private void attachEvents() {
        this.JTree.addTreeWillExpandListener(this);
    }

    /**
     * On expand we trigger loading next nodes
     * @param   event
     * @throws  ExpandVetoException 
     */
    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
        String nodeId = this.dataRetrieval.getNodeValue(currentNode);
        boolean isCached = false;
        for (String cached:this.cachedNodes) {
            if (cached.equals(nodeId)) {
                isCached = true;
                break;
            }
        }
        if (isCached == false) {
            // decouple the processing in a Swing Worker
            TreeColapseSwingWorker worker = new TreeColapseSwingWorker(this, this.dataRetrieval);
            worker.setData(currentNode, LEVEL_DEPTH_LOAD);
            worker.execute();
            // we assume we can cache it, even if the process is not done
            this.cacheNode(currentNode, 0);
        }
    }

    /**
     * Empty implementation - just to be according TreeWillExpandListener interface
     * @param   tree
     * @throws  ExpandVetoException 
     */
    @Override
    public void treeWillCollapse(TreeExpansionEvent tree) throws ExpandVetoException {}

    /**
     * Cache each element so we do not retrieve it on expand
     * @param node 
     */
    private void cacheNode(DefaultMutableTreeNode node, int offset) {
        this.cachedNodes.add(this.dataRetrieval.getNodeValue(node));
        if (node.getChildCount() > 0 && this.LEVEL_DEPTH_LOAD > 2) {
            if (offset == this.LEVEL_DEPTH_LOAD - 2) {
                return;
            }
            for (int it = 0; it < node.getChildCount(); it++) {
                this.cacheNode((DefaultMutableTreeNode)node.getChildAt(it), offset+1);
            }
        }
    }

    /**
     * Update the entire frame after the TreeModel was modified in the swing worker
     */
    public void updateFrame() {
        SwingUtilities.updateComponentTreeUI(this.frame);
    }

    /**
     * Expose dispatch API for the Swing Worker
     * @param type
     * @param message 
     */
    public void dispatchMessage(String type, String message) {
        super.dispatchMessage(type, message, this);
    }
}
