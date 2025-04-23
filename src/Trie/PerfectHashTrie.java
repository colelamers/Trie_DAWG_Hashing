package Trie;

import HashMap.PerfectHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerfectHashTrie {
  /*  public final PerfectHashNode root = new PerfectHashNode("");

    public PerfectHashTrie() {
    }

    public void insert(List<String> words) {
        PerfectHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            PerfectHashNode child = current.getChild(word);

            if (child == null) {
                current.insertChild(word, new PerfectHashNode(word));
            }
            current = current.getChild(word);
        }
    }

    public class PerfectHashNode implements Serializable {
        public PerfectHashMap<String, PerfectHashNode> children;
        public String word;

        public PerfectHashNode(String tWord) {
            word = tWord;
            List<String> childKeys = new ArrayList<>();
            children = new PerfectHashMap<String, PerfectHashNode>(childKeys);
        }

        public void insertChild(String key, PerfectHashNode childNode) {
            children.insert(key, childNode);
        }

        public PerfectHashNode getChild(String key) {
            return children.locate(key);
        }
    }*/
}
