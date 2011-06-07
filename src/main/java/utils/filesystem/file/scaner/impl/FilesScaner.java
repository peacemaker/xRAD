package utils.filesystem.file.scaner.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utils.filesystem.file.event.ISetupFileEvent;
import utils.filesystem.file.listener.ISetupFileEventListener;
import utils.filesystem.file.scaner.IFilesScaner;

public class FilesScaner implements IFilesScaner, ISetupFileEvent {

    private static Logger                   logger = Logger.getLogger(FilesScaner.class);

    protected File                          sourceDirectory;

    protected List<ISetupFileEventListener> listeners;

    public FilesScaner() {
        listeners = new ArrayList<ISetupFileEventListener>();
    }

    /*
     * (non-Javadoc)
     * @see IDirectoriesParser#setSourceDirectoryPath(java.lang.String)
     */
    @Override
    public void setSourceDirectory(final File file) {
        sourceDirectory = file;
    }

    @Override
    public File getSourceDirectory() {
        return sourceDirectory;
    }

    /*
     * (non-Javadoc)
     * @see IDirectoriesParser#execute()
     */
    @Override
    public boolean execute() {
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            logger.error("Is not a directory : " + sourceDirectory.getAbsolutePath());

            return false;
        }

        // if (!sourceDirectory.canRead()) {
        // logger.error("Have no permission (read) for : " +
        // sourceDirectory.getAbsolutePath());
        //
        // return false;
        // }

        return parseDirectory(sourceDirectory);
    }

    /**
     * 
     * @param File
     *            directory
     * @return boolean
     */
    protected boolean parseDirectory(final File directory) {
        boolean result = true;
        final File[] items = directory.listFiles();

        for (final File item : items) {

            if (!item.exists()) {
                continue;
            }

            if (item.isDirectory()) {
                result = parseDirectory(item) && result;

                continue;
            }

            if (!notifyListener(item)) {
                logger.info("File : " + item.getAbsolutePath() + " - error");
                result = false;

                continue;
            }

            logger.info("File : " + item.getAbsolutePath() + " - ok");
        }

        return result;
    }

    @Override
    public void attach(final ISetupFileEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean notifyListener(final File file) {
        boolean result = true;

        for (final ISetupFileEventListener observer : listeners) {
            result = observer.update(file) && result;
        }

        return result;
    }

}
