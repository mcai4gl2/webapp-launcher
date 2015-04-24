package webapp.launcher.jersey.spring.webapp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("test")
public class EchoService {
    @Inject
    private EchoFunction echoFunction;

    @GET
    @Path("{input}")
    public String echo(@PathParam("input") String input) {
        return echoFunction.echo(input);
    }
}
