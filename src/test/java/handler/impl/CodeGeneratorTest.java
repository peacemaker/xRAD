/**
 *
 */
package handler.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import java.io.File;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @since 24 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CodeGenerator.class)
@SuppressStaticInitializationFor({"handler.impl.FileHandler", "org.slf4j.LoggerFactory"})
public class CodeGeneratorTest {

    @Mock
    protected Logger loggerMock;

    @Mock
    protected File fileMock;

    protected CodeGenerator codeGenerator;

    @Before
    public void setUp() {
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        //MockitoAnnotations.initMocks(this);
        codeGenerator = mock(CodeGenerator.class, Mockito.CALLS_REAL_METHODS);
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
