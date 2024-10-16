import java.io.*;
import java.sql.*;
import java.util.LinkedList;

public class SerializeToString {

    public static void main(String[] args) {
        try {
            // Example object to serialize
            LinkedList<Diff_match_patch.Patch> patches = new LinkedList<>();
            // Add some patches to the list (example)
            patches.add(new Diff_match_patch.Patch());

            // Serialize object to string
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(patches);
            objectStream.close();
            String serializedObject = byteStream.toString("ISO-8859-1");

            // Retrieve credentials from environment variables
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            // Validate environment variables
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new IllegalStateException("Database credentials not set in environment variables.");
            }

            // Store serialized object in database
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            String insertSQL = "INSERT INTO your_table (serialized_object) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, serializedObject);
            preparedStatement.executeUpdate();

            // Retrieve serialized object from database
            String selectSQL = "SELECT serialized_object FROM your_table WHERE id = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, 1); 
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String retrievedObject = resultSet.getString("serialized_object");

                // Deserialize object from string
                byteStream = new ByteArrayOutputStream();
                byteStream.write(retrievedObject.getBytes("ISO-8859-1"));
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
                LinkedList<Diff_match_patch.Patch> deserializedPatches = (LinkedList<Diff_match_patch.Patch>) objectInputStream.readObject();
                objectInputStream.close();

                // Use the deserialized object
                System.out.println("Deserialized patches: " + deserializedPatches);
            }

            connection.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
