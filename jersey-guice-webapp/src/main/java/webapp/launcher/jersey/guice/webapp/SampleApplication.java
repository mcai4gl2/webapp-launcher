package webapp.launcher.jersey.guice.webapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SampleApplication extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(EchoFunction.class).to(EchoFunctionImpl.class);
                ResourceConfig rc = new PackagesResourceConfig("webapp.launcher.jersey.guice.webapp");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                }

                serve("/*").with(GuiceContainer.class);
            }
        });
    }
}