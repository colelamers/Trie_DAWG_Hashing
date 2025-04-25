package DAWG;

import java.io.Serializable;
import java.util.List;
import HashMap.CustomHashMap;

public class CustomHashDAWG implements Serializable {
    public final CustomHashNode root = new CustomHashNode("");

    public CustomHashDAWG() {}

    public void insert(List<String> words) {
        CustomHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            CustomHashNode child = current.getChild(word);

            if (child == null) {
                child = new CustomHashNode(word);
                current.insertChild(word, child);
            }

            current = child;
        }
    }

    public class CustomHashNode implements Serializable {
        public String word;
        public CustomHashMap<String, CustomHashNode> children;

        public CustomHashNode(String word) {
            this.word = word;
            this.children = new CustomHashMap<>();
        }

        public void insertChild(String key, CustomHashNode child) {
            children.put(key, child);
        }

        public CustomHashNode getChild(String key) {
            return children.get(key);
        }
    }
}
