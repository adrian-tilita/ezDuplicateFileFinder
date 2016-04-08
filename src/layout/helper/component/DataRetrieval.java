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

/**
 * Define a DataRetrieval for the jTree Component
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-03
 */
public interface DataRetrieval {
    /**
     * Set the main TreeModel from which to start from
     * @param rootNode 
     */
    public void addRootNodes(javax.swing.tree.DefaultMutableTreeNode rootNode);

    /**
     * Add a new child node to the current node
     * @param treeNode
     * @param startIndex
     * @param depth 
     */
    public void addChildren(javax.swing.tree.DefaultMutableTreeNode treeNode, int startIndex, int depth);

    /**
     * Get level 0 root node name
     * @return 
     */
    public String getRootTextValue();

    /**
     * Get the current selected item String label
     * @param node
     * @return 
     */
    public String getNodeValue(javax.swing.tree.DefaultMutableTreeNode node);
}
