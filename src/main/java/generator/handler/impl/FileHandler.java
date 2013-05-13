/**
 *
 */
package generator.handler.impl;

import common.event.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-09
 */
abstract public class FileHandler implements IObserver<File> {

    static protected Logger logger = LoggerFactory.getLogger(FileHandler.class);

    protected Pattern fileNamePattern;

    public Pattern getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(final Pattern fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public void update(File eventData) {
        if (eventData == null) {
            logger.debug("FAIL (file == null)");

            return;
        }

        if (!processFileValidation(eventData) || !processFile(eventData)) {
            logger.info("skipped : {}", eventData.getAbsolutePath());

            return;
        }

        logger.info("handled : {}", eventData.getAbsolutePath());
    }

    protected boolean processFileValidation(final File file) {
        if (!file.exists()) {
            logger.debug("FAIL (file.exists() == false) : {}", file.getAbsolutePath());

            return false;
        }

        if (!file.isFile()) {
            logger.debug("FAIL (file.isFile() == false) : {}", file.getAbsolutePath());

            return false;
        }

        if (!file.canRead()) {
            logger.debug("FAIL (file.canRead() == false) : {}", file.getAbsolutePath());

            return false;
        }

        if (fileNamePattern == null) {
            logger.debug("FAIL (fileNamePattern == null)");

            return false;
        }

        if (!fileNamePattern.matcher(file.getName()).matches()) {
            logger.debug("FAIL (matcher.matches({})) : {}", fileNamePattern.pattern(), file.getAbsolutePath());

            return false;
        }

        return true;
    }

    abstract protected boolean processFile(final File file);

}
