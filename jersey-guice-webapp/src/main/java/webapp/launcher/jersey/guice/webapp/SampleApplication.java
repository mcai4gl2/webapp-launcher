package webapp.launcher.jersey.guice.webapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class SampleApplication extends GuiceServletContextListener {
    public static Injector injector;

    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(EchoFunction.class).to(EchoFunctionImpl.class);
            }
        });

        return injector;
    }
}
