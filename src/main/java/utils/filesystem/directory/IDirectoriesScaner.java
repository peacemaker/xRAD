/**
 * 
 */
package utils.filesystem.directory;

import java.io.File;

/**
 * @author denis
 *
 */
public interface IDirectoriesScaner {
    public abstract void setSourceDirectoryPath(String sourceDirectoryPath);

    public abstract File getSourceDirectory();

    public abstract boolean execute();
}
