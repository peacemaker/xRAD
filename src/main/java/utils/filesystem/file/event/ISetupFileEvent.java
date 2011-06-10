/**
 * 
 */
package utils.filesystem.file.event;

import java.io.File;

import utils.filesystem.file.listener.ISetupFileEventListener;

/**
 * 
 * 
 * @author    Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since     9 июня 2011
 */
public interface ISetupFileEvent {
    /**
     * 
     * @param listener
     */
    public void attach(ISetupFileEventListener listener);

    /**
     * 
     * @param listener
     */
    // public void detach(ISetupFileEventListener listener);

    /**
     * 
     * @return
     */
    public boolean notifyListener(File file);
}
