/**
 * Copyright (c) 2016 Adrian Tilita <adrian@tilita.ro>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package layout.helper.component;

import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Map;
import java.util.HashMap;

/**
 * Group sets off duplicates by color
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-04
 */
public class RowRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    /**
     * Forcing a UID for the nagging warnings (the serial represents 
     * the birth date off my beautiful daughter)
     */
    private static final long serialVersionUID = 19092015;

    public RowRenderer() {
        setOpaque(true);
    }

    /**
     * Reference identifiers for coloring
     */
    private Map<Integer, Boolean> identifiers = new HashMap<>();

    /**
     * Add a set off identifiers for groups off elements
     * @param idenfiers 
     */
    public void addIdentifiers(Map<Integer, Boolean> idenfiers) {
        this.identifiers.putAll(idenfiers);
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (this.identifiers.containsKey(row)) {
            cellComponent.setBackground(Color.YELLOW);
        } else {
            cellComponent.setBackground(Color.WHITE);
        }
        return cellComponent;
    }
}
