package eu.luepg.vertx.message;

import eu.luepg.vertx.api.message.IHelloWorldMessageCreator;

/**
 * @author elmexl
 * Created on 18.09.2018.
 */
public class HelloWorldMessageCreatorImpl implements IHelloWorldMessageCreator {
    @Override
    public String getMessage() {
        return "Hello World";
    }
}
