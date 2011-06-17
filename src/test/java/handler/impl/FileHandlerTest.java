/**
 * 
 */
package handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * @author denis
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileHandler.class})
@SuppressStaticInitializationFor({"handler.impl.FileHandler", "org.apache.log4j.LogManager"})
public class FileHandlerTest {

    final String     filePath = "/path/to/file";

    protected Logger loggerMock;
    protected File   fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        // create mock File object
        fileMock = PowerMock.createMock(File.class);
    }

    @Test
    public void getSetDestination() {
        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, "processFileValidation");

        PowerMock.replayAll();

        fileHandler.setDestination(fileMock);
        assertEquals(fileHandler.getDestination(), fileMock);

        PowerMock.verifyAll();
    }

    @Test
    public void updateWrongProcessFileValidation() throws Exception {
        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, "processFileValidation");
        PowerMock.expectPrivate(fileHandler, "processFileValidation").andReturn(false);

        PowerMock.replayAll();

        assertFalse(fileHandler.update(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void updateWrongProcessFile() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {
                "processFileValidation", "processFile"});
        PowerMock.expectPrivate(fileHandler, "processFileValidation").andReturn(true);
        PowerMock.expectPrivate(fileHandler, "processFile").andReturn(false);

        PowerMock.replayAll();

        assertFalse(fileHandler.update(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void fileDoesNotExist() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertFalse(fileHandler.processFileValidation());

        PowerMock.verifyAll();
    }

    @Test
    public void itDoesNotFile() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertFalse(fileHandler.processFileValidation());

        PowerMock.verifyAll();
    }

    @Test
    public void fileCouldNotBeRead() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertFalse(fileHandler.processFileValidation());

        PowerMock.verifyAll();
    }

    @Test
    public void incorrectFileExtension() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(true);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile",
                "getFileExtension", "getDefaultFileExtension"});
        PowerMock.expectPrivate(fileHandler, "getFileExtension").andReturn("extension");
        PowerMock.expectPrivate(fileHandler, "getDefaultFileExtension").andReturn("");

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertFalse(fileHandler.processFileValidation());

        PowerMock.verifyAll();
    }

    @Test
    public void correctFile() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(true);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile",
                "getFileExtension", "getDefaultFileExtension"});
        PowerMock.expectPrivate(fileHandler, "getFileExtension").andReturn("extension");
        PowerMock.expectPrivate(fileHandler, "getDefaultFileExtension").andReturn("extension");

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertTrue(fileHandler.processFileValidation());

        PowerMock.verifyAll();
    }

    @Test
    public void emptyFileName() {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.getName()).andReturn("");

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertEquals("", fileHandler.getFileExtension());

        PowerMock.verifyAll();
    }

    @Test
    public void emptyFileExtension() {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.getName()).andReturn("test");

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertEquals("", fileHandler.getFileExtension());

        PowerMock.verifyAll();
    }

    @Test
    public void getFileExtension() {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.getName()).andReturn("test.test.xml");

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {"processFile"});

        PowerMock.replayAll();

        fileHandler.file = fileMock;
        assertEquals("xml", fileHandler.getFileExtension());

        PowerMock.verifyAll();
    }
}
