package DAWG;

import HashMap.PerfectHashMap;
import java.io.Serializable;
import java.util.*;

public class PerfectHashDAWG implements Serializable {
    public final PerfectHashNode root = new PerfectHashNode("");

    // Used to identify and reuse identical subtrees (if minimization is needed)
    private final Map<PerfectHashNode, PerfectHashNode> registry = new HashMap<>();

    public void insert(List<String> words) throws Exception {
        PerfectHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            PerfectHashNode child = current.children.get(word, false);
            if (child == null) {
                child = new PerfectHashNode(word);
                current.insertChild(word, child);
            }

            current = child;
        }
    }

    public static class PerfectHashNode implements Serializable {
        public PerfectHashMap<String, PerfectHashNode> children = new PerfectHashMap<String, PerfectHashNode>();
        public String word;

        public PerfectHashNode(String word) {
            this.word = word;
        }

        public void insertChild(String key, PerfectHashNode child) throws Exception {
            if (children.get(key, false) == null) {
                children.put(key, child);
            }
        }
    }
}
