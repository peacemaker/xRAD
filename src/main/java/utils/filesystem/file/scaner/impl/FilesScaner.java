package utils.filesystem.file.scaner.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.filesystem.file.scaner.IFilesScaner;
import event.IObserver;
import event.ISubject;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
public class FilesScaner implements IFilesScaner, ISubject<File> {

    static Logger                   logger = LoggerFactory.getLogger(FilesScaner.class);

    protected File                  sourceDirectory;

    protected List<IObserver<File>> observers;

    public FilesScaner() {
        observers = new ArrayList<IObserver<File>>();
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
     * @see IDirectoriesParser#execute()
     */
    @Override
    public boolean execute() {
        logger.info("Directory : {} - start", sourceDirectory.getAbsolutePath());
        if (!sourceDirectory.exists()) {
            logger.error("The directory {} does not exist", sourceDirectory.getAbsolutePath());

            return false;
        }

        if (!sourceDirectory.isDirectory()) {
            logger.error("The directory {} is not a directory", sourceDirectory.getAbsolutePath());

            return false;
        }

        if (!sourceDirectory.canRead()) {
            logger.error("The directory {} can not be readed", sourceDirectory.getAbsolutePath());

            return false;
        }

        if (!parseDirectory(sourceDirectory)) {
            logger.error("Parse FAIL for directory : {}", sourceDirectory.getAbsolutePath());

            return false;
        }

        logger.info("end");

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

            logger.info("File : {}", item.getAbsolutePath());

            notifyObservers(item);
        }

        return result;
    }

    @Override
    public void attach(final IObserver<File> observer) {
        observers.add(observer);
    }

    @Override
    public void clear() {
        observers.clear();
    }

    @Override
    public void detach(final IObserver<File> observer) {
        observers.remove(observer);

    }

    @Override
    public void notifyObservers(final File eventData) {
        for (final IObserver<File> observer : observers) {
            observer.update(eventData);
        }
    }

    @Override
    public int size() {
        return observers.size();
    }

}
