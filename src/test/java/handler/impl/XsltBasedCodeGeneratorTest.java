package handler.impl;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 9 июня 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ XsltBasedCodeGenerator.class })
@SuppressStaticInitializationFor({ "org.apache.log4j.LogManager", "handler.impl.FileHandler",
        "handler.impl.CodeGenerator", "handler.impl.XsltBasedCodeGenerator" })
public class XsltBasedCodeGeneratorTest {

    final String     filePath = "/path/to/file";

    protected Logger loggerMock;
    protected File   fileMock;

    @Before
    public void setUp() {
        // create static mock Logger object
        loggerMock = PowerMock.createNiceMock(Logger.class);
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        Whitebox.setInternalState(CodeGenerator.class, loggerMock);
        Whitebox.setInternalState(XsltBasedCodeGenerator.class, loggerMock);
        // create mock File object
        fileMock = PowerMock.createMock(File.class);
    }

    @Test
    @PrepareForTest(TransformerFactory.class)
    public void processFileWithTransformerConfigurationException() throws Exception {
        // create mock File object
        File mockOutputFile = PowerMock.createMock(File.class);
        EasyMock.expect(mockOutputFile.getAbsolutePath()).andReturn("12345");

        // create mock Template File object
        File mockTemplateFile = PowerMock.createMock(File.class);

        // create mock Template StreamSource object
        StreamSource mockTemplateSource = PowerMock.createMock(StreamSource.class);

        // mock static
        PowerMock.mockStatic(TransformerFactory.class);

        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));

        // create mock for TransformerFactory object
        TransformerFactory mockTransformerFactory = PowerMock.createMock(TransformerFactory.class);
        EasyMock.expect(TransformerFactory.newInstance()).andReturn(mockTransformerFactory);
        EasyMock.expect(mockTransformerFactory.newTransformer(mockTemplateSource)).andThrow(
            new TransformerConfigurationException());

        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final XsltBasedCodeGenerator xsltBasedCodeGenerator = PowerMock.createPartialMock(XsltBasedCodeGenerator.class,
            "createOutputFile", "createSource");
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createSource", mockTemplateFile).andReturn(mockTemplateSource);
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createOutputFile", fileMock).andReturn(mockOutputFile);

        PowerMock.replayAll();

        xsltBasedCodeGenerator.template = mockTemplateFile;
        xsltBasedCodeGenerator.destinationDirectory = fileMock;
        Assert.assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        PowerMock.verifyAll();
    }

    @Test
    @PrepareForTest(TransformerFactory.class)
    public void processFileWithTransformerException() throws Exception {
        // create mock File object
        File mockOutputFile = PowerMock.createMock(File.class);
        EasyMock.expect(mockOutputFile.getAbsolutePath()).andReturn("12345");

        // mock static
        PowerMock.mockStatic(TransformerFactory.class);

        // Tell Powermock to not to invoke constructor
        MemberModifier.suppress(MemberMatcher.constructor(TransformerFactory.class));

        // create mock Template StreamSource object
        StreamSource mockTemplateSource = PowerMock.createMock(StreamSource.class);

        // create mock Source StreamSource object
        StreamSource mockFileSource = PowerMock.createMock(StreamSource.class);

        // create mock Result StreamSource object
        StreamResult mockFileResult = PowerMock.createMock(StreamResult.class);

        Transformer mockTransformer = PowerMock.createMock(Transformer.class);
        mockTransformer.transform(mockFileSource, mockFileResult);
        EasyMock.expectLastCall().andThrow(new TransformerException("123"));

        // create mock for TransformerFactory object
        TransformerFactory mockTransformerFactory = PowerMock.createMock(TransformerFactory.class);
        EasyMock.expect(TransformerFactory.newInstance()).andReturn(mockTransformerFactory);
        EasyMock.expect(mockTransformerFactory.newTransformer(mockTemplateSource)).andReturn(mockTransformer);

        // create mock for DestinationDirectory File object
        File mockDestinationDirectory = PowerMock.createMock(File.class);

        // create mock for Template File object
        File mockTemplateFile = PowerMock.createMock(File.class);

        // prepare fileMock object for testing
        EasyMock.expect(fileMock.getAbsolutePath()).andReturn(filePath);

        final XsltBasedCodeGenerator xsltBasedCodeGenerator = PowerMock.createPartialMock(XsltBasedCodeGenerator.class,
            "createOutputFile", "createSource", "createResult");
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createOutputFile", fileMock).andReturn(mockOutputFile);
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createSource", mockTemplateFile).andReturn(mockTemplateSource);
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createSource", fileMock).andReturn(mockFileSource);
        PowerMock.expectPrivate(xsltBasedCodeGenerator, "createResult", mockOutputFile).andReturn(mockFileResult);

        PowerMock.replayAll();

        xsltBasedCodeGenerator.destinationDirectory = mockDestinationDirectory;
        xsltBasedCodeGenerator.template = mockTemplateFile;
        Assert.assertFalse(xsltBasedCodeGenerator.processFile(fileMock));

        PowerMock.verifyAll();
    }
}
