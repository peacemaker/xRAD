package handler.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.ObjectUtils.Null;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
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
 * @since 9 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ XsltBasedCodeGenerator.class })
@SuppressStaticInitializationFor({ "handler.impl.XsltBasedCodeGenerator", "handler.impl.CodeGenerator",
        "handler.impl.FileHandler", "org.slf4j.LoggerFactory" })
public class XsltBasedCodeGeneratorTest {

    final String     filePath = "/path/to/file";

    protected Logger loggerMock;

    @Mock
    protected File   fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = mock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @PrepareForTest(TransformerFactory.class)
    public void processFileWithTransformerConfigurationException() throws Exception {
        // prepare fileMock object for testing
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        // create mock for TransformerFactory object
        final TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        when(mockTransformerFactory.newTransformer(any(StreamSource.class))).thenThrow(
            new TransformerConfigurationException());

        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenReturn(mockTransformerFactory);

        final XsltBasedCodeGenerator xsltBasedCodeGenerator = mock(XsltBasedCodeGenerator.class,
            Mockito.CALLS_REAL_METHODS);
        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        Assert.assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        verify(fileMock, times(1)).getAbsolutePath();

        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verifyNew(StreamSource.class).withArguments(any());
        // verifyPrivate(xsltBasedCodeGenerator, times(0)).invoke("createOutputFile", fileMock); // ?

        verifyNoMoreInteractions();
    }

    @Test
    @PrepareForTest(TransformerFactory.class)
    public void processFileWithTransformerException() throws Exception {
        // prepare fileMock object for testing
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        final Transformer mockTransformer = mock(Transformer.class, Mockito.CALLS_REAL_METHODS);
        doThrow(new TransformerException("")).when(mockTransformer).transform(any(StreamSource.class),
            any(StreamResult.class));

        // create mock for TransformerFactory object
        final TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        when(mockTransformerFactory.newTransformer(any(StreamSource.class))).thenReturn(mockTransformer);

        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenReturn(mockTransformerFactory);

        final XsltBasedCodeGenerator xsltBasedCodeGenerator = mock(XsltBasedCodeGenerator.class,
            Mockito.CALLS_REAL_METHODS);
        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);
        whenNew(StreamResult.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        Assert.assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        verify(fileMock, times(1)).getAbsolutePath();

        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verifyNew(StreamSource.class, times(2)).withArguments(any());
        verifyNew(StreamResult.class, times(1)).withArguments(any());

        verifyNoMoreInteractions();
    }
}
