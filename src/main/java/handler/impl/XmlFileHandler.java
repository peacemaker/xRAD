/**
 * 
 */
package handler.impl;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import org.w3c.dom.Document;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
abstract public class XmlFileHandler extends FileHandler {

	@Override
	protected boolean processFile(File file) {
		Document xml = null;
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
			DocumentBuilder db = dbf.newDocumentBuilder();
			xml = db.parse(file);
			xml.getDocumentElement().normalize();
		} catch (final FactoryConfigurationError e) {
			logger.error("if the implementation is not available or cannot be instantiated");
			logger.error(e.fillInStackTrace().toString());

			return false;			
		} catch (final Exception e) {
			logger.error("XML not well formated : {}", file.getAbsolutePath());
			logger.error(e.fillInStackTrace().toString());

			return false;
		}

		return processXml(xml);
	}

	abstract protected boolean processXml(Document xml);
}