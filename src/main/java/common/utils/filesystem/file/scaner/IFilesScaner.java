/**
 *
 */
package common.utils.filesystem.file.scaner;

import java.io.File;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
public interface IFilesScaner {
    public abstract void setSourceDirectory(File file);

    public abstract File getSourceDirectory();

    public abstract boolean execute();
}
