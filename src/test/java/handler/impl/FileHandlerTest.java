/**
 * 
 */
package handler.impl;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileHandler.class })
@SuppressStaticInitializationFor({ "handler.impl.FileHandler", "org.apache.log4j.LogManager" })
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

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        fileHandler.setDestination(fileMock);
        Assert.assertEquals(fileHandler.getDestination(), fileMock);

        PowerMock.verifyAll();
    }

    @Test
    public void getFileNamePattern() {
        Pattern pattern = Pattern.compile("a");

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        fileHandler.setFileNamePattern(pattern);
        Assert.assertEquals(fileHandler.getFileNamePattern(), pattern);

        PowerMock.verifyAll();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void updateWrongProcessFileValidation() throws Exception {
        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, "processFileValidation");
        PowerMock.expectPrivate(fileHandler, "processFileValidation", fileMock).andReturn(false);

        PowerMock.replayAll();

        Assert.assertFalse(fileHandler.update(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void updateWrongProcessFile() throws Exception {
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] {
                "processFileValidation", "processFile" });
        PowerMock.expectPrivate(fileHandler, "processFileValidation", fileMock).andReturn(true);
        PowerMock.expectPrivate(fileHandler, "processFile", fileMock).andReturn(false);

        PowerMock.replayAll();

        Assert.assertFalse(fileHandler.update(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void fileDoesNotExist() throws Exception {
        final File fileMock = PowerMock.createMock(File.class);
        EasyMock.expect(fileMock.exists()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void itDoesNotFile() throws Exception {
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void fileCouldNotBeRead() throws Exception {
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(false);
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void incorrectFileName() {
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(true);
        EasyMock.expect(fileMock.getName()).andReturn("test.test");
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        // create mock Matcher object
        Matcher matcherMock = PowerMock.createMock(Matcher.class);
        EasyMock.expect(matcherMock.matches()).andReturn(false);

        // create mock Pattern object
        Pattern fileNamePatternMock = PowerMock.createMock(Pattern.class);
        EasyMock.expect(fileNamePatternMock.matcher("test.test")).andReturn(matcherMock);
        EasyMock.expect(fileNamePatternMock.pattern()).andReturn("a");

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        fileHandler.fileNamePattern = fileNamePatternMock;
        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    public void correctFile() throws Exception {
        EasyMock.expect(fileMock.exists()).andReturn(true);
        EasyMock.expect(fileMock.isFile()).andReturn(true);
        EasyMock.expect(fileMock.canRead()).andReturn(true);
        EasyMock.expect(fileMock.getName()).andReturn("test.test");
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        // create mock Matcher object
        Matcher matcherMock = PowerMock.createMock(Matcher.class);
        EasyMock.expect(matcherMock.matches()).andReturn(true);

        // create mock Pattern object
        Pattern fileNamePatternMock = PowerMock.createMock(Pattern.class);
        EasyMock.expect(fileNamePatternMock.matcher("test.test")).andReturn(matcherMock);

        final FileHandler fileHandler = PowerMock.createPartialMock(FileHandler.class, new String[] { "processFile" });

        PowerMock.replayAll();

        fileHandler.fileNamePattern = fileNamePatternMock;
        Assert.assertTrue(fileHandler.processFileValidation(fileMock));

        PowerMock.verifyAll();
    }

}
