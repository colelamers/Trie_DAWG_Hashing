package HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class CuckooHashMap<K, V> {
    private static final int INITIAL_SIZE = 16;
    private static final int MAX_EVICTIONS = 32;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int AGGRESSIVE_REHASH_THRESHOLD = 10;  // Threshold for aggressive rehashing

    private int tableSize;
    private int numElements = 0;
    private int evictionAttempts = 0; // Track eviction attempts to adjust rehash behavior

    private Entry<K, V>[] table1;
    private Entry<K, V>[] table2;

    // Only use 2 hash functions for the 2 tables
    private List<Function<K, Integer>> hashFunctions;

    private static class Entry<K, V> {
        K key;
        V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public CuckooHashMap() {
        this.tableSize = INITIAL_SIZE;
        this.table1 = new Entry[tableSize];
        this.table2 = new Entry[tableSize];
        this.hashFunctions = generateUniversalHashes(2); // Just 2 for two-table cuckoo
    }

    private List<Function<K, Integer>> generateUniversalHashes(int count) {
        Random rand = new Random();
        List<Function<K, Integer>> functions = new ArrayList<>();
        int p = Integer.MAX_VALUE;

        for (int i = 0; i < count; i++) {
            int a = rand.nextInt(p - 1) + 1;
            int b = rand.nextInt(p);
            functions.add(key -> {
                int x = key.hashCode();
                long hash = ((long) a * x + b) % p;
                return (int) (hash % tableSize);
            });
        }

        return functions;
    }

    public boolean insert(K key, V value) {
        if ((float)(numElements + 1) / (2 * tableSize) > LOAD_FACTOR || evictionAttempts > AGGRESSIVE_REHASH_THRESHOLD) {
            rehash();
        }

        return insertInternal(key, value, 0);
    }

    private boolean insertInternal(K key, V value, int attempt) {
        if (attempt >= MAX_EVICTIONS) {
            evictionAttempts++;
            rehash();
            return insert(key, value); // Re-attempt insertion after rehashing
        }

        int pos1 = hashFunctions.get(0).apply(key);
        if (table1[pos1] == null) {
            table1[pos1] = new Entry<>(key, value);
            numElements++;
            return true;
        }
        if (table1[pos1].key.equals(key)) {
            table1[pos1].value = value;
            return true;
        }

        int pos2 = hashFunctions.get(1).apply(key);
        if (table2[pos2] == null) {
            table2[pos2] = new Entry<>(key, value);
            numElements++;
            return true;
        }
        if (table2[pos2].key.equals(key)) {
            table2[pos2].value = value;
            return true;
        }

        // Evict from table1
        Entry<K, V> evicted = table1[pos1];
        table1[pos1] = new Entry<>(key, value);
        return insertInternal(evicted.key, evicted.value, attempt + 1);
    }

    private void rehash() {
        tableSize *= 2;
        Entry<K, V>[] oldTable1 = table1;
        Entry<K, V>[] oldTable2 = table2;

        table1 = new Entry[tableSize];
        table2 = new Entry[tableSize];
        hashFunctions = generateUniversalHashes(2);
        numElements = 0;
        evictionAttempts = 0;  // Reset after rehash

        for (Entry<K, V> entry : oldTable1) {
            if (entry != null) insert(entry.key, entry.value);
        }
        for (Entry<K, V> entry : oldTable2) {
            if (entry != null) insert(entry.key, entry.value);
        }
    }

    public V search(K key) {
        int pos1 = hashFunctions.get(0).apply(key);
        if (table1[pos1] != null && table1[pos1].key.equals(key)) {
            return table1[pos1].value;
        }

        int pos2 = hashFunctions.get(1).apply(key);
        if (table2[pos2] != null && table2[pos2].key.equals(key)) {
            return table2[pos2].value;
        }

        return null;
    }

    public boolean containsKey(K key) {
        return search(key) != null;
    }

    public int size() {
        return numElements;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Entry<K, V> entry : table1) {
            if (entry != null) keys.add(entry.key);
        }
        for (Entry<K, V> entry : table2) {
            if (entry != null) keys.add(entry.key);
        }
        return keys;
    }

    public boolean delete(K key) {
        // First, find the position in both tables
        int pos1 = hashFunctions.get(0).apply(key);
        if (table1[pos1] != null && table1[pos1].key.equals(key)) {
            table1[pos1] = null;
            numElements--;
            evictAfterDeletion(pos1, 0);  // Check for potential evictions in table 1
            return true;
        }

        int pos2 = hashFunctions.get(1).apply(key);
        if (table2[pos2] != null && table2[pos2].key.equals(key)) {
            table2[pos2] = null;
            numElements--;
            evictAfterDeletion(pos2, 1);  // Check for potential evictions in table 2
            return true;
        }

        return false;  // Key not found
    }

    private void evictAfterDeletion(int pos, int tableNumber) {
        // Attempt to find the evicted elements in the opposite table
        if (tableNumber == 0) {  // Deletion was in table 1
            // Check if any element in table 2 hashes to the current position in table 1
            int pos2 = hashFunctions.get(1).apply(table1[pos].key);  // Get where the element should go in table 2
            if (table2[pos2] != null) {  // If there is an element in table 2
                // Evict and try to reinsert the element in table 1
                Entry<K, V> evicted = table2[pos2];
                table2[pos2] = table1[pos];
                table1[pos] = evicted;  // Reinsert evicted element into table 1
            }
        } else {  // Deletion was in table 2
            // Check if any element in table 1 hashes to the current position in table 2
            int pos1 = hashFunctions.get(0).apply(table2[pos].key);  // Get where the element should go in table 1
            if (table1[pos1] != null) {  // If there is an element in table 1
                // Evict and try to reinsert the element in table 2
                Entry<K, V> evicted = table1[pos1];
                table1[pos1] = table2[pos];
                table2[pos] = evicted;  // Reinsert evicted element into table 2
            }
        }
    }

}
