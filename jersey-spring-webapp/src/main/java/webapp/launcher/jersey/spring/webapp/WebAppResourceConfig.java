package webapp.launcher.jersey.spring.webapp;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.spring.bridge.api.SpringBridge;
import org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge;
import org.springframework.web.context.ContextLoaderListener;

import javax.inject.Inject;

public class WebAppResourceConfig extends ResourceConfig {
    @Inject
    public WebAppResourceConfig(ServiceLocator serviceLocator) {
        packages("webapp.launcher.jersey.spring.webapp");

        SpringBridge.getSpringBridge().initializeSpringBridge(serviceLocator);

        SpringIntoHK2Bridge springBridge = serviceLocator.getService(SpringIntoHK2Bridge.class);
        springBridge.bridgeSpringBeanFactory(
                ContextLoaderListener.getCurrentWebApplicationContext().getAutowireCapableBeanFactory());
    }
}
