package eu.luepg.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if the server is able to run and returns the correct Hello World
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
@RunWith(VertxUnitRunner.class)
public class HWServerTest {
    private Vertx vertx;

    @Before
    public void setUp(TestContext testContext) {
        vertx = Vertx.vertx();
        Main.start(vertx).setHandler(testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }


    @Test
    public void testHTTPServer(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient().getNow(8181, "localhost", "/", response -> {
            context.assertEquals(200, response.statusCode());
            response.bodyHandler(body -> {
                context.assertEquals("{\"value\":\"Hello World\"}", body.toString());
                async.complete();
            });
        });
    }
}
