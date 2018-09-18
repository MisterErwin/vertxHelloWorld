package eu.luepg.vertx.util;

import com.google.inject.AbstractModule;
import eu.luepg.vertx.guice.GuiceBinder;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author elmexl
 * Created on 18.09.2018.
 */
public class DeployHelper {

    private DeployHelper() {
    }

    /**
     * Deploy a vertx-guice verticle on a vertx instance with deployment options
     *
     * @param vertx    - Vertx instance to deploy on
     * @param verticle - Verticle class to deploy
     * @param opts     - Deployment options to use for deployment
     * @return - Future that can be used to handle successful / failed deployments
     */
    public static Future<Void> deploy(Vertx vertx, Class verticle, DeploymentOptions opts) {
        return deploy(vertx, verticle, opts, GuiceBinder.class);
    }

    /**
     * Deploy a vertx-guice verticle on a vertx instance with deployment options and a guice binder
     *
     * @param vertx    - Vertx instance to deploy on
     * @param verticle - Verticle class to deploy
     * @param opts     - Deployment options to use for deployment
     * @param guicer   - the Guice Binder class
     * @return - Future that can be used to handle successful / failed deployments
     */
    public static Future<Void> deploy(Vertx vertx, Class verticle, DeploymentOptions opts, Class<? extends AbstractModule> guicer) {
        Future<Void> done = Future.future();
        String deploymentName = "java-guice:" + verticle.getName();
        JsonObject config = new JsonObject()
                .put("guice_binder", guicer.getName());

        opts.setConfig(config);

        vertx.deployVerticle(deploymentName, opts, r -> {
            if (r.succeeded()) {
                System.out.println("Successfully deployed verticle: " + deploymentName);
                done.complete();
            } else {
                System.out.println("Failed to deploy verticle: " + deploymentName);
                done.fail(r.cause());
            }
        });

        return done;
    }
}