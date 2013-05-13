/**
 *
 */
package xrad.generator.handler.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 */
public class PhpObjectGenerator extends XsltBasedCodeGenerator {

    final String EXTENSION = "php";

    /*
     * (non-Javadoc)
     * 
     * @see xrad.generator.handler.xrad.impl.XsltBasedCodeGenerator#buildFileName(java.lang.String)
     */
    @Override
    protected String buildFileName(String result) {
        if (result == null) {
            logger.debug("FAIL (result == null)");

            return null;
        }

        Pattern pattern = Pattern.compile("(interface|class)\\s([A-Za-z0-9_]{1,}).*\\{");

        final Matcher matcher = pattern.matcher(result);

        if (!matcher.find()) {
            logger.debug("Can not get file name from {} : ", result);

            return null;
        }

        return matcher.group(2) + "." + EXTENSION;
    }

}
