/**
 * 
 */
package handler.impl;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 12 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlFileHandler.class)
@SuppressStaticInitializationFor({ "handler.impl.FileHandler",
        "handler.impl.XmlFileHandler", "org.slf4j.LoggerFactory" })
public class XmlFileHandlerTest {

    protected Logger         loggerMock;

    protected XmlFileHandler xmlFileHandler = Mockito.mock(
                                                    XmlFileHandler.class,
                                                    Mockito.CALLS_REAL_METHODS);

    @Mock
    protected File           fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void processFileWithFactoryConfigurationError() {
        // create mock for TransformerFactory object
        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenThrow(
                new FactoryConfigurationError());

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(0)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void processFileWithParserConfigurationException()
            throws ParserConfigurationException {
        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenThrow(
                new ParserConfigurationException());

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(
                documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        // verify(fileMock, times(0)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void processFileWithIOException() throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenThrow(new IOException());

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(
                documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(
                documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void processFileWithSAXException() throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenThrow(new SAXException());

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(
                documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(
                documentBuilderFactoryMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(0)).processXml(any(Document.class));
        verify(fileMock, times(1)).getAbsolutePath();

        verifyNoMoreInteractions();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void processFile() throws SAXException, IOException,
            ParserConfigurationException {
        Element elementMock = mock(Element.class);
        doNothing().when(elementMock).normalize();

        Document documentMock = mock(Document.class);
        when(documentMock.getDocumentElement()).thenReturn(elementMock);

        DocumentBuilder documentBuilderMock = mock(DocumentBuilder.class);
        when(documentBuilderMock.parse(fileMock)).thenReturn(documentMock);

        // create mock for TransformerFactory object
        DocumentBuilderFactory documentBuilderFactoryMock = mock(DocumentBuilderFactory.class);
        when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(
                documentBuilderMock);

        mockStatic(DocumentBuilderFactory.class);
        when(DocumentBuilderFactory.newInstance()).thenReturn(
                documentBuilderFactoryMock);

        doReturn(false).when(xmlFileHandler).processXml(documentMock);

        // do test
        assertFalse(xmlFileHandler.processFile(fileMock));

        // expectation specification
        verify(xmlFileHandler, times(1)).processXml(documentMock);
        verify(fileMock, times(0)).getAbsolutePath();

        verifyNoMoreInteractions();
    }
}
