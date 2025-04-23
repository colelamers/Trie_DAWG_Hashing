import DAWG.*;
import HashMap.CuckooHashMap;
import HashMap.CustomHashMap;
import HashMap.PerfectHashMap;
import Trie.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;// Binary Serialized File Location

public class Processing {
    private static final String BINARY_PATH = "./BinaryFiles/";

    // Files that are read in as training data
    private static final String REGEX_SPLIT = "\"[\\•\\s\\[\\]—\'\",”/|\\-_=+@#<>{}.!?]\"";
    public static void Build_DefaultTrie(List<String> book) throws IOException, ClassNotFoundException {
        DefaultHashTrie trie = new DefaultHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/DefaultHashTrie.bin");
    }

    public static void Build_CustomTrie(List<String> book) throws IOException, ClassNotFoundException {
        CustomHashTrie trie = new CustomHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/CustomHashTrie.bin");
    }

    public static void Build_PerfectTrie(List<String> book) throws IOException, ClassNotFoundException {
        /*
        PerfectHashTrie trie = new PerfectHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/PerfectHashTrie.bin");
        */
    }

    public static void Build_CuckooTrie(List<String> book) throws IOException, ClassNotFoundException {
        // todo 1;
    }

    public static void Test_PerfectHash() throws Exception {
        PerfectHashMap<Integer, String> intStringMap = new PerfectHashMap<Integer, String>();
        for (int i = 1; i < 125000; ++i){
            try{
                intStringMap.put(i, String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }
        //intStringMap.rebuild();

        PerfectHashMap<String, String> twoStringMap = new PerfectHashMap<String, String>();
        for (int i = 125000; i < 250000; ++i){
            try{
                twoStringMap.put(String.format("key-%d", i), String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }
        twoStringMap.rebuild();
        WriteAndLoad(twoStringMap, "./BinaryFiles/PerfectHashMap.bin");
    }

    public static void Test_DefaultHash() throws Exception {
        HashMap<Integer, String> hashIntString = new HashMap<Integer, String>();
        for (int i = 1; i < 125000; ++i){
            try{
                hashIntString.put(i, String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }

        HashMap<String, String> hashStringString = new HashMap<String, String>();
        for (int i = 125000; i < 250000; ++i){
            try{
                hashStringString.put(String.format("key-%d", i), String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }
        WriteAndLoad(hashStringString, "./BinaryFiles/HashMap.bin");
    }

    public static void Test_CustomHash() throws IOException, ClassNotFoundException {
        CustomHashMap<Integer, String> intStringMap = new CustomHashMap<Integer, String>();
        for (int i = 1; i < 125000; ++i){
            try{
                intStringMap.put(i, String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }

        CustomHashMap<String, String> twoStringMap = new CustomHashMap<String, String>();
        for (int i = 125000; i < 250000; ++i){
            try{
                twoStringMap.put(String.format("key-%d", i), String.format("val-%d", i));
            }
            catch (Exception e){

            }
        }
        int x = twoStringMap.size();
        WriteAndLoad(twoStringMap, "./BinaryFiles/CustomHashMap.bin");
    }

    public static void Test_CuckooHash(){
        // todo 1;
    }

    public static <T extends Serializable> void WriteAndLoad(T tObject, String path) throws IOException, ClassNotFoundException {
        BinaryFileUtility.WriteToBinaryFile(tObject, path);
        T loadedType = BinaryFileUtility.ReadFromBinaryFile(path);
    }

}