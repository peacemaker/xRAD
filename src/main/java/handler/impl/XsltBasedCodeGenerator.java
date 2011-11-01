/**
 *
 */
package handler.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 24 июня 2011
 */
public abstract class XsltBasedCodeGenerator extends CodeGenerator {

    /*
     * (non-Javadoc)
     * @see handler.impl.FileHandler#processFile(java.io.File)
     */
    @Override
    protected boolean processFile(final File file) {
        if (file == null) {
            logger.debug("FAIL (file == null)");

            return false;
        }

        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance(); // TransformerFactoryConfigurationError
            final Transformer transformer = transformerFactory.newTransformer(new StreamSource(template)); // TransformerConfigurationException
            final StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(file), new StreamResult(writer));

            final String result = writer.toString();

            final String outputFileName = buildFullFileName(result);

            if (outputFileName == null) {
                // logger.debug("Can not get file name from {} : ", result);

                return false;
            }

            final BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));// IOException
            out.write(result);// IOException
            out.close();// IOException

            return true;
        } catch (final TransformerFactoryConfigurationError e) {
            logger.error("The implementation is not available or cannot be instantiated");
            logger.error(e.fillInStackTrace().toString());
        } catch (final TransformerConfigurationException e) {
            logger.error("there are errors when parsing the Source or it is not possible to create a Transformer instance");
            logger.error(e.fillInStackTrace().toString());
        } catch (final TransformerException e) {
            logger.error("There are errors when parsing the Source or it is not possible to create a Transformer instance");
            logger.error(e.fillInStackTrace().toString());
        } catch (final IOException e) {
            logger.error("Can not write to file");
            logger.error(e.fillInStackTrace().toString());
        }

        return false;
    }

    protected String buildFullFileName(String result) {
        final String fileName = buildFileName(result);
        if (fileName == null) {
            // logger.debug("Can not get file name from {} : ", result);

            return null;
        }

        return getDestinationDirectory().getAbsolutePath() + File.separator + fileName;
    }

    protected abstract String buildFileName(String result);

}
