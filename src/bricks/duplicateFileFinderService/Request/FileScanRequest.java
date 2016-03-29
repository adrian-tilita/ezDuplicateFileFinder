package bricks.duplicateFileFinderService.Request;

import java.util.ArrayList;
import java.io.FileFilter;

public interface FileScanRequest {
    public void addDirectories(ArrayList<String> directories);
    public ArrayList<String> getDirectories();
    public void setFilter(FileFilter filter);
    public FileFilter getFileFilter();
    public void addFile(String file);
}
