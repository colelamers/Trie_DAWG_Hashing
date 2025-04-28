package HashMap;

import Utilities.Hashing.HashFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Utilities.Nodes.Node;

public class CuckooHashMap<K, V> implements Serializable {
    private static final int INITIAL_SIZE = 16;
    private static final int MAX_EVICTIONS = 32;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int AGGRESSIVE_REHASH_THRESHOLD = 10;

    private int tableSize;
    private int numElements = 0;
    private int evictionAttempts = 0;

    private Node<K, V>[] table1;
    private Node<K, V>[] table2;

    private List<HashFunction<K>> hashFunctions;

    public CuckooHashMap() {
        this.tableSize = INITIAL_SIZE;
        this.table1 = new Node[tableSize];
        this.table2 = new Node[tableSize];
        this.hashFunctions = generateUniversalHashes(2); // Two hash functions
    }

    private List<HashFunction<K>> generateUniversalHashes(int count) {
        Random rand = new Random();
        List<HashFunction<K>> functions = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            int a = rand.nextInt(Integer.MAX_VALUE - 1) + 1;
            int b = rand.nextInt(Integer.MAX_VALUE);
            functions.add(new HashFunction(a, b));
        }

        return functions;
    }

    public boolean put(K key, V value) {
        boolean shouldRehash = ((float)(numElements + 1) / (2 * tableSize) > LOAD_FACTOR)
                || evictionAttempts > AGGRESSIVE_REHASH_THRESHOLD;

        if (shouldRehash) {
            rehash();
        }

        return putInternal(key, value, 0);
    }

    private boolean putInternal(K key, V value, int attempt) {
        if (attempt >= MAX_EVICTIONS) {
            ++evictionAttempts;
            rehash();
            return put(key, value);
        }

        int pos1 = Math.abs(hashFunctions.get(0).apply(key, tableSize)) % tableSize;
        if (table1[pos1] == null) {
            table1[pos1] = new Node<>(key, value);
            ++numElements;
            return true;
        }
        if (table1[pos1].key.equals(key)) {
            table1[pos1].value = value;
            return true;
        }

        int pos2 = Math.abs(hashFunctions.get(1).apply(key, tableSize)) % tableSize;
        if (table2[pos2] == null) {
            table2[pos2] = new Node<>(key, value);
            ++numElements;
            return true;
        }
        if (table2[pos2].key.equals(key)) {
            table2[pos2].value = value;
            return true;
        }

        Node<K, V> evicted = table1[pos1];
        table1[pos1] = new Node<>(key, value);
        return putInternal(evicted.key, evicted.value, attempt + 1);
    }


    private void rehash() {
        tableSize *= 2;
        Node<K, V>[] oldTable1 = table1;
        Node<K, V>[] oldTable2 = table2;

        table1 = new Node[tableSize];
        table2 = new Node[tableSize];
        hashFunctions = generateUniversalHashes(2);
        numElements = 0;
        evictionAttempts = 0;

        for (Node<K, V> Node : oldTable1) {
            if (Node != null) {
                put(Node.key, Node.value);
            }
        }
        for (Node<K, V> Node : oldTable2) {
            if (Node != null) {
                put(Node.key, Node.value);
            }
        }
    }

    public V get(K key) {
        int pos1 = Math.abs(hashFunctions.get(0).apply(key, tableSize)) % tableSize;  // Ensure non-negative index
        if (table1[pos1] != null && table1[pos1].key.equals(key)) {
            return table1[pos1].value;
        }
        int pos2 = Math.abs(hashFunctions.get(1).apply(key, tableSize)) % tableSize;  // Ensure non-negative index
        if (table2[pos2] != null && table2[pos2].key.equals(key)) {
            return table2[pos2].value;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return numElements;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Node<K, V> Node : table1) {
            if (Node != null) keys.add(Node.key);
        }
        for (Node<K, V> Node : table2) {
            if (Node != null) keys.add(Node.key);
        }
        return keys;
    }

    public boolean delete(K key) {
        int pos1 = hashFunctions.get(0).apply(key, tableSize);
        if (table1[pos1] != null && table1[pos1].key.equals(key)) {
            table1[pos1] = null;
            numElements--;
            return true;
        }

        int pos2 = hashFunctions.get(1).apply(key, tableSize);
        if (table2[pos2] != null && table2[pos2].key.equals(key)) {
            table2[pos2] = null;
            numElements--;
            return true;
        }
        return false;
    }
}
