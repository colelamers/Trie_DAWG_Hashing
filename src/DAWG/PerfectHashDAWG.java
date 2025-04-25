package DAWG;

import HashMap.PerfectHashMap;

import java.io.Serializable;
import java.util.*;

public class PerfectHashDAWG implements Serializable {
    public final PerfectHashNode root = new PerfectHashNode("");

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

    public void finalizeTrie() throws Exception {
        dfsRebuild(root);
    }

    private void dfsRebuild(PerfectHashNode node) throws Exception {
        if (node.children == null) {
            return;
        }

        node.children.rebuild();
        for (String key : node.children.getKeys()) {
            PerfectHashNode child = node.children.get(key);
            if (child != null) {
                dfsRebuild(child);
            }
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
