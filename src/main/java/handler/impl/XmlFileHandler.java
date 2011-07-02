/**
 * 
 */
package handler.impl;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
abstract public class XmlFileHandler extends FileHandler {

    private static Logger logger = Logger.getLogger(XmlFileHandler.class);

    protected Document    xml;

    @Override
    protected String getDefaultFileExtension() {
        return "xml";
    }

    @Override
    protected boolean processFile() {
        if (!convertFileToXml()) {
            return false;
        }

        return processXmlFile();
    }

    protected boolean convertFileToXml() {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            logger.error(e.fillInStackTrace());

            return false;
        }
        Document xml = null;
        try {
            xml = db.parse(file);
        } catch (final SAXException e) {
            logger.error(e.fillInStackTrace());

            return false;
        } catch (final IOException e) {
            logger.error(e.fillInStackTrace());

            return false;
        }
        xml.getDocumentElement().normalize();
        logger.info(String.format("convertFileToXml OK for file : %1$s", file.getAbsolutePath()));

        return true;
    }

    abstract protected boolean processXmlFile();
}