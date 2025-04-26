package Trie;

import java.io.Serializable;
import java.util.*;
import HashMap.CustomHashMap;

public class CustomHashTrie implements Serializable {
    public CustomHashNode root = new CustomHashNode("");

    public CustomHashTrie() {
    }

    public void insert(List<String> words) {
        CustomHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            CustomHashNode child = current.getChild(word);

            if (child == null) {
                current.insertChild(word, new CustomHashNode(word));
            }
            current = current.getChild(word);
        }
    }

    public static class CustomHashNode implements Serializable {
        public CustomHashMap<String, CustomHashNode> children;
        public String word;

        public CustomHashNode(String tWord) {
            word = tWord;
            children = new CustomHashMap<String, CustomHashNode>();
        }

        public void insertChild(String key, CustomHashNode childNode) {
            children.put(key, childNode);
        }

        public CustomHashNode getChild(String key) {
            return children.get(key);
        }
    }
}
