/**
 * 
 */
package handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 13 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlFileConcatenator.class)
@SuppressStaticInitializationFor({"handler.impl.FileHandler", "handler.impl.XmlFileHandler",
        "handler.impl.XmlFileConcatenator", "org.apache.log4j.LogManager"})
public class XmlFileConcatenatorTest {

    final String     filePath = "/path/to/file";

    protected Logger loggerMock;

    @Before
    public void setUp() {
        loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        Whitebox.setInternalState(XmlFileHandler.class, loggerMock);
        Whitebox.setInternalState(XmlFileConcatenator.class, loggerMock);
    }

    @Test
    public void processWrongTagNameFile() throws Exception {
        // create mock object for Element
        final Element element = PowerMock.createMock(Element.class);
        EasyMock.expect(element.getTagName()).andReturn("");

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, "getContentElement");
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(element);

        PowerMock.replayAll();

        assertFalse(xmlFileConcatenator.processXmlFile());
        assertEquals(0, xmlFileConcatenator.xmlStorage.size());

        PowerMock.verifyAll();
    }

    @Test
    public void processNotExistAttributeFile() throws Exception {
        // create mock object for Element
        final Element element = PowerMock.createMock(Element.class);
        EasyMock.expect(element.getTagName()).andReturn("node");
        EasyMock.expect(element.getAttribute("id")).andReturn("");

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, "getContentElement");
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(element);

        PowerMock.replayAll();

        assertFalse(xmlFileConcatenator.processXmlFile());
        assertEquals(0, xmlFileConcatenator.xmlStorage.size());

        PowerMock.verifyAll();
    }

    @Test
    public void processFile() throws Exception {
        final String key = "node";
        final String subKey = "class";
        // create mock object for Element
        final Element element = PowerMock.createMock(Element.class);
        EasyMock.expect(element.getTagName()).andReturn(key);
        EasyMock.expect(element.getAttribute("id")).andReturn(subKey);

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, "getContentElement");
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(element);

        PowerMock.replayAll();

        assertTrue(xmlFileConcatenator.processXmlFile());
        assertEquals(1, xmlFileConcatenator.xmlStorage.size());
        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(key));

        final HashMap<String, Document> temp = xmlFileConcatenator.xmlStorage.get(key);
        assertEquals(1, temp.size());
        assertTrue(temp.containsKey(subKey));
        assertEquals(null, temp.get(subKey));

        PowerMock.verifyAll();
    }

    /**
     * 
     * - xml -> <node id="class">
     * - xml -> <node id="class">
     * 
     * @throws Exception
     */
    @Test
    public void processTwoEqualsXmlFile() throws Exception {
        final String key = "node";
        final String subKey = "class";
        // create mock object for Element
        final Element elementOne = PowerMock.createMock(Element.class);
        EasyMock.expect(elementOne.getTagName()).andReturn(key);
        EasyMock.expect(elementOne.getAttribute("id")).andReturn(subKey);

        final Element elementTwo = PowerMock.createMock(Element.class);
        EasyMock.expect(elementTwo.getTagName()).andReturn(key);
        EasyMock.expect(elementTwo.getAttribute("id")).andReturn(subKey);

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, new String[] {"getContentElement"});
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementOne);
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementTwo);

        // create mock File object
        File fileMock = PowerMock.createMock(File.class);
        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        PowerMock.replayAll();

        xmlFileConcatenator.file = fileMock;

        assertTrue(xmlFileConcatenator.processXmlFile());
        assertFalse(xmlFileConcatenator.processXmlFile());
        assertEquals(1, xmlFileConcatenator.xmlStorage.size());
        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(key));

        final HashMap<String, Document> temp = xmlFileConcatenator.xmlStorage.get(key);
        assertEquals(1, temp.size());
        assertTrue(temp.containsKey(subKey));
        assertEquals(null, temp.get(subKey));

        PowerMock.verifyAll();
    }

    /**
     * 
     * - xml -> <nodeOne id="classOne">
     * - xml -> <nodeTwo id="classTwo">
     * 
     * @throws Exception
     */
    @Test
    public void processTwoXmlFile() throws Exception {
        // create mock object for Element
        final String keyOne = "nodeOne";
        final String subKeyOne = "classOne";
        final Element elementOne = PowerMock.createMock(Element.class);
        EasyMock.expect(elementOne.getTagName()).andReturn(keyOne);
        EasyMock.expect(elementOne.getAttribute("id")).andReturn(subKeyOne);

        // create mock object for Element
        final String keyTwo = "nodeTwo";
        final String subKeyTwo = "classTwo";
        final Element elementTwo = PowerMock.createMock(Element.class);
        EasyMock.expect(elementTwo.getTagName()).andReturn(keyTwo);
        EasyMock.expect(elementTwo.getAttribute("id")).andReturn(subKeyTwo);

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, new String[] {"getContentElement"});
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementOne);
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementTwo);

        PowerMock.replayAll();

        xmlFileConcatenator.xml = null;
        assertTrue(xmlFileConcatenator.processXmlFile());
        assertTrue(xmlFileConcatenator.processXmlFile());

        assertEquals(2, xmlFileConcatenator.xmlStorage.size());

        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(keyOne));
        final HashMap<String, Document> hashMapOne = xmlFileConcatenator.xmlStorage.get(keyOne);
        assertEquals(1, hashMapOne.size());
        assertTrue(hashMapOne.containsKey(subKeyOne));
        assertEquals(null, hashMapOne.get(subKeyOne));

        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(keyTwo));
        final HashMap<String, Document> hashMapTwo = xmlFileConcatenator.xmlStorage.get(keyTwo);
        assertEquals(1, hashMapTwo.size());
        assertTrue(hashMapTwo.containsKey(subKeyTwo));
        assertEquals(null, hashMapTwo.get(subKeyTwo));

        PowerMock.verifyAll();
    }

    /**
     * 
     * - xml -> <nodeOne id="classOne">
     * - xml -> <nodeTwo id="classTwo">
     * - xml -> <nodeOne id="classTwo">
     * 
     * @throws Exception
     */
    @Test
    public void processThreeXmlFile() throws Exception {
        // create mock object for Element
        final String keyOne = "nodeOne";
        final String subKeyOne = "classOne";
        final Element elementOne = PowerMock.createMock(Element.class);
        EasyMock.expect(elementOne.getTagName()).andReturn(keyOne);
        EasyMock.expect(elementOne.getAttribute("id")).andReturn(subKeyOne);

        // create mock object for Element
        final String keyTwo = "nodeTwo";
        final String subKeyTwo = "classTwo";
        final Element elementTwo = PowerMock.createMock(Element.class);
        EasyMock.expect(elementTwo.getTagName()).andReturn(keyTwo);
        EasyMock.expect(elementTwo.getAttribute("id")).andReturn(subKeyTwo);

        // create mock object for Element
        final Element elementThree = PowerMock.createMock(Element.class);
        EasyMock.expect(elementThree.getTagName()).andReturn(keyOne);
        EasyMock.expect(elementThree.getAttribute("id")).andReturn(subKeyTwo);

        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, new String[] {"getContentElement"});
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementOne);
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementTwo);
        PowerMock.expectPrivate(xmlFileConcatenator, "getContentElement").andReturn(elementThree);

        PowerMock.replayAll();

        xmlFileConcatenator.xml = null;
        assertTrue(xmlFileConcatenator.processXmlFile());
        assertTrue(xmlFileConcatenator.processXmlFile());
        assertTrue(xmlFileConcatenator.processXmlFile());

        assertEquals(2, xmlFileConcatenator.xmlStorage.size());

        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(keyOne));
        final HashMap<String, Document> hashMapOne = xmlFileConcatenator.xmlStorage.get(keyOne);
        assertEquals(2, hashMapOne.size());
        assertTrue(hashMapOne.containsKey(subKeyOne));
        assertEquals(null, hashMapOne.get(subKeyOne));
        assertTrue(hashMapOne.containsKey(subKeyTwo));
        assertEquals(null, hashMapOne.get(subKeyTwo));

        assertTrue(xmlFileConcatenator.xmlStorage.containsKey(keyTwo));
        final HashMap<String, Document> hashMapTwo = xmlFileConcatenator.xmlStorage.get(keyTwo);
        assertEquals(1, hashMapTwo.size());
        assertTrue(hashMapTwo.containsKey(subKeyTwo));
        assertEquals(null, hashMapTwo.get(subKeyTwo));

        PowerMock.verifyAll();
    }

    @Test
    public void emptyExecute() throws Exception {
        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, new String[] {"getContentElement"});

        PowerMock.replayAll();

        assertTrue(xmlFileConcatenator.execute());

        PowerMock.verifyAll();
    }

    // @Test
    public void oneXmlExecute() throws Exception {
        final XmlFileConcatenator xmlFileConcatenator = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                XmlFileConcatenator.class, new String[] {"getContentElement", "saveToFile"});

        PowerMock.expectPrivate(xmlFileConcatenator, "saveToFile").andReturn(true);

        PowerMock.replayAll();

        final String key = "node";
        final String subKey = "class";

        String content = "<node id=\"class\">\n  Hello World!\n</node>";
        InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);

        HashMap<String, Document> hashMap = new HashMap<String, Document>();
        hashMap.put(subKey, doc);
        xmlFileConcatenator.xmlStorage = new HashMap<String, HashMap<String, Document>>();
        xmlFileConcatenator.xmlStorage.put(key, hashMap);

        assertTrue(xmlFileConcatenator.execute());

        PowerMock.verifyAll();
    }
}
