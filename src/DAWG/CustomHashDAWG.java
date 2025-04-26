package DAWG;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import HashMap.CustomHashMap;

public class CustomHashDAWG<K> implements Serializable {
    public CustomHashMap<K, CustomHashNode<K>> root = new CustomHashMap<>();

    public CustomHashDAWG() {}

    public void insert(List<K> nodes) {
        if (nodes.isEmpty()){
            return;
        }

        K nodeAtZero = nodes.get(0);
        CustomHashNode<K> current = root.get(nodeAtZero);
        if (current == null) {
            current = new CustomHashNode<>(nodeAtZero);
            root.put(nodeAtZero, current);
        }

        for (int i = 1; i < nodes.size(); ++i) {
            K nodeAtI = nodes.get(i);
            int childIndex = current.getChildIndex(nodeAtI);
            if (childIndex == -1) {
                current.children.add(nodeAtI);

                if (root.get(nodeAtI) == null) {
                    root.put(nodeAtI, new CustomHashNode<>(nodeAtI));
                }
            }

            // Always move current to the existing node
            current = root.get(nodeAtI);
        }
    }

    public static class CustomHashNode<K> implements Serializable {
        public K node;
        public ArrayList<K> children = new ArrayList<>();

        public CustomHashNode(K word) {
            this.node = word;
            this.children = new ArrayList<K>();
        }

        public int getChildIndex(K key) {
            return children.indexOf(key);
        }
    }
}
