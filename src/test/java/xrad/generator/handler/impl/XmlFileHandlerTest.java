/**
 *
 */
package xrad.generator.handler.impl;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({XmlFileHandler.class, DocumentBuilderFactory.class})
@SuppressStaticInitializationFor({"xrad.generator.handler.impl.FileHandler", "org.slf4j.LoggerFactory"})
public class XmlFileHandlerTest {

    @Mock
    protected Logger loggerMock;

    @Mock
    protected File fileMock;

    protected XmlFileHandler xmlFileHandler;

    @Before
    public void setUp() {
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        //MockitoAnnotations.initMocks(this);
        xmlFileHandler = Mockito.mock(XmlFileHandler.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void processFileWithFactoryConfigurationError() {
        // create mock for TransformerFactory object
        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenThrow(new FactoryConfigurationError());

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(0)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithParserConfigurationException() throws ParserConfigurationException {
        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenThrow(new ParserConfigurationException());

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithIOException() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenThrow(new IOException());

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithSAXException() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenThrow(new SAXException());

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    public void processFileWithIllegalArgumentException() throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenThrow(new IllegalArgumentException());

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));

        verifyNoMoreInteractions();
    }

    @Test
    public void processFile() throws SAXException, IOException, ParserConfigurationException {
        Element elementMock = mock(Element.class);
        doNothing().when(elementMock).normalize();

        Document documentMock = mock(Document.class);
        when(documentMock.getDocumentElement()).thenReturn(elementMock);

        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenReturn(documentMock);

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactoryMock);

        doReturn(false).when(xmlFileHandler).processXml(documentMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(1)).processXml(documentMock);
        verify(fileMock, times(0)).getAbsolutePath();

        verifyNoMoreInteractions();
    }
}
