import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.abc.com");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Set the hostname verifier to disable hostname verification
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        conn.setHostnameVerifier(hv);

        conn.setRequestMethod("GET");

        conn.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        // wr.writeBytes(params);
        wr.flush();
        wr.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}