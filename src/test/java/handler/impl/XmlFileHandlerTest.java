/**
 * 
 */
package handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.SAXException;

/**
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 12 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlFileHandler.class)
@SuppressStaticInitializationFor({"handler.impl.FileHandler", "handler.impl.XmlFileHandler",
        "org.apache.log4j.LogManager"})
public class XmlFileHandlerTest {

    protected Logger loggerMock;

    @Before
    public void setUp() {
        loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        Whitebox.setInternalState(XmlFileHandler.class, loggerMock);
    }

    /**
     * Test method for
     * 
     * {@link handler.impl.XmlFileHandler#getDefaultFileExtension()}.
     */
    @Test
    public void getDefaultFileExtension() {
        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class,
                new String[] {"processXmlFile"});

        PowerMock.replayAll();

        assertEquals("xml", xmlFileHandler.getDefaultFileExtension());

        PowerMock.verifyAll();
    }

    @Test
    public void processFileWrongConvertFileToXml() throws Exception {
        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, "convertFileToXml");
        PowerMock.expectPrivate(xmlFileHandler, "convertFileToXml").andReturn(false);

        PowerMock.replayAll();

        assertFalse(xmlFileHandler.processFile());

        PowerMock.verifyAll();
    }

    @Test
    public void processFileWrongProcessXmlFile() throws Exception {
        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, new String[] {
                "processXmlFile", "convertFileToXml"});
        PowerMock.expectPrivate(xmlFileHandler, "convertFileToXml").andReturn(true);
        PowerMock.expectPrivate(xmlFileHandler, "processXmlFile").andReturn(false);

        PowerMock.replayAll();

        assertFalse(xmlFileHandler.processFile());

        PowerMock.verifyAll();
    }

    @Test
    public void processFile() throws Exception {
        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, new String[] {
                "processXmlFile", "convertFileToXml"});
        PowerMock.expectPrivate(xmlFileHandler, "convertFileToXml").andReturn(true);
        PowerMock.expectPrivate(xmlFileHandler, "processXmlFile").andReturn(true);

        PowerMock.replayAll();

        assertTrue(xmlFileHandler.processFile());

        PowerMock.verifyAll();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void wrongNewDocumentBuilder() throws ParserConfigurationException {
        // mock static
        PowerMock.mockStatic(DocumentBuilderFactory.class);

        // Tell Powermock to not to invoke constructor
        PowerMock.suppress(PowerMock.constructor(DocumentBuilderFactory.class));

        // create mock for DocumentBuilderFactory
        DocumentBuilderFactory mockDocumentBuilderFactory = PowerMock.createMock(DocumentBuilderFactory.class);
        EasyMock.expect(mockDocumentBuilderFactory.newDocumentBuilder()).andThrow(new ParserConfigurationException());

        EasyMock.expect(DocumentBuilderFactory.newInstance()).andReturn(mockDocumentBuilderFactory).anyTimes();

        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, "processXmlFile");

        PowerMock.replayAll();

        assertFalse(xmlFileHandler.convertFileToXml());

        PowerMock.verifyAll();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void wrongParse() throws ParserConfigurationException, SAXException, IOException {
        // mock static
        PowerMock.mockStatic(DocumentBuilderFactory.class);

        // Tell Powermock to not to invoke constructor
        PowerMock.suppress(PowerMock.constructor(DocumentBuilderFactory.class));

        // create mock File object
        File fileMock = PowerMock.createMock(File.class);

        // create mock for DocumentBuilder
        DocumentBuilder mockDocumentBuilder = PowerMock.createMock(DocumentBuilder.class);
        EasyMock.expect(mockDocumentBuilder.parse(fileMock)).andThrow(new SAXException());

        // create mock for DocumentBuilderFactory
        DocumentBuilderFactory mockDocumentBuilderFactory = PowerMock.createMock(DocumentBuilderFactory.class);
        EasyMock.expect(mockDocumentBuilderFactory.newDocumentBuilder()).andReturn(mockDocumentBuilder);

        EasyMock.expect(DocumentBuilderFactory.newInstance()).andReturn(mockDocumentBuilderFactory).anyTimes();

        // loggerMock.error(EasyMock.anyObject());

        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, "processXmlFile");

        PowerMock.replayAll();

        xmlFileHandler.file = fileMock;
        assertFalse(xmlFileHandler.convertFileToXml());

        PowerMock.verifyAll();
    }

    @Test
    @PrepareForTest(DocumentBuilderFactory.class)
    public void wrongParse2() throws ParserConfigurationException, SAXException, IOException {
        // mock static
        PowerMock.mockStatic(DocumentBuilderFactory.class);

        // Tell Powermock to not to invoke constructor
        PowerMock.suppress(PowerMock.constructor(DocumentBuilderFactory.class));

        // create mock File object
        File fileMock = PowerMock.createMock(File.class);

        // create mock for DocumentBuilder
        DocumentBuilder mockDocumentBuilder = PowerMock.createMock(DocumentBuilder.class);
        EasyMock.expect(mockDocumentBuilder.parse(fileMock)).andThrow(new IOException());

        // create mock for DocumentBuilderFactory
        DocumentBuilderFactory mockDocumentBuilderFactory = PowerMock.createMock(DocumentBuilderFactory.class);
        EasyMock.expect(mockDocumentBuilderFactory.newDocumentBuilder()).andReturn(mockDocumentBuilder);

        EasyMock.expect(DocumentBuilderFactory.newInstance()).andReturn(mockDocumentBuilderFactory).anyTimes();

        // loggerMock.error(EasyMock.anyObject());

        XmlFileHandler xmlFileHandler = PowerMock.createPartialMock(XmlFileHandler.class, "processXmlFile");

        PowerMock.replayAll();

        xmlFileHandler.file = fileMock;
        assertFalse(xmlFileHandler.convertFileToXml());

        PowerMock.verifyAll();
    }
}
