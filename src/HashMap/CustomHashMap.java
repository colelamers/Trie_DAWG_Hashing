package HashMap;

import java.io.Serializable;
import java.util.Objects;

public class CustomHashMap<K, V> implements Serializable {

    // Used for template and understanding purposes of the
    // HashMap data structure
    public static class Node<K, V> implements Serializable {
        K key;
        V value;
        Node<K, V> next; // Basically simple linked list to hash collision nodes

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // Base "array" size
    private static final int INITIAL_CAPACITY = 16; // Start Size of array
    private static final float LOAD_FACTOR = 0.75f; // Max size of array before resize occurs
    public Node<K, V>[] buckets;
    private int size;

    public CustomHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private int hash(K key) {
        // NOTE: As the bucket size grows, that implies that less
        // hash collisions will occur over time because the modulus
        // is substantially larger as resizes occur.
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> head = buckets[index];

        // Check if key exists, update if found
        Node<K, V> curr = head;
        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                // This is just to update the value in case the key
                // already DOES exist.
                curr.value = value;
                return;
            }
            curr = curr.next;
        }

        // Prepend node, basically insert an index 0 and push head down
        buckets[index] = new Node<>(key, value, head);
        ++size;

        if ((1.0 * size) / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    private void putWithoutResize(K key, V value) {
        int index = hash(key);
        Node<K, V> head = buckets[index];

        Node<K, V> curr = head;
        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                curr.value = value;
                return;
            }
            curr = curr.next;
        }

        buckets[index] = new Node<>(key, value, head);
        ++size; // size is fine here because we reset it before resize()
    }

    public V get(K key) {
        int index = hash(key);
        Node<K, V> curr = buckets[index];
        // Basically iterate over bucket until you find the matching key in
        // of bucket with several collisions
        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                return curr.value;
            }
            curr = curr.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        Node<K, V> curr = buckets[index];
        Node<K, V> prev = null;

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                if (prev != null) {
                    prev.next = curr.next;
                } else {
                    buckets[index] = curr.next;
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }

        return null;
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        size = 0;

        // Iterate over oldBuckets,
        // rehashing and inserting elements into new buckets
        for (Node<K, V> head : oldBuckets) {
            // Ensure head is not null
            if (head != null) {
                Node<K, V> current = head;
                while (current != null) {
                    // save before modifying
                    Node<K, V> next = current.next;
                    // todo 2; probably can refactor this somehow
                    // to avoid having basically the exact same function
                    putWithoutResize(current.key, current.value);
                    current = next;
                }
            }
        }
    }

    public int size() {
        return size;
    }
}
