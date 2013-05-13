/**
 *
 */
package common.utils.filesystem.file.scanner;

import java.io.File;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-09
 */
public interface IFilesScanner {
    public abstract void setSourceDirectory(File file);

    public abstract File getSourceDirectory();

    public abstract boolean execute();
}
