package eu.luepg.vertx.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import eu.luepg.vertx.api.message.IHelloWorldMessageCreator;

/**
 * A GuiceBinder that causes failures
 *
 * @author elmexl
 * Created on 18.09.2018.
 */
public class TestGuiceBinderFailingHWMC extends AbstractModule {
    @Override
    protected void configure() {
        //Nothing here
    }

    @Provides
    @Singleton
    public IHelloWorldMessageCreator provideIHelloWorldMessageCreator() {
        return () -> {
            throw new RuntimeException("This exception should be handled");
        };
    }

}
