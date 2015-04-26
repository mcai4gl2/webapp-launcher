package webapp.launcher.jersey.guice.webapp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

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

    @GET
    @Path("wait")
    public void wait(@Suspended final AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {

            }
            System.err.println("Just before async finished");
            asyncResponse.resume("Finished after 5000 ms");
        }).start();
        System.err.println("I finish first!");
    }
}
