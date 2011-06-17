/**
 * 
 */
package handler.impl;

import java.io.File;

import org.apache.log4j.Logger;

import utils.filesystem.file.listener.ISetupFileEventListener;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
abstract public class FileHandler implements ISetupFileEventListener {

    private static Logger logger = Logger.getLogger(FileHandler.class);

    protected File        destination;

    protected File        file;

    public void setDestination(final File destination) {
        this.destination = destination;
        logger.info(String.format("set destination : %1$s", this.destination.getAbsolutePath()));
    }

    public File getDestination() {
        return destination;
    }

    @Override
    public boolean update(final File file) {
        this.file = file;
        logger.info("--------");
        logger.info(String.format("Start update for file : %1$s", this.file.getAbsolutePath()));
        if (!processFileValidation()) {
            logger.error(String.format("Validation FAIL for file : %1$s", this.file.getAbsolutePath()));

            return false;
        }

        if (!processFile()) {
            logger.error(String.format("Process FAIL for file : %1$s", this.file.getAbsolutePath()));

            return false;
        }

        return true;
    }

    protected boolean processFileValidation() {
        if (!file.exists()) {
            logger.error(String.format("Validation (file.exists()) FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        if (!file.isFile()) {
            logger.error(String.format("Validation (file.isFile()) FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        if (!file.canRead()) {
            logger.error(String.format("Validation (file.canRead()) FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        if (getFileExtension() != getDefaultFileExtension()) {
            logger.info(String.format("Validation (file extension) FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        logger.info(String.format("Validation OK for file : %1$s", file.getAbsolutePath()));

        return true;
    }

    protected String getFileExtension() {
        final String fileName = file.getName();

        if (fileName == "") {
            return "";
        }

        final int lastDotPosition = fileName.lastIndexOf('.');
        if (lastDotPosition < 0) {
            return "";
        }

        return fileName.substring(lastDotPosition + 1, fileName.length());
    }

    abstract protected String getDefaultFileExtension();

    abstract protected boolean processFile();

}
