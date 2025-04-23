import java.io.*;
import java.util.List;

public class ObjectSerializer {

    // Generic method to serialize any object and save it to a file
    public static <T> void serializeObject(T object, String fileName) throws IOException {
        // Check if object is not null
        if (object == null) {
            throw new IllegalArgumentException("Object to serialize cannot be null.");
        }

        // Open a FileOutputStream and wrap it in an ObjectOutputStream
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            // Serialize the object to the file
            out.writeObject(object);
            System.out.println("Object serialized and saved to " + fileName);
        } catch (IOException e) {
            throw new IOException("Error during serialization: " + e.getMessage(), e);
        }
    }

    // Generic method to deserialize any object from a file
    public static <T> T deserializeObject(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            // Deserialize the object from the file and return it
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error during deserialization: " + e.getMessage(), e);
        }
    }
}