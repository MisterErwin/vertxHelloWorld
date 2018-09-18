package eu.luepg.vertx.server;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

/**
 * Verticle for the server
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
public class ServerVerticle extends AbstractVerticle {
    private final static int PORT = 8181;

    @Inject
    private HelloWorldController helloWorldController;

    @Override
    public void start(Future<Void> startFuture) {
        // Use our hello World router
        Router router = helloWorldController.createRouter();

        // Set the content type
        router.route()
                .consumes("application/json")
                .produces("application/json");
        //ToDo: Add a failure handler and/or other routes (if required) here

        // Create a http server
        vertx.createHttpServer()
                .requestHandler(router::accept) // with the route handling
                .listen(PORT, event -> { //on port PORT
                    // And forward the success/failure
                    if (event.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(event.cause());
                    }
                });
    }
}
