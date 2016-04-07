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

import bricks.common.NotificationMessage;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.JProgressBar;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import bricks.common.Observer;
import bricks.duplicateFileFinderService.Request.FileFilterComponent;
import bricks.duplicateFileFinderService.Request.ScanRequest;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import layout.helper.component.ResultParser;
import bricks.duplicateFileFinderService.Manager.ObjectPoolManager;
import bricks.duplicateFileFinderService.MasterService;
import javax.swing.table.DefaultTableCellRenderer;
import layout.helper.component.ButtonRenderer;
import layout.helper.component.RowRenderer;

/**
 * Main layout helper, a glue/bridge between backend logic and view form
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-04
 */
public class MainHelper implements ActionListener, Observer {
    final public static String JTREE_COMPONENT          = "jtree";
    final public static String JTREE_PROGRESS_COMPONENT = "jtree_loader";
    final public static String DIR_ADD_BUTTON           = "add_dir";
    final public static String DIR_REMOVE_BUTTON        = "remove_dir";
    final public static String DIR_LIST                 = "dir_list";
    final public static String EXT_LIST                 = "ext_list";
    final public static String SCAN_BUTTON              = "scan_button";
    final public static String SCAN_PROGRESS_BAR        = "scan_progress_bar";
    final public static String SCAN_PROGRESS_INFO       = "scan_progress_info";
    final public static String RESULT_GRID              = "result_grid";

    /**
     * All dependent component
     */
    private Map<String, JComponent> components = new HashMap<>();

    /**
     * Main Window
     */
    private JFrame frame = null;

    /**
     * JTreeHelper for JTree selection reference
     */
    private JTreeHelper helper = null;

    /**
     * Store ScanRequest reference so it can be used to get the results
     */
    private ScanRequest requestReponse;

    /**
     * Upon "MasterWorker" COMPLETE state we can populate the table/grid
     * with the service result data
     * {@inheritDoc}
     */
    @Override
    public void catchNotification(NotificationMessage notificationMessage) {
        if (this.requestReponse.getDuplicates().isEmpty() == false) {
            this.addResultsToTable(this.requestReponse.getDuplicates());
        }
    }

    /**
     * Add main JFrame
     * @param frame 
     */
    public void addLayoutFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Inject any component that the helper should handle
     * @param key
     * @param component 
     * @return 
     */
    public MainHelper addComponent(String key, JComponent component) {
        this.components.put(key, component);
        return this;
    }

    /**
     * Actually execute helping process
     */
    public void init() {
        this.initDirectoryList();
        this.initExtensionList();
        this.initJtreeHelper();
        this.initTable();
        this.addButtonEvents();
    }

    /**
     * Initialize list for selected directories
     */
    private void initDirectoryList() {
        // clean lists
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("No element selected");
        JList directories = (JList)this.components.get(MainHelper.DIR_LIST);
        directories.setModel((ListModel)listModel);
        directories.setFont(directories.getFont().deriveFont(Font.ITALIC));
    }

    /**
     * Initialize extension list
     */
    private void initExtensionList() {
        // clean lists
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("All files (*.*)");
        listModel.addElement("Images (*.jpg, *.jpeg, *.bmp, *.png, *.gif)");
        listModel.addElement("Documents (*.rtf, *.txt, *.doc, *.xls, *.ppt, *.docx, *.xlsx, *.pptx)");
        listModel.addElement("PDF Document (*.pdf)");
        JList extensions = (JList)this.components.get(MainHelper.EXT_LIST);
        extensions.setModel((ListModel)listModel);
        extensions.setSelectedIndex(0);     // mark "all files" as default selection
    }

    /**
     * Instantiate JTreeHelper and set dependencies
     */
    private void initJtreeHelper() {
        this.helper = new JTreeHelper((JTree)this.components.get(MainHelper.JTREE_COMPONENT), this.frame);
        LoadingTreeObserver observer = new LoadingTreeObserver();
        observer.setLoader((JProgressBar)this.components.get(MainHelper.JTREE_PROGRESS_COMPONENT));
        this.helper.addObserver(observer);
        this.helper.apply();
    }

    /**
     * Instantiate and normalize JTable's data
     */
    private void initTable() {
        JTable table = (JTable)this.components.get(MainHelper.RESULT_GRID);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Filename");
        tableModel.addColumn("Filesize");
        tableModel.addColumn("Modified");
        tableModel.addColumn("Action");
        table.setModel(tableModel);

        List<TableColumn> listedColumns = Collections.list(table.getColumnModel().getColumns());
        table.getColumn(listedColumns.get(0).getIdentifier()).setPreferredWidth(200);
    }

    /**
     * Attach actions to buttons
     */
    private void addButtonEvents() {
        // add directory
        JButton addDir = (JButton)this.components.get(MainHelper.DIR_ADD_BUTTON);
        addDir.setActionCommand(MainHelper.DIR_ADD_BUTTON);
        addDir.addActionListener(this);

        // remove directory
        JButton removeDir = (JButton)this.components.get(MainHelper.DIR_REMOVE_BUTTON);
        removeDir.setActionCommand(MainHelper.DIR_REMOVE_BUTTON);
        removeDir.addActionListener(this);

        // Scan Action
        JButton scan = (JButton)this.components.get(MainHelper.SCAN_BUTTON);
        scan.setActionCommand(MainHelper.SCAN_BUTTON);
        scan.addActionListener(this);
    }

