package utils.filesystem.file.scaner.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utils.filesystem.file.event.ISetupFileEvent;
import utils.filesystem.file.listener.ISetupFileEventListener;
import utils.filesystem.file.scaner.IFilesScaner;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
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
        logger.info(String.format("set Source : %1$s", sourceDirectory.getAbsolutePath()));
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
        logger.info("-----");
        logger.info(String.format("Start execute for file : %1$s", sourceDirectory.getAbsolutePath()));
        if (!sourceDirectory.exists()) {
            logger.error(String.format("Execute (sourceDirectory.exists()) FAIL for directory : %1$s",
                    sourceDirectory.getAbsolutePath()));

            return false;
        }

        if (!sourceDirectory.isDirectory()) {
            logger.error(String.format("Execute (sourceDirectory.isDirectory()) FAIL for directory : %1$s",
                    sourceDirectory.getAbsolutePath()));

            return false;
        }

        if (!sourceDirectory.canRead()) {
            logger.error(String.format("Execute (sourceDirectory.canRead()) FAIL for directory : %1$s",
                    sourceDirectory.getAbsolutePath()));

            return false;
        }

        if (!parseDirectory(sourceDirectory)) {
            logger.error(String.format("Parse FAIL for directory : %1$s", sourceDirectory.getAbsolutePath()));

            return false;
        }

        return true;
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
                logger.info(String.format("File : %1$s - error", item.getAbsolutePath()));
                result = false;

                continue;
            }

            logger.info(String.format("File : %1$s - ok", item.getAbsolutePath()));
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
