package http.client;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpClient {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.client.HttpClient httpClient = null;
        try  {
            SslContextFactory sslContextFactory = new SslContextFactory();
            httpClient = new org.eclipse.jetty.client.HttpClient(sslContextFactory);

            httpClient.start();

            ContentResponse response = httpClient.GET("https://simple-executor.herokuapp.com/echo");
            System.out.println(response.getContentAsString());
        } finally {
            if (httpClient != null) {
                httpClient.stop();
            }
        }
    }
}
