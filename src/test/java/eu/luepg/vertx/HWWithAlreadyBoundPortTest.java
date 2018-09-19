package eu.luepg.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if the server fails correctly in case the port is already bound IHelloWorldMessageCreator
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
@RunWith(VertxUnitRunner.class)
public class HWWithAlreadyBoundPortTest {
    private Vertx vertxA;
    private Vertx vertxB;

    @Before
    public void setUp(TestContext testContext) {
        vertxA = Vertx.vertx();
        Main.start(vertxA).setHandler(testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertxA.close(context.asyncAssertSuccess());
        vertxB.close(context.asyncAssertSuccess());
    }


    @Test
    public void itShouldNotStartASecondServer(TestContext context) {
        vertxB = Vertx.vertx();
        Main.start(vertxB).setHandler(context.asyncAssertFailure());
    }

}
