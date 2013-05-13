/**
 *
 */
package generator.handler.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author denis
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PhpObjectGenerator.class)
@SuppressStaticInitializationFor({"generator.handler.impl.FileHandler", "org.slf4j.LoggerFactory"})
public class PhpObjectGeneratorTest {

    @Mock
    protected Logger loggerMock;

    @Mock
    protected File fileMock;

    protected PhpObjectGenerator phpObjectGenerator;

    @Before
    public void setUp() {
        Whitebox.setInternalState(FileHandler.class, loggerMock);
        //MockitoAnnotations.initMocks(this);

        phpObjectGenerator = new PhpObjectGenerator();
    }

    /**
     * Test method for {@link generator.handler.impl.PhpObjectGenerator#buildFileName(java.lang.String)}.
     */
    @Test
    public final void testBuildFileNameWithNullResult() {
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName(null));
    }

    @Test
    public final void testBuildFileNameWithNullEmpty() {
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName(""));
    }

    @Test
    public final void testBuildFileNameWithIncorrectResult() {
        // do test
        assertEquals(null, phpObjectGenerator.buildFileName("test abc"));
    }

    @Test
    public final void testBuildFileNameWithCorrectClassName() {
        // do test
        assertEquals("abc.php", phpObjectGenerator.buildFileName("   class abc {"));
    }

    @Test
    public final void testBuildFileNameWithCorrectInterfaceName() {
        // do test
        assertEquals("Iabc.php", phpObjectGenerator.buildFileName("   interface Iabc {"));
    }

}
