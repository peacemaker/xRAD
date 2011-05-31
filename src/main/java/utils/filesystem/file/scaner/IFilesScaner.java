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
    public abstract void setSourceDirectoryPath(String sourceDirectoryPath);

    public abstract File getSourceDirectory();

    public abstract boolean execute();
}
