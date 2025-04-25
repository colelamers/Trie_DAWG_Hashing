package Utilities;

import java.io.*;

public class BinaryFileUtility {

    /**
     * Writes the given object to a binary file.
     *
     * @param filePath      The path to the file to write.
     * @param objectToWrite The object instance to serialize.
     * @param <T>           The type of object.
     * @throws IOException If writing fails.
     */
    public static <T extends Serializable> void WriteToBinaryFile(T objectToWrite, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(objectToWrite);
        }
    }

    /**
     * Reads a serialized object from a binary file.
     *
     * @param filePath The path to the file to read from.
     * @param <T>      The expected return type.
     * @return The deserialized object.
     * @throws IOException            If reading fails.
     * @throws ClassNotFoundException If the class is not found during deserialization.
     */
    @SuppressWarnings("unchecked")
    public static <T> T ReadFromBinaryFile(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (T) in.readObject();
        } /*catch (IOException e){
            return e;
        }*/
    }
}
