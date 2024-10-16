import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecureRestClient {

    public static void main(String[] args) throws IOException {

        // 1. Create a TrustManager that validates certificates, but logs exceptions
        TrustManager[] trustManagers = getTrustManagersWithLogging();

        // 2. Create an SSLSocketFactory with the TrustManager
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(trustManagers);

        // 3. Create an OkHttpClient with the SSLSocketFactory and a custom HostnameVerifier
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0])
                .hostnameVerifier(getHostnameVerifierWithLogging())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // 4. Make your API requests
        Request request = new Request.Builder()
                .url("https://your.rest.api.endpoint") // Change accordingly to your API
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    private static TrustManager[] getTrustManagersWithLogging() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

            return new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            try {
                                originalTrustManager.checkClientTrusted(chain, authType);
                            } catch (CertificateException e) {
                                // Log the exception, but allow the connection to proceed
                                e.printStackTrace(); 
                            }
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            try {
                                originalTrustManager.checkServerTrusted(chain, authType);
                            } catch (CertificateException e) {
                                // Log the exception, but allow the connection to proceed
                                e.printStackTrace(); 
                            }
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return originalTrustManager.getAcceptedIssuers();
                        }
                    }
            };
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Error initializing TrustManager", e);
        }
    }

    private static SSLSocketFactory getSSLSocketFactory(TrustManager[] trustManagers) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Error creating SSLSocketFactory", e);
        }
    }

    private static HostnameVerifier getHostnameVerifierWithLogging() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                boolean isValid = false;
                try {
                    isValid = HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                } catch (Exception e) {
                    // Log the exception
                    e.printStackTrace(); 
                }
                if (!isValid) {
                    // Log hostname verification failure
                    System.err.println("Hostname verification failed for: " + hostname); 
                }
                return isValid;
            }
        };
    }
}