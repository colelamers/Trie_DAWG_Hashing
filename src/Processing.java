import HashMap.*;
import DAWG.*;
import Trie.*;
import Utilities.BinaryFileUtility;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Processing {

    private static final String BINARY_PATH = "./BinaryFiles/";
    private static String WHICH_BOOK = "";

    public Processing(String bookName){
        WHICH_BOOK = bookName + "_";
    }

    public static void printLinedTime(String text, long startTime){
        System.out.print(text);

        int targetColumn = 40;
        int spacesToAdd = targetColumn - text.length();
        for (int i = 0; i < spacesToAdd; i++) {
            System.out.print(' ');
        }

        System.out.printf("%.2fs\n", (System.currentTimeMillis() - startTime) / 1000d);
    }

    public static void printLinedTimeForMB(String text, double mbSize){
        System.out.print(text);

        int targetColumn = 40;
        int spacesToAdd = targetColumn - text.length();
        for (int i = 0; i < spacesToAdd; i++) {
            System.out.print(' ');
        }

        System.out.printf("%.2f MB\n", mbSize);
    }


    public void Build_DefaultTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();

        DefaultHashTrie trie = new DefaultHashTrie();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            trie.insert(Arrays.asList(splitSentence));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            DefaultHashTrie.DefaultHashNode dhn = trie.root;

            for (String word : splitSentence) {
                if (dhn.children.containsKey(word)) {
                    dhn = dhn.children.get(word);
                } else {
                    dhn = null;
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_DefaultTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_CustomTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CustomHashTrie trie = new CustomHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            CustomHashTrie.CustomHashNode dhn = trie.root;

            for (String word : splitSentence) {
                CustomHashTrie.CustomHashNode tNode = dhn.children.get(word);
                if (tNode != null) {
                    dhn = tNode;
                } else {
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_CustomTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public void Build_PerfectTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        PerfectHashTrie trie = new PerfectHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long buildTime = System.currentTimeMillis();
        trie.finalizeTrie();
        printLinedTime("Rebuild Perfect Hash Time: ", buildTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            PerfectHashTrie.PerfectHashNode dhn = trie.root;

            for (String word : splitSentence) {
                PerfectHashTrie.PerfectHashNode tNode = dhn.children.get(word);
                if (tNode != null) {
                    dhn = tNode;
                } else {
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_PerfectTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_CuckooTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooTrie trie = new CuckooTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            CuckooTrie.CuckooNode dhn = trie.root;

            for (String word : splitSentence) {
                if (dhn.children.containsKey(word)) {
                    dhn = dhn.children.get(word);
                } else {
                    dhn = null;
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_CuckooTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_CuckooHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooHashDAWG dawg = new CuckooHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            CuckooHashDAWG.CuckooNode dhn = dawg.root;

            for (String word : splitSentence) {
                if (dhn.children.containsKey(word)) {
                    dhn = dhn.children.get(word);
                } else {
                    dhn = null;
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_CuckooHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_CustomHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();

        CustomHashDAWG dawg = new CustomHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            CustomHashDAWG.CustomHashNode dhn = dawg.root;

            for (String word : splitSentence) {
                CustomHashDAWG.CustomHashNode tNode = dhn.children.get(word);
                if (tNode != null) {
                    dhn = tNode;
                } else {
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_CustomHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_PerfectHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();

        PerfectHashDAWG dawg = new PerfectHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }

        printLinedTime("Assembly Time: ", startTime);

        long buildTime = System.currentTimeMillis();
        dawg.finalizeTrie();
        printLinedTime("Rebuild Perfect Hash Time: ", buildTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            PerfectHashDAWG.PerfectHashNode dhn = dawg.root;

            for (String word : splitSentence) {
                PerfectHashDAWG.PerfectHashNode tNode = dhn.children.get(word);
                if (tNode != null) {
                    dhn = tNode;
                } else {
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);
        String fileName = "Build_PerfectHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }

    public void Build_DefaultHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();

        DefaultHashDAWG dawg = new DefaultHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            DefaultHashDAWG.DefaultHashNode dhn = dawg.root;

            for (String word : splitSentence) {
                if (dhn.children.containsKey(word)) {
                    dhn = dhn.children.get(word);
                } else {
                    dhn = null;
                    break;
                }
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_DefaultHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public void Build_PerfectHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();

        PerfectHashMap<String, String> customMap = new PerfectHashMap<String, String>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printLinedTime("Assembly Time: ", startTime);

        long buildTime = System.currentTimeMillis();
        customMap.rebuild();
        printLinedTime("Rebuild Perfect Hash Time: ", buildTime);

        long traversalTime = System.currentTimeMillis();
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                customMap.get(word);
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_PerfectHash";
        writeBinaryObject(customMap, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public void Build_DefaultHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        HashMap<String, String> customMap = new HashMap<String, String>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                customMap.get(word);
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_DefaultHash";
        writeBinaryObject(customMap, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public void Build_CustomHash(List<String> book) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        CustomHashMap<String, String> customMap = new CustomHashMap<>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                customMap.get(word);
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);

        String fileName = "Build_CustomHash";
        writeBinaryObject(customMap, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public void Build_CuckooHash(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooHashMap<String, String> customMap = new CuckooHashMap<>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printLinedTime("Assembly Time: ", startTime);

        long traversalTime = System.currentTimeMillis();
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) {
                    continue;
                }
                customMap.get(word);
            }
        }
        printLinedTime("Traversal of Entire Data Structure: ", traversalTime);
        String fileName = "Build_CuckooHash";
        writeBinaryObject(customMap, fileName);
        loadBinaryObject(fileName);
        getFileSize(fileName);
    }


    public static <T extends Serializable> void writeBinaryObject(T tObject, String fileName) throws IOException, ClassNotFoundException {
        long assemblyTime = System.currentTimeMillis();
        String path = getBinaryFileName(fileName);
        BinaryFileUtility.WriteToBinaryFile(tObject, path);
        tObject = null;
        printLinedTime("Binary File Write Time: ", assemblyTime);
    }

    public static void getFileSize(String fileName){
        // Get the file size
        String path = getBinaryFileName(fileName);
        File file = new File(path);
        double fileSizeMB = (double) file.length() / (1024 * 1024);
        printLinedTimeForMB("File Size:", fileSizeMB);
    }

    public static <T extends Serializable> void loadBinaryObject(String fileName) throws IOException, ClassNotFoundException {
        long loadFileTime = System.currentTimeMillis();
        T loadedType = BinaryFileUtility.ReadFromBinaryFile(getBinaryFileName(fileName));
        loadedType = null;
        printLinedTime("Binary File Load Time: ", loadFileTime);
    }

    private static String getBinaryFileName(String fileName){
        return BINARY_PATH + WHICH_BOOK + fileName + ".bin";
    }

}