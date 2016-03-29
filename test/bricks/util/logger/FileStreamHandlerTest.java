package bricks.util.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

public class FileStreamHandlerTest {

    private String path = null;
    private String filename = null;

    @Before
    public void setUp() {
        this.path = Paths.get(".").toAbsolutePath().normalize().toString() + File.separatorChar;
        this.filename = "alongfilename";
    }

    @After
    public void tearDown() {
        this.path = null;
        this.filename = null;
        Path newfile = Paths.get(this.path + this.filename);

        if (Files.exists(newfile) == true) {
            try {
                Files.delete(newfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test of stream method, of class FileStreamHandler.
     */
    @Test
    public void testStreamNullSetters() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String message = "";
        FileStreamHandler instance = new FileStreamHandler();
        instance.stream(message);

        assertTrue(outContent.toString().contains("Could not write to log"));
    }

    /**
     * Test of stream method, of class FileStreamHandler.
     */
    @Test
    public void testStreamWriteFileCreationTrue() {
        String message = "TEST";
        FileStreamHandler instance = new FileStreamHandler();
        instance.setLogFilename(this.filename);
        instance.setLogPath(this.path);
        instance.stream(message);

        Path newfile = Paths.get(this.path + this.filename);
        // verify that the file was created
        assertTrue(Files.exists(newfile));
    }

    /**
     * Test of stream method, of class FileStreamHandler.
     */
    @Test
    public void testStreamWriteFileCreationFalse() {
        String message = "TEST";
        FileStreamHandler instance = new FileStreamHandler();
        instance.setLogFilename(this.filename);
        instance.setLogPath(this.path);

        Path newfile = Paths.get(this.path + this.filename);
        // I don't know why tearDown does not remove the file
        if (Files.exists(newfile) == true) {
            try {
                Files.delete(newfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // verify that the file was created
        assertFalse(Files.exists(newfile));
    }


    /**
     * Test of stream method, of class FileStreamHandler.
     */
    @Test
    public void testStreamFileCreate() {
        String message = "TEST";
        String createFileName = "anewfile";
        FileStreamHandler instance = new FileStreamHandler();
        instance.setLogFilename(createFileName);
        instance.setLogPath(this.path);
        // on stream should create the new file
        instance.stream(message);
        Path newfile = Paths.get(this.path + createFileName);
        assertTrue(Files.exists(newfile));
        try {
            Files.delete(newfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of stream method, of class FileStreamHandler.
     */
    @Test
    public void testStreamWrite() {
        String message = "TEST";
        FileStreamHandler instance = new FileStreamHandler();
        instance.setLogFilename(this.filename);
        instance.setLogPath(this.path);
        instance.stream(message);

        Path newfile = Paths.get(this.path + this.filename);
        // read content
        try {
            List<String> content = Files.readAllLines(newfile);
            String loggedMessage = content.get(0);
            assertTrue(loggedMessage.equals(message));
        } catch (Exception e) {
            fail("IO Error");
        }
    }
}