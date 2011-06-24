/**
 * 
 */
package handler.impl;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected Pattern     fileNamePattern;

    public Pattern getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(final Pattern fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public boolean update(final File file) {
        FileHandler.logger.info("--------");
        FileHandler.logger.info(String.format("Start update for file : %1$s", file.getAbsolutePath()));
        if (!processFileValidation(file)) {
            FileHandler.logger.error(String.format("Validation FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        if (!processFile(file)) {
            FileHandler.logger.error(String.format("Process FAIL for file : %1$s", file.getAbsolutePath()));

            return false;
        }

        return true;
    }

    protected boolean processFileValidation(final File file) {
        if (!file.exists()) {
            FileHandler.logger.error(String.format("Validation (file.exists()) FAIL for file : %1$s",
                file.getAbsolutePath()));

            return false;
        }

        if (!file.isFile()) {
            FileHandler.logger.error(String.format("Validation (file.isFile()) FAIL for file : %1$s",
                file.getAbsolutePath()));

            return false;
        }

        if (!file.canRead()) {
            FileHandler.logger.error(String.format("Validation (file.canRead()) FAIL for file : %1$s",
                file.getAbsolutePath()));

            return false;
        }

        final String filename = file.getName();

        final Matcher matcher = fileNamePattern.matcher(filename);
        if (!matcher.matches()) {
            FileHandler.logger.info(String.format("Validation (file name %2$s) FAIL for file : %1$s",
                file.getAbsolutePath(), fileNamePattern.pattern()));

            return false;
        }

        FileHandler.logger.info(String.format("Validation OK for file : %1$s", file.getAbsolutePath()));

        return true;
    }

    abstract protected boolean processFile(File file);

}
