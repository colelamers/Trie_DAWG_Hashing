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
    public static void printTimeInSeconds(long startTime){
        System.out.printf("%.2fs\n", (System.currentTimeMillis() - startTime) / 1000d);
    }

    public static void Build_DefaultTrie(List<String> book) throws Exception {
        System.out.print("Build_DefaultTrie:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        DefaultHashTrie trie = new DefaultHashTrie();
        for (String sentence : book) {
            var splitSentence = sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+");
            trie.insert(Arrays.asList(splitSentence));
        }
        printTimeInSeconds(startTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);
        String fileName = "Build_DefaultTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_CustomTrie(List<String> book) throws Exception {
        System.out.print("Build_CustomTrie:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        CustomHashTrie trie = new CustomHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printTimeInSeconds(startTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);

        String fileName = "Build_CustomTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
    }


    public static void Build_PerfectTrie(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        PerfectHashTrie trie = new PerfectHashTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        trie.finalizeTrie();
        String fileName = "Build_PerfectTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_CuckooTrie(List<String> book) throws Exception {
        System.out.print("Build_CuckooTrie:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        CuckooTrie trie = new CuckooTrie();
        for (String sentence : book) {
            trie.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printTimeInSeconds(startTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);

        String fileName = "Build_CuckooTrie";
        writeBinaryObject(trie, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_CuckooHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        CuckooHashDAWG dawg = new CuckooHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printTimeInSeconds(startTime);
        String fileName = "Build_CuckooHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_CustomHashDAWG(List<String> book) throws Exception {
        System.out.print("Build_CustomHashDAWG:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        CustomHashDAWG dawg = new CustomHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printTimeInSeconds(startTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);

        String fileName = "Build_CustomHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_PerfectHashDAWG(List<String> book) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        PerfectHashDAWG dawg = new PerfectHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }

        printTimeInSeconds(startTime);

        long buildTime = System.currentTimeMillis();
        System.out.print("Build Perfect Map Time: ");
        dawg.finalizeTrie();
        printTimeInSeconds(buildTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);
        String fileName = "Build_PerfectHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
    }

    public static void Build_DefaultHashDAWG(List<String> book) throws Exception {
        System.out.print("Build_DefaultHashDAWG:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        DefaultHashDAWG dawg = new DefaultHashDAWG();
        for (String sentence : book) {
            dawg.insert(Arrays.asList(sentence.split("[•—'\",”/|_=+@#<>{}.!?\\s]+")));
        }
        printTimeInSeconds(startTime);

        long traversalTime = System.currentTimeMillis();
        System.out.print("Traversal of Entire Data Structure: ");
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
        printTimeInSeconds(traversalTime);

        String fileName = "Build_DefaultHashDAWG";
        writeBinaryObject(dawg, fileName);
        loadBinaryObject(fileName);
    }


    public static void Build_PerfectHash(List<String> book) throws Exception {
        System.out.print("Build_PerfectHash:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        PerfectHashMap<String, String> twoStringMap = new PerfectHashMap<String, String>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                twoStringMap.put(word, word);
            }
        }
        printTimeInSeconds(startTime);

        long buildTime = System.currentTimeMillis();
        System.out.print("Build Perfect Map Time: ");
        twoStringMap.rebuild();
        printTimeInSeconds(buildTime);

        String fileName = "Build_PerfectHash";
        writeBinaryObject(twoStringMap, fileName);
        loadBinaryObject(fileName);
    }


    public static void Build_DefaultHash(List<String> book) throws Exception {
        System.out.print("Build_DefaultHash:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        HashMap<String, String> hashStringString = new HashMap<String, String>();
        int i = 0;

        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                hashStringString.put(word, String.format("%s-%d", word, i));
            }
        }

        printTimeInSeconds(startTime);
        String fileName = "Build_DefaultHash";
        writeBinaryObject(hashStringString, fileName);
        loadBinaryObject(fileName);
    }


    public static void Build_CustomHash(List<String> book) throws IOException, ClassNotFoundException {
        System.out.print("Build_CustomHash:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");
        CustomHashMap<String, String> customMap = new CustomHashMap<>();
        int i = 0;

        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                customMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printTimeInSeconds(startTime);
        String fileName = "Build_CustomHash";
        writeBinaryObject(customMap, fileName);
        loadBinaryObject(fileName);
    }


    public static void Build_CuckooHash(List<String> book) throws Exception {
        System.out.print("Build_CuckooHash:\n");
        long startTime = System.currentTimeMillis();
        System.out.print("Assembly Time: ");

        CuckooHashMap<String, String> cuckooMap = new CuckooHashMap<>();
        int i = 0;
        for (String line : book) {
            for (String word : Arrays.asList(line.split("[•—'\",”/|_=+@#<>{}.!?\\s]+"))) {
                if (word.isEmpty()) continue;
                ++i;
                cuckooMap.put(word, String.format("%s-%d", word, i));
            }
        }
        printTimeInSeconds(startTime);
        String fileName = "Build_CuckooHash";
        writeBinaryObject(cuckooMap, fileName);
        loadBinaryObject(fileName);
    }


    public static <T extends Serializable> void writeBinaryObject(T tObject, String fileName) throws IOException, ClassNotFoundException {
        long assemblyTime = System.currentTimeMillis();
        System.out.print("Binary File Write Time: ");
        String path = BINARY_PATH + fileName + ".bin";
        BinaryFileUtility.WriteToBinaryFile(tObject, path);
        tObject = null;
        printTimeInSeconds(assemblyTime);
    }

    public static <T extends Serializable> void loadBinaryObject(String fileName) throws IOException, ClassNotFoundException {
        long loadFileTime = System.currentTimeMillis();
        System.out.print("Binary File Load Time: ");
        T loadedType = BinaryFileUtility.ReadFromBinaryFile(BINARY_PATH + fileName + ".bin");
        loadedType = null;
        printTimeInSeconds(loadFileTime);
    }

}