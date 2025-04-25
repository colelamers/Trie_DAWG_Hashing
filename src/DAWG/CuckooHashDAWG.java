package DAWG;

import HashMap.CuckooHashMap;

import java.io.Serializable;
import java.util.List;

public class CuckooHashDAWG implements Serializable {
    public final CuckooNode root = new CuckooNode("");

    public CuckooHashDAWG() {}

    public void insert(List<String> words) throws Exception {
        CuckooNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            CuckooNode child = current.getChild(word);

            if (child == null) {
                child = new CuckooNode(word);
                current.insertChild(word, child);
            }

            current = child;
        }
    }

    public static class CuckooNode implements Serializable {
        public String word;
        public CuckooHashMap<String, CuckooNode> children;

        public CuckooNode(String word) {
            this.word = word;
            this.children = new CuckooHashMap<>();
        }

        public void insertChild(String key, CuckooNode child) throws Exception {
            children.put(key, child);
        }

        public CuckooNode getChild(String key) {
            return children.get(key);
        }
    }
}
