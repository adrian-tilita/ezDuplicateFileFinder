package ezduplicatefilefinder;

import bricks.util.logger.FileStreamHandler;
import bricks.util.logger.Logger;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class EzDuplicateFileFinder {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Started project");
        
        ArrayList<String> list = new ArrayList<>();
        list.add("C:\\Intel\\");
        list.add("C:\\Drivers\\");
        list.add("C:\\Users\\adrian.tilita\\Downloads");
        class CustomFileFilter implements FileFilter {
            private ArrayList<String> fileExtensionList = new ArrayList<>();
            public CustomFileFilter() {
                this.fileExtensionList.add("jpg");
                this.fileExtensionList.add("pdf");
                this.fileExtensionList.add("log");
                this.fileExtensionList.add("txt");
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
                        //System.out.println(file.getAbsolutePath().toLowerCase() + " ends with " + extension); 
                        return true;
                    }
                }
                return false;
            }
        }
        bricks.duplicateFileFinderService.Request.ScanRequest request = new bricks.duplicateFileFinderService.Request.ScanRequest();
        request.addDirectories(list);
        request.setFilter(new CustomFileFilter());
        bricks.duplicateFileFinderService.MasterService service = new bricks.duplicateFileFinderService.MasterService(request);
        
        class OuputStreamHandler implements bricks.util.logger.StreamHandler {
            public void stream(String message) {
                System.out.println(message);
            }
        }
        OuputStreamHandler handler = new OuputStreamHandler();
        class CusotmLogger extends Logger {
            /*public void logDebug(String message) {
                //
            }*/
            public void logDump(String message) {
                //
            }
        }
        Logger loger = new CusotmLogger();
        loger.setStreamHandler(handler);
        service.setLogger(loger);
        try {
            service.start();
        } catch (Exception e) {
            System.out.println("Herrreeee,,");
            e.printStackTrace();
        }
    }
    
}
