import HashMap.*;
import DAWG.*;
import Trie.*;
import Utilities.BinaryFileUtility;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Processing {

    private static final String BINARY_PATH = "./BinaryFiles/";
    public static void PrintTimeDiff(long startTime){
        System.out.printf("%.2fs\n", (System.currentTimeMillis() - startTime) / 1000d);
    }

    public static void Build_DefaultTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        DefaultHashTrie trie = new DefaultHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/DefaultHashTrie.bin");
        PrintTimeDiff(startTime);
    }

    public static void Build_CustomTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CustomHashTrie trie = new CustomHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/Build_CustomTrie.bin");
        PrintTimeDiff(startTime);
    }

    public static void Build_PerfectTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        PerfectHashTrie trie = new PerfectHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        trie.finalizeTrie();
        WriteAndLoad(trie, "./BinaryFiles/Build_PerfectTrie.bin");
        PrintTimeDiff(startTime);
    }

    public static void Build_CuckooTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooTrie trie = new CuckooTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(trie, "./BinaryFiles/Build_CuckooTrie.bin");
        PrintTimeDiff(startTime);
    }


    public static void Build_CuckooHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooHashDAWG dawg = new CuckooHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(dawg, BINARY_PATH + "Build_CuckooDAWG.bin");
        PrintTimeDiff(startTime);
    }

    public static void Build_CustomHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CustomHashDAWG dawg = new CustomHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(dawg, BINARY_PATH + "Build_CustomDAWG.bin");
        PrintTimeDiff(startTime);
    }

    public static void Build_PerfectHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        PerfectHashDAWG dawg = new PerfectHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        dawg.root.children.rebuild(); // todo 1; i have no idea if thats right
        WriteAndLoad(dawg, BINARY_PATH + "Build_PerfectDAWG.bin");
        PrintTimeDiff(startTime);
    }


    public static void Build_DefaultHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        DefaultHashDAWG dawg = new DefaultHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        WriteAndLoad(dawg, BINARY_PATH + "Build_DefaultDAWG.bin");
        PrintTimeDiff(startTime);
    }

    public static void Test_PerfectHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        PerfectHashMap<String, String> twoStringMap = new PerfectHashMap<String, String>();
        int i = 0;
        for (String line : book){
            for(String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))){
                if (word.isEmpty()) continue;
                ++i;
                twoStringMap.put(word, String.format("%s", word));
            }
        }
        twoStringMap.rebuild();
        WriteAndLoad(twoStringMap, "./BinaryFiles/Test_PerfectHash.bin");
        PrintTimeDiff(startTime);
    }

    public static void Test_DefaultHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        HashMap<String, String> hashStringString = new HashMap<>();
        int i = 0;

        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                hashStringString.put(word, String.format("%s-%d", word, i));
            }
        }

        WriteAndLoad(hashStringString, BINARY_PATH + "Test_DefaultHash.bin");
        PrintTimeDiff(startTime);
    }

    public static void Test_CustomHash(List<String> book) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        CustomHashMap<String, String> customMap = new CustomHashMap<>();
        int i = 0;

        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }

        WriteAndLoad(customMap, BINARY_PATH + "Test_CustomHash.bin");
        PrintTimeDiff(startTime);
    }

    public static void Test_CuckooHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooHashMap<String, String> cuckooMap = new CuckooHashMap<>();
        int i = 0;

        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                cuckooMap.put(word, String.format("%s-%d", word, i));
            }
        }

        WriteAndLoad(cuckooMap, BINARY_PATH + "Test_CuckooHash.bin");
        PrintTimeDiff(startTime);
    }



    public static <T extends Serializable> void WriteAndLoad(T tObject, String path) throws IOException, ClassNotFoundException {
        BinaryFileUtility.WriteToBinaryFile(tObject, path);
        T loadedType = BinaryFileUtility.ReadFromBinaryFile(path);
    }

}