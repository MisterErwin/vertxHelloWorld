package eu.luepg.vertx;

import eu.luepg.vertx.server.ServerVerticle;
import eu.luepg.vertx.util.DeployHelper;
import eu.luepg.vertx.verticle.HelloWorldVerticle;
import io.vertx.core.*;

/**
 * @author elmexl
 * Created on 18.09.2018.
 */
public class Main {

    private Main() {
    }

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();

        start(vertx);
    }

    /**
     * Starts the hello world server
     *
     * @param vertx - Vertx instance to deploy on
     * @return Future that can be used to handle successful / failed deployments
     */
    static Future<Void> start(Vertx vertx) {
        return DeployHelper.deploy(vertx, ServiceLauncher.class, new DeploymentOptions());
    }

    /**
     * A Verticle that deploys our server verticles with the correct options
     */
    static class ServiceLauncher extends AbstractVerticle {
        @Override
        public void start(Future<Void> startFuture) {
            DeploymentOptions serverOptions = new DeploymentOptions()
                    .setWorkerPoolSize(3);  //Scale me up for more connection threads

            DeploymentOptions workerOpts = new DeploymentOptions()
                    .setWorker(true)
                    .setWorkerPoolSize(5);  //Scale me up for more Hello World worker

            CompositeFuture.all(
                    DeployHelper.deploy(vertx, ServerVerticle.class, serverOptions),
                    DeployHelper.deploy(vertx, HelloWorldVerticle.class, workerOpts)
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
