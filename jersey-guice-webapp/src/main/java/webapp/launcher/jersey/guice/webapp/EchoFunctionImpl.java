package webapp.launcher.jersey.guice.webapp;

public class EchoFunctionImpl implements EchoFunction {
    @Override
    public String echo(String input) {
        return input.toUpperCase();
    }
}
