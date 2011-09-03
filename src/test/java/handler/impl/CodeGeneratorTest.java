/**
 * 
 */
package handler.impl;

import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 24 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CodeGenerator.class)
@SuppressStaticInitializationFor({ "handler.impl.CodeGenerator", "handler.impl.FileHandler", "org.slf4j.LoggerFactory" })
public class CodeGeneratorTest {

    final String            destinationPath = "/path/to/destination/folder";

    protected Logger        loggerMock;

    protected CodeGenerator codeGenerator   = Mockito.mock(CodeGenerator.class, Mockito.CALLS_REAL_METHODS);

    @Mock
    protected File          fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = mock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setGetDestinationDirectory() {
        codeGenerator.setDestinationDirectory(fileMock);
        Assert.assertEquals(codeGenerator.destinationDirectory, fileMock);
        Assert.assertEquals(codeGenerator.getDestinationDirectory(), fileMock);
    }

    @Test
    public void setGetTemplate() {
        codeGenerator.setTemplate(fileMock);
        Assert.assertEquals(codeGenerator.template, fileMock);
        Assert.assertEquals(codeGenerator.getTemplate(), fileMock);
    }

}
