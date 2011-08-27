/**
 * 
 */
package handler.impl;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import event.IObserver;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
abstract public class FileHandler implements IObserver<File> {

    static Logger     logger = LoggerFactory.getLogger(FileHandler.class);

    protected Pattern fileNamePattern;

    public Pattern getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(final Pattern fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public void update(File eventData) {
        FileHandler.logger.info("--------");
        FileHandler.logger.info(String.format("Start update for file : %1$s", eventData.getAbsolutePath()));
        if (!processFileValidation(eventData)) {
            FileHandler.logger.error(String.format("Validation FAIL for file : %1$s", eventData.getAbsolutePath())); // ???

            return;
        }

        if (!processFile(eventData)) {
            FileHandler.logger.error(String.format("Process FAIL for file : %1$s", eventData.getAbsolutePath()));

            return;
        }

        return;
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
