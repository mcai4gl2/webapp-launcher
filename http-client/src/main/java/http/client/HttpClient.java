package http.client;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpClient {
    public static void main(String[] args) throws Exception {
//        System.setProperty("javax.net.debug", "ssl");

        org.eclipse.jetty.client.HttpClient httpClient = null;
        try  {
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath("keys/client1.p12");
            sslContextFactory.setKeyStoreType("pkcs12");
            sslContextFactory.setKeyStorePassword("123456");
            sslContextFactory.setTrustStorePath("keys/clientkeystore");
            sslContextFactory.setTrustStorePassword("741152");
            sslContextFactory.setTrustStoreType("jks");
            httpClient = new org.eclipse.jetty.client.HttpClient(sslContextFactory);

            httpClient.start();

            ContentResponse response = httpClient.GET("https://localhost:8081/test/test/adfd");
            System.out.println(response.getContentAsString());
        } finally {
            if (httpClient != null) {
                httpClient.stop();
            }
        }
    }
}
