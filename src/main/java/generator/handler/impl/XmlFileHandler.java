/**
 *
 */
package generator.handler.impl;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
abstract public class XmlFileHandler extends FileHandler {

    @Override
    protected boolean processFile(final File file) {
        Document xml;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            xml = db.parse(file);
            xml.getDocumentElement().normalize();

            return processXml(xml);
        } catch (final FactoryConfigurationError e) {
            logger.error("The implementation is not available or cannot be instantiated");
            logger.error(e.fillInStackTrace().toString());
        } catch (final ParserConfigurationException e) {
            logger.error("A DocumentBuilder cannot be created which satisfies the configuration requested");
            logger.error(e.fillInStackTrace().toString());
        } catch (final IOException e) {
            logger.error("IO errors occur : {}", file.getAbsolutePath());
            logger.error(e.fillInStackTrace().toString());
        } catch (final SAXException e) {
            logger.error("Parse errors occur : {}", file.getAbsolutePath());
            logger.error(e.fillInStackTrace().toString());
        } catch (final IllegalArgumentException e) {
            logger.error("File is null");
            logger.error(e.fillInStackTrace().toString());
        }

        return false;
    }

    abstract protected boolean processXml(Document xml);
}