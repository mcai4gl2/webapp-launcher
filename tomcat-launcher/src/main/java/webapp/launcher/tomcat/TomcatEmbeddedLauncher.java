package webapp.launcher.tomcat;

import com.google.common.base.Joiner;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

public class TomcatEmbeddedLauncher {
    public static final String URL_OPTION = "url";
    public static final String PATH_OPTION = "path";
    public static final String PORT_OPTION = "port";

    private int port;
    private boolean autoDeploy;
    private boolean deployOnStartup;
    private boolean unpackWARs;
    private String accessLogDirectory;
    private String accessLogPrefix;
    private String accessLogSuffix;
    private String accessLogPattern;
    private boolean accessLogEnabled;
    private String shutdownCommand;
    private int shutdownPort;

    private Tomcat tomcat;

    public TomcatEmbeddedLauncher() {
        port = 8080;
        autoDeploy = true;
        deployOnStartup = true;
        unpackWARs = false;
        accessLogDirectory = Joiner.on("/").skipNulls().join(System.getProperty("catalina.home"), "logs");
        accessLogPrefix = "server_access_log";
        accessLogSuffix = ".txt";
        accessLogPattern = "%h %l %u %t \"%r\" %s %b";
        accessLogEnabled = true;
        shutdownCommand = "STOP";
        shutdownPort = 7001;
    }

    public static void main(String[] args) throws Exception {
        TomcatEmbeddedLauncher launcher = new TomcatEmbeddedLauncher();

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

    public Options createOptions() {
        Options options = new Options();

        options.addOption(URL_OPTION, URL_OPTION, true, "Url mapping");
        options.addOption(PATH_OPTION, PATH_OPTION, true, "Web application path");
        options.addOption(PORT_OPTION, PORT_OPTION, true, "Port");

        return options;
    }

    public TomcatEmbeddedLauncher build() {
        tomcat = new Tomcat();

        tomcat.setPort(port);

        StandardHost host = (StandardHost)tomcat.getHost();
        host.setAutoDeploy(true);
        host.setDeployOnStartup(true);
        host.setUnpackWARs(false);

        AccessLogValve value = new AccessLogValve();
        value.setDirectory(accessLogDirectory);
        value.setPrefix(accessLogPrefix);
        value.setSuffix(accessLogSuffix);
        value.setPattern(accessLogPattern);
        value.setEnabled(accessLogEnabled);
        host.addValve(value);

        tomcat.setHost(host);

        return this;
    }


    public TomcatEmbeddedLauncher addWebApplication(String url, String path) {
        tomcat.addWebapp(tomcat.getHost(), url, path);
        return this;
    }

    public TomcatEmbeddedLauncher start() throws Exception {
        tomcat.start();
        return this;
    }

    public TomcatEmbeddedLauncher startAndWait() throws Exception {
        tomcat.start();
        tomcat.getServer().await();
        return this;
    }
}
