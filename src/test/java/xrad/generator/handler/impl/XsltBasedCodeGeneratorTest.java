package xrad.generator.handler.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-09
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({XsltBasedCodeGenerator.class, TransformerFactory.class})
@SuppressStaticInitializationFor({"xrad.generator.handler.impl.FileHandler", "org.slf4j.LoggerFactory"})
public class XsltBasedCodeGeneratorTest {

    final String filePath = "/path/to/file";
    final String fileName = "test.txt";

    @Mock
    protected Logger loggerMock;

    @Mock
    protected File fileMock;

    protected XsltBasedCodeGenerator xsltBasedCodeGenerator;

    @Before
    public void setUp() {
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        //MockitoAnnotations.initMocks(this);
        xsltBasedCodeGenerator = mock(XsltBasedCodeGenerator.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void processFileWithNullParameter() throws TransformerConfigurationException {
        // behaviour specification
        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenReturn(null);

        // do test
        assertFalse(xsltBasedCodeGenerator.processFile(null));

        // expectation specification
        verifyStatic(times(0));
        TransformerFactory.newInstance();

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithTransformerFactoryConfigurationError() throws Exception {
        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenThrow(new TransformerFactoryConfigurationError());

        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        // do test
        assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        // expectation specification
        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithTransformerConfigurationException() throws Exception {
        // behaviour specification
        final TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        when(mockTransformerFactory.newTransformer(any(StreamSource.class))).thenThrow(
                new TransformerConfigurationException());
        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenReturn(mockTransformerFactory);

        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        // do test
        assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        // expectation specification
        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verify(mockTransformerFactory, times(1)).newTransformer(any(StreamSource.class));

        verifyNew(StreamSource.class).withArguments(any());

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithTransformerException() throws Exception {
        // behaviour specification
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

        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);
        whenNew(StreamResult.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        // do test
        assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        // expectation specification
        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verify(mockTransformerFactory, times(1)).newTransformer(any(StreamSource.class));

        verifyNew(StreamSource.class, times(2)).withArguments(any());
        verifyNew(StreamResult.class, times(1)).withArguments(any());

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithFileNameError() throws Exception {
        // behaviour specification
        final Transformer mockTransformer = mock(Transformer.class, Mockito.CALLS_REAL_METHODS);
        doNothing().when(mockTransformer).transform(any(StreamSource.class), any(StreamResult.class));

        // create mock for TransformerFactory object
        final TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        when(mockTransformerFactory.newTransformer(any(StreamSource.class))).thenReturn(mockTransformer);

        // mock static
        mockStatic(TransformerFactory.class);
        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));
        when(TransformerFactory.newInstance()).thenReturn(mockTransformerFactory);

        doReturn(null).when(xsltBasedCodeGenerator).buildFullFileName(any(String.class));

        whenNew(StreamSource.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);
        whenNew(StreamResult.class).withParameterTypes(File.class).withArguments(any()).thenReturn(null);

        // do test
        assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        // expectation specification
        verifyStatic(times(1));
        TransformerFactory.newInstance();

        verify(mockTransformerFactory, times(1)).newTransformer(any(StreamSource.class));

        verify(xsltBasedCodeGenerator, times(1)).buildFullFileName(any(String.class));

        verifyNew(StreamSource.class, times(2)).withArguments(any());
        verifyNew(StreamResult.class, times(1)).withArguments(any());

        verifyNoMoreInteractions();
    }

    @Test
    public void buildOutputFileNameError() {
        // behaviour specification
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        doReturn(null).when(xsltBasedCodeGenerator).buildFileName(null);

        xsltBasedCodeGenerator.destinationDirectory = fileMock;

        // do test
        assertEquals(xsltBasedCodeGenerator.buildFullFileName(null), null);

        // expectation specification
        verify(xsltBasedCodeGenerator, times(1)).buildFileName(null);

        verifyNoMoreInteractions();
    }

    @Test
    public void buildOutputFileName() {
        // behaviour specification
        when(fileMock.getAbsolutePath()).thenReturn(filePath);

        doReturn(fileName).when(xsltBasedCodeGenerator).buildFileName(null);

        xsltBasedCodeGenerator.destinationDirectory = fileMock;

        // do test
        assertEquals(xsltBasedCodeGenerator.buildFullFileName(null), filePath + File.separator + fileName);

        // expectation specification
        verify(xsltBasedCodeGenerator, times(1)).buildFileName(null);

        verifyNoMoreInteractions();
    }
}
