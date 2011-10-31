/**
 * 
 */
package handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author denis
 * 
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"handler.impl.FileHandler", "org.slf4j.LoggerFactory"})
public class XmlRootElementAnalyzerTest {

    protected Set<String>                roots                  = null;

    private final XmlRootElementAnalyzer xmlRootElementAnalyzer = new XmlRootElementAnalyzer();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        roots = new TreeSet<String>();
        roots.add("root_a");
        roots.add("root_b");
        roots.add("root_c");
        roots.add("root_c");
    }

    /**
     * Test method for {@link handler.impl.XmlRootElementAnalyzer#getRoots()}.
     */
    @Test
    public void testGetRoots() {
        XmlRootElementAnalyzer xmlRootElementAnalyzer = Mockito.mock(XmlRootElementAnalyzer.class,
                Mockito.CALLS_REAL_METHODS);

        xmlRootElementAnalyzer.roots = roots;

        assertEquals(xmlRootElementAnalyzer.getRoots(), roots);
    }

    /**
     * Test method for {@link handler.impl.XmlRootElementAnalyzer#processXml(org.w3c.dom.Document)}.
     */
    @Test
    public void testProcessXml() {
        Element elementMock = mock(Element.class);
        when(elementMock.getNodeName()).thenReturn("root_a").thenReturn("root_b").thenReturn("root_c")
                .thenReturn("root_c");

        Document documentMock = mock(Document.class);
        when(documentMock.getDocumentElement()).thenReturn(elementMock);

        // do test
        assertTrue(xmlRootElementAnalyzer.processXml(documentMock));
        assertTrue(xmlRootElementAnalyzer.processXml(documentMock));
        assertTrue(xmlRootElementAnalyzer.processXml(documentMock));
        assertTrue(xmlRootElementAnalyzer.processXml(documentMock));
        assertEquals(xmlRootElementAnalyzer.roots, roots);

        // expectation specification
        verifyNoMoreInteractions();
    }
}
