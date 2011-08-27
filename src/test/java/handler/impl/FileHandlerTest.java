/**
 * 
 */
package handler.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import utils.filesystem.file.scaner.impl.FilesScaner;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FileHandler.class)
@SuppressStaticInitializationFor({ "handler.impl.FileHandler", "org.slf4j.LoggerFactory" })
public class FileHandlerTest {

    final String          filePath    = "/path/to/file";

    protected Logger      loggerMock;

    protected FileHandler fileHandler = Mockito.mock(FileHandler.class, Mockito.CALLS_REAL_METHODS);

    @Mock
    protected File        fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFileNamePattern() {
        final Pattern pattern = Pattern.compile("a");
        fileHandler.setFileNamePattern(pattern);
        Assert.assertEquals(fileHandler.fileNamePattern, pattern);
        Assert.assertEquals(fileHandler.getFileNamePattern(), pattern);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void updateWrongProcessFileValidation() throws Exception {
        // behaviour specification
        when(fileMock.getAbsolutePath()).thenReturn(filePath);
        doReturn(false).when(fileHandler).processFileValidation(fileMock);

        // do test
        fileHandler.update(fileMock);

        // expectation specification
        verify(fileHandler, times(1)).processFileValidation(fileMock);
        verify(fileHandler, times(0)).processFile(fileMock);
        verify(fileMock, times(2)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void updateWrongProcessFile() throws Exception {
        // behaviour specification
        when(fileMock.getAbsolutePath()).thenReturn(filePath);
        doReturn(true).when(fileHandler).processFileValidation(fileMock);
        doReturn(false).when(fileHandler).processFile(fileMock);

        // do test
        fileHandler.update(fileMock);

        // expectation specification
        verify(fileHandler, times(1)).processFileValidation(fileMock);
        verify(fileHandler, times(1)).processFile(fileMock);
        verify(fileMock, times(2)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void fileDoesNotExist() throws Exception {
        // behaviour specification
        when(fileMock.exists()).thenReturn(false);
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // do test
        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        // expectation specification
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void itDoesNotFile() throws Exception {
        // behaviour specification
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(false);
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // do test
        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        // expectation specification
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).isFile();
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void fileCouldNotBeRead() throws Exception {
        // behaviour specification
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(false);
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // do test
        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        // expectation specification
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).isFile();
        verify(fileMock, times(1)).canRead();
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void incorrectFileName() {
        // behaviour specification
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.getName()).thenReturn("test.test");
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // create mock Matcher object
        final Matcher matcherMock = mock(Matcher.class);
        when(matcherMock.matches()).thenReturn(false);

        // create mock Pattern object
        final Pattern fileNamePatternMock = mock(Pattern.class);
        when(fileNamePatternMock.matcher("test.test")).thenReturn(matcherMock);

        // do test
        fileHandler.fileNamePattern = fileNamePatternMock;
        Assert.assertFalse(fileHandler.processFileValidation(fileMock));

        // expectation specification
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).isFile();
        verify(fileMock, times(1)).canRead();
        verify(fileMock, times(1)).getName();
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void correctFile() throws Exception {
        // behaviour specification
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.getName()).thenReturn("test.test");
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // create mock Matcher object
        final Matcher matcherMock = mock(Matcher.class);
        when(matcherMock.matches()).thenReturn(true);

        // create mock Pattern object
        final Pattern fileNamePatternMock = mock(Pattern.class);
        when(fileNamePatternMock.matcher("test.test")).thenReturn(matcherMock);

        // do test
        fileHandler.fileNamePattern = fileNamePatternMock;
        Assert.assertTrue(fileHandler.processFileValidation(fileMock));

        // expectation specification
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).isFile();
        verify(fileMock, times(1)).canRead();
        verify(fileMock, times(1)).getName();
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

}
