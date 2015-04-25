package webapp.launcher.jersey.spring.webapp;

import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("test")
public class EchoService {
    private EchoFunction echoFunction;
    private String prefix;

    @Inject
    public EchoService(EchoFunction echoFunction, @Named("test.input") String prefix) {
        this.echoFunction = echoFunction;
        this.prefix = prefix;
    }

    @GET
    @Path("{input}")
    public String echo(@PathParam("input") String input) {
        return echoFunction.echo(prefix + ":" + input);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
