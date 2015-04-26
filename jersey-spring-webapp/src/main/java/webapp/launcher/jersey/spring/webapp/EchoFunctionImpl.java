package webapp.launcher.jersey.spring.webapp;

public class EchoFunctionImpl implements EchoFunction {
    private String prefix;

    public EchoFunctionImpl(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String echo(String input) {
        return prefix + ":" + input.toUpperCase();
    }
}
