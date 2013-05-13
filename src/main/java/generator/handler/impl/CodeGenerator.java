/**
 *
 */
package generator.handler.impl;

import java.io.File;

/**
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @since 23 июня 2011
 */
public abstract class CodeGenerator extends FileHandler {

    protected File destinationDirectory;

    protected File template;

    /**
     * @return the destinationDirectory
     */
    public File getDestinationDirectory() {
        return destinationDirectory;
    }

    /**
     * @param destinationDirectory the destinationDirectory to set
     */
    public void setDestinationDirectory(final File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    /**
     * @return the template
     */
    public File getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(final File template) {
        this.template = template;
    }

}
