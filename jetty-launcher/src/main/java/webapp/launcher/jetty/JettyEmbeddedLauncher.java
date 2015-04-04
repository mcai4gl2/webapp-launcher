package webapp.launcher.jetty;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyEmbeddedLauncher {
    public static final String URL_OPTION = "url";
    public static final String PATH_OPTION = "path";
    public static final String PORT_OPTION = "port";

    private int port;

    private Server server;
    private HandlerCollection handlers;

    public JettyEmbeddedLauncher() {
        port = 8080;
    }

    public Options createOptions() {
        Options options = new Options();

        options.addOption(URL_OPTION, URL_OPTION, true, "Url mapping");
        options.addOption(PATH_OPTION, PATH_OPTION, true, "Web application path");
        options.addOption(PORT_OPTION, PORT_OPTION, true, "Port");

        return options;
    }

    public JettyEmbeddedLauncher build() {
        server = new Server(port);
        handlers = new HandlerCollection();
        server.setHandler(handlers);
        return this;
    }

    public JettyEmbeddedLauncher addWebApplication(String url, String path) {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath(url);
        webAppContext.setWar(path);
        webAppContext.setExtractWAR(true);

        handlers.addHandler(webAppContext);
        return this;
    }

    public JettyEmbeddedLauncher start() throws Exception {
        server.start();
        return this;
    }

    public JettyEmbeddedLauncher startAndWait() throws Exception {
        server.start();
        server.join();
        return this;
    }

    // Based on http://www.eclipse.org/jetty/documentation/9.1.4.v20140401/embedded-examples.html#adding-embedded-examples
    public static void main(String[] args) throws Exception {
        JettyEmbeddedLauncher launcher = new JettyEmbeddedLauncher();

        CommandLineParser parser = new GnuParser();
        CommandLine commandLine = parser.parse(launcher.createOptions(), args);

        launcher.port = Integer.parseInt(commandLine.getOptionValue(PORT_OPTION));

        launcher.build();

        String[] urls = commandLine.getOptionValues(URL_OPTION);
        String[] paths = commandLine.getOptionValues(PATH_OPTION);

        if (urls != null) {
            for (int index = 0; index < urls.length; index++) {
                launcher.addWebApplication(urls[index], paths[index]);
            }
        }

        launcher.startAndWait();
    }
}
