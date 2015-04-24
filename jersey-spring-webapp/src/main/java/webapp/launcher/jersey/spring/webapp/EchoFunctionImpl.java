package webapp.launcher.jersey.spring.webapp;

public class EchoFunctionImpl implements EchoFunction {
    @Override
    public String echo(String input) {
        return input.toUpperCase();
    }
}
