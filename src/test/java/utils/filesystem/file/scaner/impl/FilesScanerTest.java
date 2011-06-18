package utils.filesystem.file.scaner.impl;

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

import utils.filesystem.file.listener.ISetupFileEventListener;

/**
 * @author denis
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FilesScaner.class)
@SuppressStaticInitializationFor({"utils.filesystem.file.scaner.impl.FilesScaner", "org.apache.log4j.LogManager"})
public class FilesScanerTest {

    final String     directoryPath = "/path/to/source/dir";

    protected File   directoryMock;

    protected Logger loggerMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        Logger loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FilesScaner.class, loggerMock);
        // create mock File object
        directoryMock = PowerMock.createMock(File.class);
    }

    /**
     * Test method for {@link FilesScaner#setSourceDirectory(File)}.
     * 
     * @throws Exception
     */
    @Test
    public void testSetSourceDirectoryPath() throws Exception {
        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath);

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.setSourceDirectory(directoryMock);
        assertEquals(object.getSourceDirectory(), directoryMock);

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
        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(false);
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // error

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.sourceDirectory = directoryMock;

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
        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(false);
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // error

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.sourceDirectory = directoryMock;

        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    @Test
    public void testExecuteCanNotRead() throws Exception {
        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(true);
        EasyMock.expect(directoryMock.canRead()).andReturn(false);
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // error

        PowerMock.replayAll();

        FilesScaner object = new FilesScaner();
        object.sourceDirectory = directoryMock;

        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
     * |-directoryMock
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

        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(true);
        EasyMock.expect(directoryMock.canRead()).andReturn(true);
        EasyMock.expect(directoryMock.listFiles()).andReturn(new File[] {file01, file02});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);

        PowerMock.replayAll();

        object.sourceDirectory = directoryMock;

        assertTrue(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
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

        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(true);
        EasyMock.expect(directoryMock.canRead()).andReturn(true);
        EasyMock.expect(directoryMock.listFiles()).andReturn(new File[] {subfolder01});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);

        PowerMock.replayAll();

        object.sourceDirectory = directoryMock;

        assertTrue(object.execute());

        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link FilesScaner#execute()}.
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

        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(true);
        EasyMock.expect(directoryMock.canRead()).andReturn(true);
        EasyMock.expect(directoryMock.listFiles())
                .andReturn(new File[] {subfolder01, subfolder02, subfolder03, file04});

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file03).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file04).andReturn(true);

        PowerMock.replayAll();

        object.sourceDirectory = directoryMock;

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

        // prepare directoryMock object for testing
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // update
        EasyMock.expect(directoryMock.exists()).andReturn(true);
        EasyMock.expect(directoryMock.isDirectory()).andReturn(true);
        EasyMock.expect(directoryMock.canRead()).andReturn(true);
        EasyMock.expect(directoryMock.listFiles())
                .andReturn(new File[] {subfolder01, subfolder02, subfolder03, file04});
        EasyMock.expect(directoryMock.getAbsolutePath()).andReturn(directoryPath); // error

        FilesScaner object = PowerMock.createPartialMock(FilesScaner.class, "notifyListener");
        PowerMock.expectPrivate(object, "notifyListener", file01).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file02).andReturn(false);
        PowerMock.expectPrivate(object, "notifyListener", file03).andReturn(true);
        PowerMock.expectPrivate(object, "notifyListener", file04).andReturn(true);

        PowerMock.replayAll();

        object.sourceDirectory = directoryMock;

        assertFalse(object.execute());

        PowerMock.verifyAll();
    }

    @Test
    public void notifyListener() throws Exception {
        FilesScaner object = new FilesScaner();
        object.sourceDirectory = directoryMock;

        ISetupFileEventListener listener = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener.update(directoryMock)).andReturn(true);

        PowerMock.replayAll();

        object.attach(listener);

        assertTrue(object.notifyListener(directoryMock));

        PowerMock.verifyAll();
    }

    @Test
    public void notifyListenerMany() throws Exception {
        File file = new File("");

        FilesScaner object = new FilesScaner();
        object.sourceDirectory = directoryMock;

        ISetupFileEventListener listener01 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener01.update(file)).andReturn(true);

        ISetupFileEventListener listener02 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener02.update(file)).andReturn(false);

        ISetupFileEventListener listener03 = PowerMock.createMock(ISetupFileEventListener.class);
        EasyMock.expect(listener03.update(file)).andReturn(true);

        PowerMock.replayAll();

        object.attach(listener01);
        object.attach(listener02);
        object.attach(listener03);

        assertFalse(object.notifyListener(file));

        PowerMock.verifyAll();
    }

}
