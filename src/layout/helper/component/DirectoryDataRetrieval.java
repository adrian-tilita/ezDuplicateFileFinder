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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Implementation off a DirectoryList DataRetrieval for the jTree Component
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public class DirectoryDataRetrieval implements DataRetrieval {
    /**
     * Default root node text
     */
    protected String rootTextValue = "Local computer";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRootTextValue() {
        return this.rootTextValue;
    }

    /**
     * Add main root items, for example Windows partitions C:\, D:\
     * {@inheritDoc}
     */
    @Override
    public void addRootNodes(DefaultMutableTreeNode rootNode) {
        ArrayList<String> rootItems = this.getRootDirectories();
        rootItems.stream().forEach((item) -> {
            DefaultMutableTreeNode tmp_item = new DefaultMutableTreeNode(item);
            rootNode.add(tmp_item);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChildren(DefaultMutableTreeNode treeNode, int startIndex, int depth) {
        // stop when we reach the chunk size
        if (startIndex == depth) {
            return;
        }
        ArrayList<String> items = new ArrayList<>();
        try {
            ArrayList<String> temp = this.scanDir(this.getNodeValue(treeNode));
            temp.stream().forEach((item) -> {
                items.add(item);
            });
        } catch (Exception e) {
            // if we do not have permissions to scan the dir
            // or the dir is not readable no mather the reason
            // we ignore it
            return;
        }
        if (items.isEmpty()) {
            return;
        }
        // remove all previous children if they were just empty labels
        treeNode.removeAllChildren();
        items.stream().forEach((item) -> {
            DefaultMutableTreeNode tmp_item = new DefaultMutableTreeNode(item);
            treeNode.add(tmp_item);
            this.addChildren(tmp_item, startIndex+1, depth);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue(DefaultMutableTreeNode node) {
        String builderPath = "";
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
        if (parent == null) {
            return builderPath;
        }
        ArrayList<String> nodes = new ArrayList<>();
        DefaultMutableTreeNode currentNode;
        currentNode = node;
        while (currentNode.getParent() != null) {
            nodes.add(currentNode.toString());
            currentNode = (DefaultMutableTreeNode)currentNode.getParent();
        }
        Collections.reverse(nodes);
        for (String item : nodes) {
            if (item.substring(item.length() - 1).equals(File.separator)) {
                item = item.substring(0, item.length() - 1);
            }
            builderPath += item + File.separator;
        }
        return builderPath;
    }

    /**
     * Retrieve the main root "directories".
     * Example Windows partitions C:\, D:\ etc.
     * @return  ArrayList 
     */
    private ArrayList<String> getRootDirectories() {
        ArrayList<String> rootDirectories = new ArrayList<>();
        File[] list = File.listRoots();
        if (list.length != 0) {
            for (File item : list) {
                rootDirectories.add(item.getAbsolutePath());
            }
        }
        return rootDirectories;
    }

    /**
     * Return a list of files and directories in a given path
     * @param   path
     * @return  ArrayList<String> 
     */
    private ArrayList<String> scanDir(String path) {
        ArrayList<String> result = new ArrayList<>();
        File dir_path = new File(path);
        File[] list = dir_path.listFiles();
        if (list.length == 0) {
            return result;
        }
        for (File file:list) {
            if (file.isDirectory() == false || file.canRead() == false) {
                continue;
            }
            result.add(file.getName());
        }
        return result;
    }
}