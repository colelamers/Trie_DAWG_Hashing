package DAWG;

import java.io.Serializable;
import java.util.*;

public class DefaultHashDAWG implements Serializable {
    public final DefaultHashNode root = new DefaultHashNode("");

    public DefaultHashDAWG() {
    }

    public void insert(List<String> words) {
        DefaultHashNode current = root;

        for (String word : words) {
            if (word.isEmpty()) continue;

            DefaultHashNode child = current.getChild(word);

            if (child == null) {
                child = new DefaultHashNode(word);
                current.insertChild(word, child);
            }

            current = child;
        }
    }

    public class DefaultHashNode implements Serializable {
        public String word;
        public Map<String, DefaultHashNode> children;

        public DefaultHashNode(String word) {
            this.word = word;
            this.children = new HashMap<>();
        }

        public void insertChild(String key, DefaultHashNode child) {
            children.put(key, child);
        }

        public DefaultHashNode getChild(String key) {
            return children.get(key);
        }
    }
}
