package bricks.duplicateFileFinderService.Request;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileFilterComponentTest {
    /**
     * Functional Test of the entire FileFilterComponent
     */
    @Test
    public void testFileFilterCompoent() {
        String extension = "log";
        FileFilterComponent instance = new FileFilterComponent();
        instance.addFileExtension(extension);
        instance.addFileExtension("." + extension);
        instance.removeFileExtension(extension);
        instance.removeFileExtension(extension);
        instance.addFileExtension(extension);

        java.io.File file = new java.io.File(System.getProperty("user.dir") + File.separator + "tmp.log");
        if (file.exists()) {
            file.delete();
        }
        // file does not exist
        assertFalse(instance.accept(file));
        try {
            file.createNewFile();
            assertTrue(instance.accept(file));
        } catch (IOException e) {
            fail("IOExceptio thrown with message " + e.getMessage());
        }
        instance.removeAllFileExtensions();
        assertTrue(instance.accept(file));

        instance.addFileExtension("jpg");
        assertFalse(instance.accept(file));

        file.delete();
        java.io.File directory = new java.io.File(System.getProperty("user.dir") + File.separator);
        assertTrue(instance.accept(directory));
    }    
}