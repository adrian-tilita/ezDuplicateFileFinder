package bricks.duplicateFileFinderService.Request;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ScanRequest implements FileScanRequest, FileChecksumRequest, DuplicateFileRequest {

    private final ArrayList<String> directories = new ArrayList<>();

    private FileFilter filter = null;

    private final ArrayList<String> files = new ArrayList<>();

    private Map<String, String> hashedFiles = new HashMap<>();

    public void addDirectories(ArrayList<String> directories) {
        this.directories.addAll(directories);
    }

    public ArrayList<String> getDirectories() {
        return this.directories;
    }

    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    public FileFilter getFileFilter() {
        return this.filter;
    }

    public void addFile(String file) {
        this.files.add(file);
    }

    public ArrayList<String> getFileList() {
        return this.files;
    }

    public void addChecksumResult(String file, String hash) {
        this.hashedFiles.put(file, hash);
    }

    public Map<String, String> getHashedFiles() {
        return this.hashedFiles;
    }
}
