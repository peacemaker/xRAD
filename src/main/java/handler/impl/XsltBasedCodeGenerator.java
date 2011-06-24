/**
 * 
 */
package handler.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 24 июня 2011
 */
public abstract class XsltBasedCodeGenerator extends CodeGenerator {

    private static Logger logger = Logger.getLogger(XsltBasedCodeGenerator.class);

    /*
     * (non-Javadoc)
     * 
     * @see handler.impl.FileHandler#processFile(java.io.File)
     */
    @Override
    protected boolean processFile(final File file) {
        logger.info(String.format("Start processFile for file : %1$s", file.getAbsolutePath()));
        // final File output = createOutputFile(file);
        // logger.info(String.format("Output file : %1$s", output.getAbsolutePath()));
        final TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            final Transformer transformer = tFactory.newTransformer(createSource(template));
            transformer.transform(createSource(file), createResult(file));

            // // transformer.transform(new StreamSource("howto.xml"), new
            // // StreamResult(new FileOutputStream("howto.html")));
        } catch (TransformerConfigurationException e) {
            // e.printStackTrace();

            return false;
        } catch (TransformerException e) {
            // e.printStackTrace();

            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return false;
        }

        // System.out.println("InterfaceRequestCodeGenerator.execute()");
        // System.out.println(file.getAbsolutePath());

        return true;
    }

    protected abstract String createOutputFile(File file);

    protected Source createSource(File file) {
        return new StreamSource(file);
    }

    protected Result createResult(File file) throws FileNotFoundException {
        return new StreamResult(new FileOutputStream(file));
    }

}
