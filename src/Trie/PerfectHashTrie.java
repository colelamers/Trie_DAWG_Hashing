package Trie;

import HashMap.PerfectHashMap;
import java.io.Serializable;
import java.util.List;

public class PerfectHashTrie implements Serializable {
    public final PerfectHashNode root = new PerfectHashNode("");

    public PerfectHashTrie() {}

    public void insert(List<String> words) throws Exception {
        PerfectHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            // todo 1; stuck here. need to somehow get the nodes. right now it's failing.
            // can't really send back a Entry<K, V> object so I'm trying to think of how to do that.
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

        // todo 1; somehow right here, this is growing to larger during rebuild which makes no sense
        // the root tries children has issues with generating hashcodes
        node.children.rebuild();  // Rebuild this node's perfect hash map

        for (String key : node.children.getKeys()) {
            PerfectHashNode child = node.children.get(key);
            if (child != null) {
                dfsRebuild(child);
            }
        }
    }

    public static class PerfectHashNode implements Serializable {
        public PerfectHashMap<String, PerfectHashNode> children;
        public String word;

        public PerfectHashNode(String tWord) {
            word = tWord;
            children = new PerfectHashMap<String, PerfectHashNode>();
        }

        public void insertChild(String key, PerfectHashNode childNode) throws Exception {
            if (children.get(key, false) == null) {
                children.put(key, childNode);
                // we absolutely cannot rebuild here. because some nodes have several
                // hits and are more common than others. thus rebuilding up to say 15 times
                // will cause the int val to go out of bounds.
                //children.rebuild();
            }
        }
    }
}
