/**
 * 
 */
package utils.filesystem.file.listener;

import java.io.File;

/**
 * 
 * 
 * @author    Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since     9 июня 2011
 */
public interface ISetupFileEventListener {
    public boolean update(File file);
}