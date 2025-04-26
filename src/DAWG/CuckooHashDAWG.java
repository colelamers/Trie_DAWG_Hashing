package DAWG;

import HashMap.CuckooHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuckooHashDAWG<K> implements Serializable {
    public CuckooHashMap<K, CuckooHashNode<K>> root = new CuckooHashMap<>();

    public CuckooHashDAWG() {}

    public void insert(List<K> nodes) {
        if (nodes.isEmpty()){
            return;
        }

        K nodeAtZero = nodes.get(0);
        CuckooHashNode<K> current = root.get(nodeAtZero);
        if (current == null) {
            current = new CuckooHashNode<>(nodeAtZero);
            root.put(nodeAtZero, current);
        }

        for (int i = 1; i < nodes.size(); ++i) {
            K nodeAtI = nodes.get(i);
            int childIndex = current.getChildIndex(nodeAtI);
            if (childIndex == -1) {
                current.children.add(nodeAtI);

                if (root.get(nodeAtI) == null) {
                    root.put(nodeAtI, new CuckooHashNode<>(nodeAtI));
                }
            }

            // Always move current to the existing node
            current = root.get(nodeAtI);
        }
    }

    public static class CuckooHashNode<K> implements Serializable {
        public K node;
        public ArrayList<K> children = new ArrayList<>();

        public CuckooHashNode(K word) {
            this.node = word;
            this.children = new ArrayList<K>();
        }

        public int getChildIndex(K key) {
            return children.indexOf(key);
        }
    }
}