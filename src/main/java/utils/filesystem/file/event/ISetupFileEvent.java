/**
 * 
 */
package utils.filesystem.file.event;

import java.io.File;

import utils.filesystem.file.listener.ISetupFileEventListener;

/**
 * @author denis
 * 
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
