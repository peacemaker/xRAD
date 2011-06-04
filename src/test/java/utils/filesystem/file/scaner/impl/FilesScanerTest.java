package utils.filesystem.file.scaner.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import utils.filesystem.file.listener.ISetupFileEventListener;
import utils.filesystem.file.scaner.impl.FilesScaner;

/**
 * @author denis
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FilesScaner.class)
public class FilesScanerTest {

    final String sourceDirectoryPath      = "sourceDirectoryPath";

    final String destinationDerectoryPath = "destinationDerectoryPath";

    final String directoryPath            = "directoryPath";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for
     * {@link FilesScaner#setSourceDirectoryPath(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testSetSourceDirectoryPath() throws Exception {
    	File file = PowerMock.createMockAndExpectNew(File.class, sourceDirectoryPath);

        PowerMock.replayAll();
        
        FilesScaner object = new FilesScaner();
        object.setSourceDirectoryPath(sourceDirectoryPath);

        assertEquals(object.getSourceDirectory(), file);

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-not exist folder
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteNotExistDirectory() throws Exception {
        File fileSystem = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(fileSystem.exists()).andReturn(false);
        EasyMock.expect(fileSystem.getAbsolutePath()).andReturn(directoryPath);

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.setSourceDirectoryPath(directoryPath);

        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-not exist folder
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteNotDirectory() throws Exception {
        File fileSystem = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(fileSystem.exists()).andReturn(true);
        EasyMock.expect(fileSystem.isDirectory()).andReturn(false);
        EasyMock.expect(fileSystem.getAbsolutePath()).andReturn(directoryPath);

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.setSourceDirectoryPath(directoryPath);

        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-folder0A
     * | |
     * | |-file01.xml
     * | |-file02.xml
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteFiles() throws Exception {
        // making fixture for subfolder01
        File file01 = PowerMock.createMock(File.class);
        EasyMock.expect(file01.exists()).andReturn(true);
        EasyMock.expect(file01.isDirectory()).andReturn(false);
        EasyMock.expect(file01.getAbsolutePath()).andReturn("folder0A/file01.xml");

        File file02 = PowerMock.createMock(File.class);
        EasyMock.expect(file02.exists()).andReturn(true);
        EasyMock.expect(file02.isDirectory()).andReturn(false);
        EasyMock.expect(file02.getAbsolutePath()).andReturn("folder0A/file02.xml");

        File folder0A = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(folder0A.exists()).andReturn(true);
        EasyMock.expect(folder0A.isDirectory()).andReturn(true);
        EasyMock.expect(folder0A.listFiles()).andReturn(new File[] {file01, file02});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);

        PowerMock.replayAll();

        object.setSourceDirectoryPath(directoryPath);

        assertTrue(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-folder0A
     * | |
     * | |-subfolder01
     * | | |-file01.xml
     * | | |-file02.xml
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteSubDirectory() throws Exception {
        // making fixture for subfolder01
        File file01 = PowerMock.createMock(File.class);
        EasyMock.expect(file01.exists()).andReturn(true);
        EasyMock.expect(file01.isDirectory()).andReturn(false);
        EasyMock.expect(file01.getAbsolutePath()).andReturn("folder0A/subfolder01/file01.xml");

        File file02 = PowerMock.createMock(File.class);
        EasyMock.expect(file02.exists()).andReturn(true);
        EasyMock.expect(file02.isDirectory()).andReturn(false);
        EasyMock.expect(file02.getAbsolutePath()).andReturn("folder0A/subfolder01/file02.xml");

        File subfolder01 = PowerMock.createMock(File.class);

        EasyMock.expect(subfolder01.exists()).andReturn(true);
        EasyMock.expect(subfolder01.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder01.listFiles()).andReturn(new File[] {file01, file02});

        File folder0A = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(folder0A.exists()).andReturn(true);
        EasyMock.expect(folder0A.isDirectory()).andReturn(true);
        EasyMock.expect(folder0A.listFiles()).andReturn(new File[] {subfolder01});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);

        PowerMock.replayAll();

        object.setSourceDirectoryPath(directoryPath);

        assertTrue(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-folder0A
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
        // making fixture for subfolder01
        File file01 = PowerMock.createMock(File.class);
        EasyMock.expect(file01.exists()).andReturn(true);
        EasyMock.expect(file01.isDirectory()).andReturn(false);
        EasyMock.expect(file01.getAbsolutePath()).andReturn("folder0A/subfolder01/file01.xml");

        File file02 = PowerMock.createMock(File.class);
        EasyMock.expect(file02.exists()).andReturn(true);
        EasyMock.expect(file02.isDirectory()).andReturn(false);
        EasyMock.expect(file02.getAbsolutePath()).andReturn("folder0A/subfolder01/file02.xml");

        File subfolder01 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder01.exists()).andReturn(true);
        EasyMock.expect(subfolder01.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder01.listFiles()).andReturn(new File[] {file01, file02});

        // making fixture for subfolder02
        File subfolder02 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder02.exists()).andReturn(true);
        EasyMock.expect(subfolder02.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder02.listFiles()).andReturn(new File[] {});

        // making fixture for subfolder03
        File file03 = PowerMock.createMock(File.class);
        EasyMock.expect(file03.exists()).andReturn(true);
        EasyMock.expect(file03.isDirectory()).andReturn(false);
        EasyMock.expect(file03.getAbsolutePath()).andReturn("folder0A/subfolder03/file03.xml");

        File subfolder03 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder03.exists()).andReturn(true);
        EasyMock.expect(subfolder03.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder03.listFiles()).andReturn(new File[] {file03});

        // making fixture for folder0A
        File file04 = PowerMock.createMock(File.class);
        EasyMock.expect(file04.exists()).andReturn(true);
        EasyMock.expect(file04.isDirectory()).andReturn(false);
        EasyMock.expect(file04.getAbsolutePath()).andReturn("folder0A/file03.xml");

        File folder0A = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(folder0A.exists()).andReturn(true);
        EasyMock.expect(folder0A.isDirectory()).andReturn(true);
        EasyMock.expect(folder0A.listFiles()).andReturn(new File[] {subfolder01, subfolder02, subfolder03, file04});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file03).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file04).andReturn(true);

        PowerMock.replayAll();

        object.setSourceDirectoryPath(directoryPath);

        assertTrue(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-folder0A
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
    public void testExecuteOneFail() throws Exception {
        // making fixture for subfolder01
        File file01 = PowerMock.createMock(File.class);
        EasyMock.expect(file01.exists()).andReturn(true);
        EasyMock.expect(file01.isDirectory()).andReturn(false);
        EasyMock.expect(file01.getAbsolutePath()).andReturn("folder0A/subfolder01/file01.xml");

        File file02 = PowerMock.createMock(File.class);
        EasyMock.expect(file02.exists()).andReturn(true);
        EasyMock.expect(file02.isDirectory()).andReturn(false);
        EasyMock.expect(file02.getAbsolutePath()).andReturn("folder0A/subfolder01/file02.xml");

        File subfolder01 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder01.exists()).andReturn(true);
        EasyMock.expect(subfolder01.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder01.listFiles()).andReturn(new File[] {file01, file02});

        // making fixture for subfolder02
        File subfolder02 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder02.exists()).andReturn(true);
        EasyMock.expect(subfolder02.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder02.listFiles()).andReturn(new File[] {});

        // making fixture for subfolder03
        File file03 = PowerMock.createMock(File.class);
        EasyMock.expect(file03.exists()).andReturn(true);
        EasyMock.expect(file03.isDirectory()).andReturn(false);
        EasyMock.expect(file03.getAbsolutePath()).andReturn("folder0A/subfolder03/file03.xml");

        File subfolder03 = PowerMock.createMock(File.class);
        EasyMock.expect(subfolder03.exists()).andReturn(true);
        EasyMock.expect(subfolder03.isDirectory()).andReturn(true);
        EasyMock.expect(subfolder03.listFiles()).andReturn(new File[] {file03});

        // making fixture for folder0A
        File file04 = PowerMock.createMock(File.class);
        EasyMock.expect(file04.exists()).andReturn(true);
        EasyMock.expect(file04.isDirectory()).andReturn(false);
        EasyMock.expect(file04.getAbsolutePath()).andReturn("folder0A/file03.xml");

        File folder0A = PowerMock.createMockAndExpectNew(File.class, directoryPath);
        EasyMock.expect(folder0A.exists()).andReturn(true);
        EasyMock.expect(folder0A.isDirectory()).andReturn(true);
        EasyMock.expect(folder0A.listFiles()).andReturn(new File[] {subfolder01, subfolder02, subfolder03, file04});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(false);
        PowerMock.expectPrivate(object, "notifyListener", file03).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file04).andReturn(true);

        PowerMock.replayAll();

        object.setSourceDirectoryPath(directoryPath);
        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    @Test
    public void notifyListener() throws Exception {
        File file = new File("");

        FilesScaner object = new FilesScaner();
        object.setSourceDirectoryPath(sourceDirectoryPath);

        ISetupFileEventListener listener = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener.setup(file))
                .andReturn(true);

        PowerMock.replay(listener);

        object.attach(listener);

        assertTrue(object.notifyListener(file));

        PowerMock.verify(listener);
    }

    @Test
    public void notifyListenerMany() throws Exception {
        File file = new File("");

        FilesScaner object = new FilesScaner();
        object.setSourceDirectoryPath(sourceDirectoryPath);

        ISetupFileEventListener listener01 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener01.setup(file))
                .andReturn(true);

        ISetupFileEventListener listener02 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener02.setup(file))
                .andReturn(false);

        ISetupFileEventListener listener03 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener03.setup(file))
                .andReturn(true);

        PowerMock.replay(listener01, listener02, listener03);

        object.attach(listener01);
        object.attach(listener02);
        object.attach(listener03);

        assertFalse(object.notifyListener(file));

        PowerMock.verify(listener01, listener02, listener03);
    }

}
