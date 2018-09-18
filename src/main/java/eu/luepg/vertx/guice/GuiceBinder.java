package eu.luepg.vertx.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import eu.luepg.vertx.api.message.IHelloWorldMessageCreator;
import eu.luepg.vertx.message.HelloWorldMessageCreatorImpl;
import eu.luepg.vertx.verticle.HelloWorldVerticle;

/**
 * @author elmexl
 * Created on 18.09.2018.
 */
public class GuiceBinder extends AbstractModule {
    @Override
    protected void configure() {
        //Nothing here
    }

    @Provides
    @Singleton
    public IHelloWorldMessageCreator provideIHelloWorldMessageCreator() {
        return new HelloWorldMessageCreatorImpl();
    }

    @Provides
    @Singleton
    public HelloWorldVerticle provideHelloWorldCreator(IHelloWorldMessageCreator messageCreator) {
        return new HelloWorldVerticle(messageCreator);
    }

}
