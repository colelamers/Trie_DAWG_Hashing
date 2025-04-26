package Utilities;

import java.io.*;

public class BinaryFileUtility {

    public static <T extends Serializable> void WriteToBinaryFile(T objectToWrite, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(objectToWrite);
        }
    }

    public static <T> T ReadFromBinaryFile(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (T) in.readObject();
        } /*catch (IOException e){
            return e;
        }*/
    }
}
