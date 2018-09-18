package eu.luepg.vertx.verticle;

import com.google.inject.Inject;
import eu.luepg.vertx.api.message.IHelloWorldMessageCreator;
import eu.luepg.vertx.util.Const;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;

/**
 * Returns a JSON Object with the Hello World Message to the event caller if a IHelloWorldMessageCreator is present
 * Otherwise ignores events
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
public class HelloWorldVerticle extends AbstractVerticle {

    private IHelloWorldMessageCreator messageCreator;

    @Inject
    public HelloWorldVerticle(@Nullable IHelloWorldMessageCreator messageCreator) {
        this.messageCreator = messageCreator;
    }


    @Override
    public void start() {
        MessageConsumer<String> consumer = vertx.eventBus().consumer(Const.EVENT_ADDRESS);

        if (messageCreator != null) { //Only reply in case we have a messageCreator
            consumer.handler(event -> {
                JsonObject ret = new JsonObject();
                ret.put("value", messageCreator.getMessage());
                event.reply(ret.encode());
            });
        }
    }
}
