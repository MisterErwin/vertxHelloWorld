package eu.luepg.vertx.server;

import com.google.inject.Inject;
import eu.luepg.vertx.util.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * The Controller (which creates a router) for the Hello World
 * <p>
 * We send an event on the EventBus and return its reply or failure
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
public class HelloWorldController {
    private final Vertx vertx;

    private Router router;

    @Inject
    public HelloWorldController(Vertx vertx) {
        this.vertx = vertx;
    }

    public Router createRouter() {
        if (this.router == null) {
            this.router = Router.router(vertx);
            this.router.get().handler(this::handleGreeting);
        }
        return this.router;
    }

    private void handleGreeting(RoutingContext context) {
        // Send an event
        vertx.eventBus().send(Const.EVENT_ADDRESS, null, event -> handleReply(context, event));
    }

    private void handleReply(RoutingContext context, AsyncResult<Message<Object>> event) {
        if (event.succeeded()) {
            // Reply
            context.response().end(event.result().body().toString());
        } else {
            // Return the failure
            context.fail(event.cause());
        }
    }

}
