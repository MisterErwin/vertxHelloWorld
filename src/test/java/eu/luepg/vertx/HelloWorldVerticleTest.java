package eu.luepg.vertx;

import eu.luepg.vertx.util.Const;
import eu.luepg.vertx.util.DeployHelper;
import eu.luepg.vertx.verticle.HelloWorldVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if the HelloWorldVerticle consumes the event on the eventbus and returns the correct value
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
@RunWith(VertxUnitRunner.class)
public class HelloWorldVerticleTest {
    private Vertx vertx;

    @Before
    public void setUp(TestContext testContext) {
        vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
                .setWorker(true)
                .setWorkerPoolSize(1);
        DeployHelper.deploy(vertx, HelloWorldVerticle.class, options).setHandler(testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }


    @Test
    public void testEventBus(TestContext context) {
        vertx.eventBus().send(Const.EVENT_ADDRESS, null, event -> {
            context.assertTrue(event.succeeded());
            context.assertEquals("{\"value\":\"Hello World\"}", event.result().body().toString());
        });
    }
}
