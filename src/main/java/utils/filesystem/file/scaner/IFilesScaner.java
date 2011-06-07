/**
 * 
 */
package utils.filesystem.file.scaner;

import java.io.File;

/**
 * @author denis
 *
 */
public interface IFilesScaner {
    public abstract void setSourceDirectory(File file);

    public abstract File getSourceDirectory();

    public abstract boolean execute();
}
