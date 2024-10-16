import java.net.*;
import java.io.*;
import org.apache.commons.codec.binary.Base64;

public class GetPublicKey {
  public static void main(String[] args) throws Exception{
    // Replace with the URL of the website you want to get the public key for
    URL url = new URL("https://example.com"); 
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // Add a header to indicate that we are only interested in getting the public key and not the whole certificate
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
    connection.setRequestMethod("GET");
    connection.connect();

    int status = connection.getResponseCode();
    if (status == 200) {
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder content = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();

      // The public key is usually contained within the "-----BEGIN PUBLIC KEY-----" and "-----END PUBLIC KEY-----" markers, so we can extract it from the response
      String publicKey = content.toString().substring(
        content.toString().indexOf("-----BEGIN PUBLIC KEY-----") + 27, 
        content.toString().lastIndexOf("-----END PUBLIC KEY-----"));

      // We can use Base64 to decode the public key and convert it into a format that Java can handle
      byte[] decodedPublicKey = Base64.decodeBase64(publicKey);

      // Now we can save the public key in a file or use it for further processing
      FileOutputStream fos = new FileOutputStream("public_key.pem");
      fos.write(decodedPublicKey);
      fos.close();
    } else {
      System.out.println("Error: " + status);
    }
  }
}