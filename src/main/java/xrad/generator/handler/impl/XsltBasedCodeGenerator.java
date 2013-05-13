/**
 *
 */
package xrad.generator.handler.impl;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-24
 */
public abstract class XsltBasedCodeGenerator extends CodeGenerator {

    /*
     * (non-Javadoc)
     * 
     * @see xrad.generator.handler.xrad.impl.FileHandler#processFile(java.io.File)
     */
    @Override
    protected boolean processFile(final File file) {
        if (file == null) {
            logger.debug("FAIL (file == null)");

            return false;
        }

        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(template));
            final StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(file), new StreamResult(writer));

            final String result = writer.toString();

            final String outputFileName = buildFullFileName(result);

            if (outputFileName == null) {
                return false;
            }

            logger.info("File name : {}", outputFileName);

            final BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));// IOException
            out.write(result);// IOException
            out.close();// IOException

            return true;
        } catch (final TransformerFactoryConfigurationError e) {
            logger.error("The implementation is not available or cannot be instantiated");
            logger.error(e.fillInStackTrace().toString());
        } catch (final TransformerConfigurationException e) {
            logger.error("There are errors when parsing the Source or it is not possible to create a Transformer instance");
            logger.error(e.fillInStackTrace().toString());
        } catch (final TransformerException e) {
            logger.error("If an unrecoverable error occurs during the course of the transformation");
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
            logger.debug("Can not get file name from {} : ", result);

            return null;
        }

        return getDestinationDirectory().getAbsolutePath() + File.separator + fileName;
    }

    protected abstract String buildFileName(String result);

}
