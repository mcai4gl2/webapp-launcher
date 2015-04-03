package http.client;

import org.eclipse.jetty.client.api.ContentResponse;

public class HttpClient {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.client.HttpClient httpClient = null;
        try  {
            httpClient = new org.eclipse.jetty.client.HttpClient();

            httpClient.start();

            ContentResponse response = httpClient.GET("http://www.google.co.uk");
            System.out.println(response.getContentAsString());
        } finally {
            if (httpClient != null) {
                httpClient.stop();
            }
        }
    }
}
