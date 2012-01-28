/**
 * 
 */
package handler.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

/**
 * @author denis
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PhpObjectGenerator.class)
@SuppressStaticInitializationFor({ "handler.impl.FileHandler", "org.slf4j.LoggerFactory" })
public class PhpObjectGeneratorTest {

    @Mock
    protected Logger loggerMock;

    @Mock
    protected File   fileMock;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() {
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for {@link handler.impl.PhpObjectGenerator#buildFileName(java.lang.String)}.
     */
    @Test
    public final void testBuildFileNameWithNullResult() {
        PhpObjectGenerator phpObjectGenerator = new PhpObjectGenerator();
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName(null));
    }

    @Test
    public final void testBuildFileNameWithNullEmpty() {
        PhpObjectGenerator phpObjectGenerator = new PhpObjectGenerator();
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName(""));
    }

    @Test
    public final void testBuildFileNameWithIncorrectResult() {
        PhpObjectGenerator phpObjectGenerator = new PhpObjectGenerator();
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName("test abc"));
    }

    @Test
    public final void testBuildFileNameWithCorrectClassName() {
        PhpObjectGenerator phpObjectGenerator = new PhpObjectGenerator();
        // do test
        assertEquals("abc.php", phpObjectGenerator.buildFileName("   class abc {"));
    }

    @Test
    public final void testBuildFileNameWithCorrectInterfaceName() {
        PhpObjectGenerator phpObjectGenerator = new PhpObjectGenerator();
        // do test
        assertEquals("Iabc.php", phpObjectGenerator.buildFileName("   interface Iabc {"));
    }

}
