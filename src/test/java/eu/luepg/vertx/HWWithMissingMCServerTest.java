package eu.luepg.vertx;

import eu.luepg.vertx.guice.TestGuiceBinderMissingHWMC;
import eu.luepg.vertx.server.ServerVerticle;
import eu.luepg.vertx.util.DeployHelper;
import eu.luepg.vertx.verticle.HelloWorldVerticle;
import io.vertx.core.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if the {@link ServerVerticle} throws an error in case of a missing {@link eu.luepg.vertx.api.message.IHelloWorldMessageCreator}
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
@RunWith(VertxUnitRunner.class)
public class HWWithMissingMCServerTest {
    private Vertx vertx;

    @Before
    public void setUp(TestContext testContext) {
        vertx = Vertx.vertx();
        DeployHelper.deploy(vertx, TestServiceLauncher.class, new DeploymentOptions());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testEventBus(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient().getNow(8181, "localhost", "/", response -> {
            context.assertEquals(500, response.statusCode());
            response.bodyHandler(body -> {
                context.assertEquals("Internal Server Error", body.toString());
                async.complete();
            });
        });
    }

    /**
     * Modified launcher like in the main class
     */
    static class TestServiceLauncher extends AbstractVerticle {
        @Override
        public void start(Future<Void> startFuture) {
            DeploymentOptions serverOptions = new DeploymentOptions()
                    .setWorkerPoolSize(1);  //Scale me up for more connection threads

            DeploymentOptions workerOpts = new DeploymentOptions()
                    .setWorker(true)
                    .setWorkerPoolSize(1);  //Scale me up for more Hello World worker

            CompositeFuture.all(
                    DeployHelper.deploy(vertx, ServerVerticle.class, serverOptions, TestGuiceBinderMissingHWMC.class),
                    DeployHelper.deploy(vertx, HelloWorldVerticle.class, workerOpts, TestGuiceBinderMissingHWMC.class)
            ).setHandler(r -> {
                if (r.succeeded()) {
                    // Propagate the success
                    startFuture.complete();
                } else {
                    // Propagate the failure
                    startFuture.fail(r.cause());
                }
            });
        }
    }
}
