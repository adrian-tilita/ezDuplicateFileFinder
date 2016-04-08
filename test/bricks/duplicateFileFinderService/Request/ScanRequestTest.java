package bricks.duplicateFileFinderService.Request;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScanRequestTest {
    @Test
    public void testScanRequest() {
        ScanRequest instance = new ScanRequest();
        // Set-Get Directories
        ArrayList<String> directories = new ArrayList<>();
        directories.add("test directory");
        instance.addDirectories(directories);
        assertTrue(directories.equals(instance.getDirectories()));
    }

    @Test
    public void testFileFilter() {
        ScanRequest instance = new ScanRequest();
        // Set-Get FileFilter
        class FileFilterImplentation implements FileFilter {
            @Override
            public boolean accept(java.io.File file) {
                return true;
            }
        }
        java.io.FileFilter filter = new FileFilterImplentation();
        instance.setFilter(filter);
        assertTrue(filter.equals(instance.getFileFilter()));
    }

    @Test
    public void testFileList() {
        ScanRequest instance = new ScanRequest();
        // set-get files
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add("Test 1");
        fileList.add("Test 2");
        fileList.forEach(file -> {
            instance.addFile(file);
        });
        assertTrue(fileList.equals(instance.getFileList()));
    }

    @Test
    public void testHashFiles() {
        ScanRequest instance = new ScanRequest();
        // set-get hashed files
        Map<String, String> hashedFiles = new HashMap<>();
        hashedFiles.put("A", "B");
        hashedFiles.put("C", "D");
        hashedFiles.put("E", "B");
        hashedFiles.forEach( (file, hash) -> {
            instance.addChecksumResult(file, hash);
        });
        assertTrue(hashedFiles.equals(instance.getHashedFiles()));
    }

    @Test
    public void testHashedFiles() {
        ScanRequest instance = new ScanRequest();

        Map<String, ArrayList<String>> hashedGroup = new HashMap<>();
        ArrayList<String> fileGroup = new ArrayList<>();
        fileGroup.add("Test 1");
        hashedGroup.put("test", fileGroup);
        instance.addDuplicateList(hashedGroup);
        assertTrue(hashedGroup.equals(instance.getDuplicates()));
    }
}
