/**
 * 
 */
package handler.impl;

import java.io.File;

import org.apache.log4j.Logger;
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
 * @since 24 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CodeGenerator.class })
@SuppressStaticInitializationFor({ "org.apache.log4j.LogManager", "handler.impl.FileHandler",
        "handler.impl.CodeGenerator" })
public class CodeGeneratorTest {

    final String     destinationPath = "/path/to/destination/folder";

    protected Logger loggerMock;
    protected File   fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        Whitebox.setInternalState(CodeGenerator.class, loggerMock);
        // create mock File object
        fileMock = PowerMock.createMock(File.class);
    }

    @Test
    public void setGetDestinationDirectory() {
        final CodeGenerator codeGenerator = PowerMock.createPartialMock(CodeGenerator.class,
            new String[] { "processFile" });

        PowerMock.replayAll();

        codeGenerator.setDestinationDirectory(fileMock);
        Assert.assertEquals(codeGenerator.getDestinationDirectory(), fileMock);

        PowerMock.verifyAll();
    }

    @Test
    public void setGetTemplate() {
        final CodeGenerator codeGenerator = PowerMock.createPartialMock(CodeGenerator.class,
            new String[] { "processFile" });

        PowerMock.replayAll();

        codeGenerator.setTemplate(fileMock);
        Assert.assertEquals(codeGenerator.getTemplate(), fileMock);

        PowerMock.verifyAll();
    }

}
