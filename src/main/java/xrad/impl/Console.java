package xrad.impl;

import xrad.generator.utils.filesystem.file.scanner.impl.FilesScanner;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2013-05-13 15:21
 */
public class Console {

    @Argument(metaVar = "[target [target2 [target3] ...]]",
            usage = "targets")
    private List<String> targets = new ArrayList<String>();

    @Option(name = "-h", aliases = "-help",
            usage = "print this message")
    private boolean help = false;

    @Option(name = "-lib", metaVar = "<path>",
            usage = "specifies a path to search for jars and classes")
    private String lib;

    @Option(name = "-f", aliases = {"-file", "-buildfile"}, metaVar = "<file>",
            usage = "use given buildfile")
    private File buildFile;

    @Option(name = "-nice", metaVar = "number",
            usage = "A niceness value for the main thread:\n"
                    + "1 (lowest) to 10 (highest); 5 is the default")
    private int nice = 5;

    @Option(name = "-D", metaVar = "<property>=<value>",
            usage = "use value for given property",
            handler = MapOptionHandler.class) //MyMapOptionHandler.class
    private Map<String, String> properties = new HashMap<String, String>();

    public static void main(String[] args) throws CmdLineException {

        final String[] argv = {"test", "-D", "key = value", "-f", "build.xml",
                "-D", "key2 =' value2'", "clean", "install"};
        final Console options = new Console();
        final CmdLineParser parser = new CmdLineParser(options);
        parser.parseArgument(argv);

        // print usage
        parser.setUsageWidth(Integer.MAX_VALUE);
        parser.printUsage(System.err);

        System.out.println(options.targets);
        System.out.println(options.properties);

        ApplicationContext context = new ClassPathXmlApplicationContext("console.xml");
        FilesScanner filesScanner = (FilesScanner) context.getBean("filesScanner");

        filesScanner.execute();
        //obj.getMessage();

        // check the options have been set correctly
        //assertEquals("build.xml", options.buildFile.getName());
        //assertEquals(2, options.targets.size());
        //assertEquals(2, options.properties.size());

    }
}
