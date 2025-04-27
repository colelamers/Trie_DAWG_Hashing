package DAWG;

import java.io.Serializable;
import java.util.*;

public class DefaultHashDAWG<K> implements Serializable {
    public HashMap<K, DefaultHashNode<K>> root = new HashMap<>();

    public DefaultHashDAWG() {}

    public void insert(List<K> nodes) {
        if (nodes.isEmpty()) {
            return;
        }

        K nodeAtZero = nodes.get(0);
        DefaultHashNode<K> current = root.get(nodeAtZero);
        if (current == null) {
            current = new DefaultHashNode<>(nodeAtZero);
            root.put(nodeAtZero, current);
        }

        for (int i = 1; i < nodes.size(); ++i) {
            K nodeAtI = nodes.get(i);
            int childIndex = current.getChildIndex(nodeAtI);

            if (childIndex == -1) {
                current.children.add(nodeAtI);

                if (!root.containsKey(nodeAtI)) {
                    root.put(nodeAtI, new DefaultHashNode<>(nodeAtI));
                }
            }

            // Always move to the child node
            current = root.get(nodeAtI);
        }
    }

    public static class DefaultHashNode<K> implements Serializable {
        public K node;
        public ArrayList<K> children = new ArrayList<>();

        public DefaultHashNode(K node) {
            this.node = node;
        }

        public int getChildIndex(K key) {
            return children.indexOf(key);
        }
    }
}
