package DAWG;

import HashMap.PerfectHashMap;
import java.io.Serializable;
import java.util.*;
public class PerfectHashDAWG<K> implements Serializable {
    public PerfectHashMap<K, PerfectHashNode<K>> root = new PerfectHashMap<K, PerfectHashNode<K>> ();

    public PerfectHashDAWG() {}

    public void insert(List<K> nodes) throws Exception {
        if (nodes.isEmpty()){
            return;
        }

        // Perform gets with false flag because we need to subordinate
        // the nodes before rebuilding the hashmap.
        K nodeAtZero = nodes.get(0);
        PerfectHashNode<K> current = root.get(nodeAtZero, false);
        if (current == null) {
            current = new PerfectHashNode<>(nodeAtZero);
            root.put(nodeAtZero, current);
        }

        for (int i = 1; i < nodes.size(); ++i) {
            K nodeAtI = nodes.get(i);
            if (nodeAtI.equals("other")){
                System.out.print("");
            }
            int childIndex = current.getChildIndex(nodeAtI);
            if (childIndex == -1) {
                current.children.add(nodeAtI);

                if (root.get(nodeAtI, false) == null) {
                    root.put(nodeAtI, new PerfectHashNode<>(nodeAtI));
                }
            }

            // Always move current to the existing node
            current = root.get(nodeAtI, false);
        }
    }

    public static class PerfectHashNode<K> implements Serializable {
        public K node;
        public ArrayList<K> children = new ArrayList<>();

        public PerfectHashNode(K word) {
            this.node = word;
            this.children = new ArrayList<K>();
        }

        public int getChildIndex(K key) {
            return children.indexOf(key);
        }
    }
}