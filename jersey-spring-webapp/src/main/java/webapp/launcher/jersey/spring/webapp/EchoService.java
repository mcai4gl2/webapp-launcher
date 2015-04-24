package webapp.launcher.jersey.spring.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component
@Path("test")
public class EchoService {
    @Autowired
    private EchoFunction echoFunction;

    @GET
    @Path("{input}")
    public String echo(@PathParam("input") String input) {
        return echoFunction.echo(input);
    }
}