    /**
     * Selected item
     * @param item 
     */     
    private void addItemToSelection(String item) {
        JList directories = (JList)this.components.get(MainHelper.DIR_LIST);
        ListModel list = directories.getModel();
        if (directories.getFont().isItalic()) {
            directories.setFont(directories.getFont().deriveFont(Font.PLAIN));
            ((DefaultListModel)list).clear();
        }
        ((DefaultListModel)list).addElement(item);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        switch (command) {
            /**
             * Add items to selection path
            */
            case(MainHelper.DIR_ADD_BUTTON):
                String value = this.helper.getSelectedValue();
                if (value.equals("") == false) {
                    this.addItemToSelection(this.helper.getSelectedValue());
                }
                break;        
            /**
             * Remove the selected directories from selected paths
             **/
            case(MainHelper.DIR_REMOVE_BUTTON):
                JList item = (JList)this.components.get(MainHelper.DIR_LIST);
                int[] selections = item.getSelectedIndices();
                System.out.println(selections);
                for (int i = selections.length - 1; i >= 0; i--) {
                    ((DefaultListModel) item.getModel()).remove(selections[i]);
                }
                break;
            /**
             * Do the actual Scan Work
             */
            case(MainHelper.SCAN_BUTTON):
                // Build MasterService
                FileFilterComponent filter = new FileFilterComponent();
                ScanRequest request = new ScanRequest();
                // build extensions
                JList extensions = (JList)this.components.get(MainHelper.EXT_LIST);
                int[] extensionSelection = extensions.getSelectedIndices();
                ArrayList<String> extensionList = new ArrayList<>();
                for (int selectedExtension: extensionSelection) {
                    String selectedExtensionItem = ((String)extensions.getModel().getElementAt(selectedExtension));
                    extensionList.addAll(this.getExtensions(selectedExtensionItem));
                }
                if (extensionList.isEmpty() == false) {
                    for (String extension: extensionList) {
                        filter.addFileExtension(extension);
                    }
                }
                // build directory paths
                Object[] scanPaths = ((DefaultListModel)((JList)this.components.get(MainHelper.DIR_LIST)).getModel()).toArray();
                if (scanPaths.length != 0) {
                    ArrayList<String> scanPathList = new ArrayList<>();
                    for (Object pathItem: scanPaths) {
                        scanPathList.add((String)pathItem);
                    }
                    request.addDirectories(scanPathList);
                }
                this.requestReponse = request;
                ObjectPoolManager manager = ObjectPoolManager.getInstance();
                MasterService service = new MasterService(request);
                service.setLogger(manager.getLogger());

                Observer scanProgress = new ScanProgressObserver();
                ((bricks.common.Observable)scanProgress).addObserver(this);

                ((ScanProgressObserver)scanProgress).setLoader((JProgressBar)this.components.get(MainHelper.SCAN_PROGRESS_BAR));
                ((ScanProgressObserver)scanProgress).setScanInfo((JLabel)this.components.get(MainHelper.SCAN_PROGRESS_INFO));
                service.addObserver(scanProgress);
                
                layout.helper.component.ScanSwingWorker worker = new layout.helper.component.ScanSwingWorker(service);
                try {
                    worker.execute();
                } catch (Exception e) {
                    manager.getLogger().logCritical("Worker could not execute " + e.getMessage());
                }
                break;
        }
        
    }

    /**
     * Retrieve the selected extensions list based on the string format "Name (*.ext1, *.ext2)"
     * @param selectedItem
     * @return 
     */
    private ArrayList<String> getExtensions(String selectedItem) {
        ArrayList<String> foundExtensions = new ArrayList<>();
        int startStringIndex = selectedItem.indexOf("(") + 1;
        String[] tempExtensionList = selectedItem.substring(startStringIndex, selectedItem.length() - 1).split(",");
        for (String extension: tempExtensionList) {
            if (extension.equals("*.*")) {
                break;
            }
            foundExtensions.add(extension.trim().substring(1));
        }
        return foundExtensions;
    }

    /**
     * Populate the JTable with the worker's result
     * @param result 
     */
    private void addResultsToTable(Map<String, ArrayList<String>> result) {
        JTable table = (JTable)this.components.get(MainHelper.RESULT_GRID);
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();

        ArrayList<ArrayList<Map<String,String>>> parsedResult = ResultParser.parse(result);
        int iteration = 1;
        int rowId = 0;
        HashMap<Integer, Boolean> groups = new HashMap<>();
        for (ArrayList<Map<String,String>> groupedItems: parsedResult) {
            for (Map<String,String> fileInfo:groupedItems) {
                if (iteration % 2 == 0) {
                    groups.put(rowId, true);
                }
                tableModel.addRow(new String[] { fileInfo.get(ResultParser.FILE_NAME), fileInfo.get(ResultParser.FILE_SIZE), fileInfo.get(ResultParser.FILE_MODIFIED), "Delete" });
                rowId++;
            }
            iteration++;
        }
        DefaultTableCellRenderer renderer = new RowRenderer();
        ((RowRenderer)renderer).addIdentifiers(groups);
        table.getColumn("Filename").setCellRenderer(renderer);
        table.getColumn("Filesize").setCellRenderer(renderer);
        table.getColumn("Modified").setCellRenderer(renderer);

        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setPreferredWidth(100);
    }
}