package webapp.launcher.jersey.spring.webapp;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Singleton
@Path("test")
public class EchoService {
    private EchoFunction echoFunction;

    @Inject
    public EchoService(EchoFunction echoFunction) {
        this.echoFunction = echoFunction;
    }

    @GET
    @Path("{input}")
    public String echo(@PathParam("input") String input) {
        return echoFunction.echo(input);
    }
}
