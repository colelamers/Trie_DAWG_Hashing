package Trie;

import java.io.Serializable;
import java.util.*;

public class DefaultHashTrie implements Serializable {
    public final DefaultHashNode root = new DefaultHashNode("");

    public DefaultHashTrie() {
    }

    public void insert(List<String> words) {
        DefaultHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            DefaultHashNode child = current.getChild(word);

            if (child == null) {
                current.insertChild(word, new DefaultHashNode(word));
            }
            current = current.getChild(word);
        }
    }

    public class DefaultHashNode implements Serializable {
        public Map<String, DefaultHashNode> children;
        public String word;

        public DefaultHashNode(String tWord) {
            word = tWord;
            children = new HashMap<>();
        }

        public void insertChild(String key, DefaultHashNode childNode) {
            children.put(key, childNode);
        }

        public DefaultHashNode getChild(String key) {
            return children.get(key);
        }
    }
}

