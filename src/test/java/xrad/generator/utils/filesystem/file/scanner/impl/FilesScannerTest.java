package xrad.generator.utils.filesystem.file.scanner.impl;

import common.event.IObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FilesScanner.class)
@SuppressStaticInitializationFor({"xrad.generator.utils.filesystem.file.scanner.impl.FilesScanner", "org.slf4j.LoggerFactory"})
public class FilesScannerTest {

    final String directoryPath = "/path/to/source/dir";

    protected File directoryMock;

    protected Logger loggerMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(FilesScanner.class, loggerMock);
        // create mock File object
        directoryMock = PowerMockito.mock(File.class);
    }

    protected File createFileMock(final String path) {
        final File fileMock = mock(File.class);

        when(fileMock.exists()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(false);
        when(fileMock.getAbsolutePath()).thenReturn(path);

        return fileMock;
    }

    protected void verifyFileMock(final File file) {
        verify(file, times(1)).exists();
        verify(file, times(1)).canRead();
        verify(file, times(1)).isDirectory();
        verify(file, times(1)).getAbsolutePath();
    }

    protected File createDirectoryMock(final File[] files) {
        final File directoryMock = mock(File.class);

        when(directoryMock.exists()).thenReturn(true);
        when(directoryMock.canRead()).thenReturn(true);
        when(directoryMock.isDirectory()).thenReturn(true);
        when(directoryMock.listFiles()).thenReturn(files);

        return directoryMock;
    }

    protected void verifyDirectoryMock(final File directoryMock) {
        verify(directoryMock, times(1)).exists();
        verify(directoryMock, times(1)).canRead();
        verify(directoryMock, times(1)).isDirectory();
        verify(directoryMock, times(1)).listFiles();
    }

    protected File createSourceDirectoryMock(final File[] files) {
        final File sourceDirectoryMock = mock(File.class);

        doReturn(directoryPath).when(sourceDirectoryMock).getAbsolutePath();
        doReturn(true).when(sourceDirectoryMock).exists();
        doReturn(true).when(sourceDirectoryMock).isDirectory();
        doReturn(true).when(sourceDirectoryMock).canRead();
        doReturn(files).when(sourceDirectoryMock).listFiles();

        doReturn(true).when(sourceDirectoryMock).exists();
        doReturn(true).when(sourceDirectoryMock).isDirectory();
        doReturn(files).when(sourceDirectoryMock).listFiles();

        return sourceDirectoryMock;
    }

    protected void verifySourceDirectoryMock(final File sourceDirectoryMock) {
        verify(sourceDirectoryMock, times(1)).getAbsolutePath();
        verify(sourceDirectoryMock, times(1)).exists();
        verify(sourceDirectoryMock, times(1)).isDirectory();
        verify(sourceDirectoryMock, times(1)).canRead();
        verify(sourceDirectoryMock, times(1)).listFiles();
    }

    /**
     * Test method for {@link FilesScanner#setSourceDirectory(File)}.
     *
     * @throws Exception
     */
    @Test
    public void setGetSourceDirectory() {
        // behaviour specification
        doReturn(directoryPath).when(directoryMock).getAbsolutePath();

        // begin test
        final FilesScanner object = new FilesScanner();
        object.setSourceDirectory(directoryMock);
        assertEquals(object.sourceDirectory, directoryMock);
        assertEquals(object.getSourceDirectory(), directoryMock);

        // verify directoryMock object
        verifyNoMoreInteractions(directoryMock);
    }

    /**
     * Test method for {@link FilesScanner#execute()}.
     * |-not exist folder
     *
     * @throws Exception
     */
    @Test
    public void testExecuteNotExistDirectory() {
        // behaviour specification
        doReturn(false).when(directoryMock).exists();
        doReturn(directoryPath).when(directoryMock).getAbsolutePath();

        // do test
        final FilesScanner object = new FilesScanner();
        object.sourceDirectory = directoryMock;

        assertFalse(object.execute());

        // expectation specification
        verify(directoryMock, times(1)).getAbsolutePath();
        verify(directoryMock, times(1)).exists();

        verifyNoMoreInteractions(directoryMock);
    }

    @Test
    public void testExecuteCanNotRead() {
        // behaviour specification
        when(directoryMock.exists()).thenReturn(true);
        when(directoryMock.canRead()).thenReturn(false);
        when(directoryMock.getAbsolutePath()).thenReturn(directoryPath);

        // do test
        final FilesScanner object = new FilesScanner();
        object.sourceDirectory = directoryMock;

        assertFalse(object.execute());

        // expectation specification
        verify(directoryMock, times(1)).getAbsolutePath();
        verify(directoryMock, times(1)).exists();
        verify(directoryMock, times(1)).canRead();

        verifyNoMoreInteractions(directoryMock);
    }

    /**
     * Test method for {@link FilesScanner#execute()}.
     * |-not exist folder
     *
     * @throws Exception
     */
    @Test
    public void testExecuteNotDirectory() {
        // behaviour specification
        when(directoryMock.exists()).thenReturn(true);
        when(directoryMock.canRead()).thenReturn(true);
        when(directoryMock.isDirectory()).thenReturn(false);
        when(directoryMock.getAbsolutePath()).thenReturn(directoryPath);

        // do test
        final FilesScanner object = new FilesScanner();
        object.sourceDirectory = directoryMock;

        assertTrue(object.execute());

        // expectation specification
        verify(directoryMock, times(1)).getAbsolutePath();
        verify(directoryMock, times(1)).exists();
        verify(directoryMock, times(1)).canRead();
        verify(directoryMock, times(1)).isDirectory();

        verifyNoMoreInteractions(directoryMock);
    }

    /**
     * Test method for {@link FilesScanner#execute()}.
     * |-directoryMock
     * | |
     * | |-file01.xml
     * | |-file02.xml
     *
     * @throws Exception
     */
    @Test
    public void testExecuteFiles() throws Exception {
        // behaviour specification
        final File file01 = createFileMock("folder0A/file01.xml");
        final File file02 = createFileMock("folder0A/file02.xml");
        final File sourceDirectoryMock = createSourceDirectoryMock(new File[]{file01, file02});

        final FilesScanner object = spy(new FilesScanner());
        // behaviour specification
        doNothing().when(object, "notifyObservers", file01);
        doNothing().when(object, "notifyObservers", file02);

        // do test
        object.sourceDirectory = sourceDirectoryMock;
        assertTrue(object.execute());

        // expectation specification
        verifyPrivate(object, times(1)).invoke("notifyObservers", file01);
        verifyPrivate(object, times(1)).invoke("notifyObservers", file02);

        // expectation specification
        verifySourceDirectoryMock(sourceDirectoryMock);
        verifyFileMock(file01);
        verifyFileMock(file02);

        verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link FilesScanner#execute()}.
     * |-directoryMock
     * | |
     * | |-subfolder01
     * | | |-file01.xml
     * | | |-file02.xml
     *
     * @throws Exception
     */
    @Test
    public void testExecuteSubDirectory() throws Exception {
        // behaviour specification
        final File file01 = createFileMock("folder0A/subfolder01/file01.xml");
        final File file02 = createFileMock("folder0A/subfolder01/file02.xml");
        final File subfolder01 = createDirectoryMock(new File[]{file01, file02});
        final File sourceDirectoryMock = createSourceDirectoryMock(new File[]{subfolder01});

        final FilesScanner object = spy(new FilesScanner());
        // behaviour specification
        doNothing().when(object, "notifyObservers", file01);
        doNothing().when(object, "notifyObservers", file02);

        object.sourceDirectory = sourceDirectoryMock;
        assertTrue(object.execute());

        verifyPrivate(object, times(1)).invoke("notifyObservers", file01);
        verifyPrivate(object, times(1)).invoke("notifyObservers", file02);

        // expectation specification
        verifySourceDirectoryMock(sourceDirectoryMock);
        verifyDirectoryMock(subfolder01);
        verifyFileMock(file01);
        verifyFileMock(file02);

        verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link FilesScanner#execute()}.
     * |-directoryMock
     * | |
     * | |-subfolder01
     * | | |-file01.xml
     * | | |-file02.xml
     * | |
     * | |-subfolder02
     * | |
     * | |-subfolder03
     * | | |-file03.xml
     * | |
     * | |-file04.xml
     *
     * @throws Exception
     */
    @Test
    public void testExecute() throws Exception {
        // behaviour specification
        final File file01 = createFileMock("folder0A/subfolder01/file01.xml");
        final File file02 = createFileMock("folder0A/subfolder01/file02.xml");
        final File subfolder01 = createDirectoryMock(new File[]{file01, file02});
        final File subfolder02 = createDirectoryMock(new File[]{});
        final File file03 = createFileMock("folder0A/subfolder03/file03.xml");
        final File subfolder03 = createDirectoryMock(new File[]{file03});
        final File file04 = createFileMock("folder0A/file04.xml");
        final File sourceDirectoryMock = createSourceDirectoryMock(new File[]{subfolder01, subfolder02, subfolder03,
                file04});

        final FilesScanner object = PowerMockito.spy(new FilesScanner());
        // behaviour specification
        doNothing().when(object, "notifyObservers", file01);
        doNothing().when(object, "notifyObservers", file02);
        doNothing().when(object, "notifyObservers", file03);
        doNothing().when(object, "notifyObservers", file04);

        object.sourceDirectory = sourceDirectoryMock;
        assertTrue(object.execute());

        verifyPrivate(object, times(1)).invoke("notifyObservers", file01);
        verifyPrivate(object, times(1)).invoke("notifyObservers", file02);
        verifyPrivate(object, times(1)).invoke("notifyObservers", file03);
        verifyPrivate(object, times(1)).invoke("notifyObservers", file04);

        // expectation specification
        verifySourceDirectoryMock(sourceDirectoryMock);
        verifyFileMock(file04);
        verifyDirectoryMock(subfolder03);
        verifyFileMock(file03);
        verifyDirectoryMock(subfolder02);
        verifyDirectoryMock(subfolder01);
        verifyFileMock(file01);
        verifyFileMock(file02);

        verifyNoMoreInteractions();
    }

    @Test
    public void notifyListener() throws Exception {
        final File file = mock(File.class);

        final IObserver<File> listener = mock(IObserver.class);
        // behaviour specification
        doNothing().when(listener).update(file);

        // do test
        final FilesScanner object = new FilesScanner();
        object.attach(listener);
        object.notifyObservers(file);

        // expectation specification
        verify(listener, times(1)).update(file);

        verifyNoMoreInteractions();
    }

    @Test
    public void notifyListenerMany() throws Exception {
        final File file = mock(File.class);

        final IObserver<File> listener01 = mock(IObserver.class);
        // behaviour specification
        doNothing().when(listener01).update(file);

        final IObserver<File> listener02 = mock(IObserver.class);
        // behaviour specification
        doNothing().when(listener02).update(file);

        final IObserver<File> listener03 = mock(IObserver.class);
        // behaviour specification
        doNothing().when(listener03).update(file);

        // do test
        final FilesScanner object = new FilesScanner();

        object.attach(listener01);
        object.attach(listener02);
        object.attach(listener03);

        object.notifyObservers(file);

        // expectation specification
        verify(listener01, times(1)).update(file);
        verify(listener02, times(1)).update(file);
        verify(listener03, times(1)).update(file);
    }
}
