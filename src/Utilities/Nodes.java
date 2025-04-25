package Utilities;

import java.io.Serializable;

public class Nodes<K, V> implements Serializable {
    public static class Node<K, V> implements Serializable {
        public K key;
        public V value;
        public Node<K, V> next = null;

        // Basic KvP node
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        // Next pointer node
        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
