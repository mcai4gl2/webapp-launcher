package webapp.launcher.tomcat;

import com.google.common.base.Joiner;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatEmbeddedLauncher {
    public static final Logger LOGGER = LoggerFactory.getLogger(TomcatEmbeddedLauncher.class);

    public static final String URL_OPTION = "url";
    public static final String PATH_OPTION = "path";
    public static final String PORT_OPTION = "port";
    public static final String HTTPS_PORT_OPTION = "httpsPort";

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
    private boolean usesHttps;
    private int httpsPort;

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
        usesHttps = false;
    }

    public static void main(String[] args) throws Exception {
        TomcatEmbeddedLauncher launcher = new TomcatEmbeddedLauncher();

        CommandLineParser parser = new GnuParser();
        CommandLine commandLine = parser.parse(launcher.createOptions(), args);

        launcher.port = Integer.parseInt(commandLine.getOptionValue(PORT_OPTION));

        launcher.build();

        String[] urls = commandLine.getOptionValues(URL_OPTION);
        String[] paths = commandLine.getOptionValues(PATH_OPTION);

        launcher.usesHttps = commandLine.hasOption(HTTPS_PORT_OPTION);
        launcher.httpsPort = Integer.parseInt(commandLine.getOptionValue(HTTPS_PORT_OPTION));

        if (urls != null) {
            for (int index = 0; index < urls.length; index++) {
                launcher.addWebApplication(urls[index], paths[index]);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    LOGGER.info("Got SIGTERM, shutting down Tomcat server");
                    launcher.tomcat.stop();
                } catch (Exception ex) {
                    LOGGER.error("Error when shutting down Tomcat server", ex);
                }
            }
        });

        launcher.startAndWait();
    }

    public Options createOptions() {
        Options options = new Options();

        options.addOption(URL_OPTION, URL_OPTION, true, "Url mapping");
        options.addOption(PATH_OPTION, PATH_OPTION, true, "Web application path");
        options.addOption(PORT_OPTION, PORT_OPTION, true, "Port");
        options.addOption(HTTPS_PORT_OPTION, HTTPS_PORT_OPTION, true, "Https Port");

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
        if (usesHttps) {
            Connector httpsConnector = new Connector();
            httpsConnector.setPort(httpsPort);
            httpsConnector.setSecure(true);
            httpsConnector.setScheme("https");
            httpsConnector.setAttribute("keyAlias", System.getProperty("javax.net.ssl.keyAlias"));
            httpsConnector.setAttribute("keystorePass", System.getProperty("javax.net.ssl.keyStorePassword"));
            httpsConnector.setAttribute("keystoreFile", System.getProperty("javax.net.ssl.keyStore"));
            httpsConnector.setAttribute("truststorePass", System.getProperty("javax.net.ssl.trustStorePassword"));
            httpsConnector.setAttribute("truststoreFile", System.getProperty("javax.net.ssl.trustStore"));
            httpsConnector.setAttribute("clientAuth", System.getProperty("javax.net.ssl.clientAuth"));
            httpsConnector.setAttribute("sslProtocol", "TLS");
            httpsConnector.setAttribute("SSLEnabled", true);

            Service service = tomcat.getService();
            service.addConnector(httpsConnector);
        }

        tomcat.start();
        tomcat.getServer().await();
        return this;
    }
}
