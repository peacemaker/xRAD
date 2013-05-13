package generator.utils.filesystem.file.scaner.impl;

import common.event.IObserver;
import common.event.ISubject;
import common.utils.filesystem.file.scaner.IFilesScaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
public class FilesScaner implements IFilesScaner, ISubject<File> {

    static Logger logger = LoggerFactory.getLogger(FilesScaner.class);

    protected File sourceDirectory;

    protected List<IObserver<File>> observers;

    public FilesScaner() {
        observers = new ArrayList<IObserver<File>>();
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
        return processItem(sourceDirectory);
    }

    protected boolean processItem(final File item) {
        if (!item.exists()) {
            logger.error(" FAIL (directory.exists()) : {}", item.getAbsolutePath());

            return false;
        }

        if (!item.canRead()) {
            logger.error(" FAIL (directory.canRead() : {}", item.getAbsolutePath());

            return false;
        }

        boolean result = true;

        if (item.isDirectory()) {

            logger.debug("Handle directory : {}", item.getAbsolutePath());

            final File[] subItems = item.listFiles();

            if (subItems == null) {
                return true;
            }

            for (final File subItem : subItems) {
                result = processItem(subItem) && result;
            }

            return result;
        }

        logger.debug("Handle file : {}", item.getAbsolutePath());

        notifyObservers(item);

        return true;
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
