import android.content.Context;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import android.util.Log;
import java.security.cert.X509Certificate;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

public class SecureHttpClient {

    private static final String TAG = "SecureHttpClient";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_URL = "https://your-auth-server.com/oauth/token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private String accessToken;
    private String refreshToken;

    public SecureHttpClient(Context context, String accessToken, String refreshToken) {
        this.context = context;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public OkHttpClient getSecureClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        X509TrustManager trustManager = createTrustManager();
        SSLContext sslContext = createSSLContext(trustManager);

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .addInterceptor(new TokenInterceptor())
                .authenticator(new TokenAuthenticator())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private static X509TrustManager createTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        return (X509TrustManager) tmf.getTrustManagers()[0];
    }

    private static SSLContext createSSLContext(X509TrustManager trustManager) 
                                        throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        return sslContext;
    }

    private class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            if (accessToken == null || accessToken.isEmpty()) {
                return chain.proceed(originalRequest);
            }

            Request newRequest = originalRequest.newBuilder()
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                    .build();

            Response response = chain.proceed(newRequest);

            if (response.code() == 401) {
                synchronized (SecureHttpClient.class) {
                    String newToken = refreshAccessToken();
                    if (newToken != null) {
                        newRequest = originalRequest.newBuilder()
                                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + newToken)
                                .build();
                        response.close();
                        return chain.proceed(newRequest);
                    }
                }
            }

            return response;
        }
    }

    private class TokenAuthenticator implements Authenticator {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            String newToken = refreshAccessToken();
            if (newToken == null) {
                return null;
            }

            return response.request().newBuilder()
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + newToken)
                    .build();
        }
    }

    private String refreshAccessToken() {
        try {
            OkHttpClient client = new OkHttpClient();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GRANT_TYPE, REFRESH_TOKEN);
            jsonObject.put(REFRESH_TOKEN, refreshToken);

            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    accessToken = jsonResponse.getString("access_token");
                    refreshToken = jsonResponse.getString(REFRESH_TOKEN);
                    saveTokensSecurely(accessToken, refreshToken);
                    return accessToken;
                } else {
                    Log.e(TAG, "Failed to refresh token: " + response.code());
                    return null;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Token refresh failed", e);
            return null;
        }
    }

    private void saveTokensSecurely() {
        // Implement secure token storage using Android Keystore or EncryptedSharedPreferences
    }
}
