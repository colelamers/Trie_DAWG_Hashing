package HashMap;

import java.io.Serializable;
import java.util.*;

public class PerfectHashMap<K, V> implements Serializable {
    public static class Entry<K, V> implements Serializable {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private List<Entry<K, V>> entries;  // Raw key-value pairs
    private List<Entry<K, V>> table;    // Perfect hash table
    private int m;
    private int r;
    private int[] T;
    private boolean isRebuilt = false;  // Flag to ensure rebuild is called before access

    public PerfectHashMap() {
        this.entries = new ArrayList<>();
        this.table = new ArrayList<>();
    }

    public void put(K key, V value) throws Exception {
        entries.add(new Entry<>(key, value));
    }

    public void rebuild() throws Exception {
        if (entries.isEmpty()) {
            throw new Exception("Cannot rebuild empty map");
        }

        List<K> keys = new ArrayList<>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.key);
        }

        r = keys.size();
        m = r * 2;
        T = new int[m];
        Arrays.fill(T, 0);

        // Split into buckets
        List<List<K>> buckets = new ArrayList<>(Collections.nCopies(r, null));
        for (int i = 0; i < r; i++) {
            buckets.set(i, new ArrayList<>());
        }

        for (K key : keys) {
            int index = computeHash(key, r);
            buckets.get(index).add(key);
        }

        // Sort buckets by size
        buckets.sort((a, b) -> b.size() - a.size());

        // Find injective mapping for all buckets
        for (List<K> bucket : buckets) {
            int count = 1;
            while (!findHashForBucket(bucket)){
                // A limitation of how perfect hashes work is that
                // if there is a significantly large n, it will cause
                // the bucket size to overflow. Meaning that you cannot
                // make the hashmap any larger due to memory limitations.
                // max-1 is basically the final check. If it can't fit
                // in that sized bucket, it's way too large.
                m = (m * 2) > 0 ? m * 2 : Integer.MAX_VALUE - 1;
                T = new int[m];
                Arrays.fill(T, 0);
                ++count;
            }
        }

        // Fill the hashtable
        table = new ArrayList<>(Collections.nCopies(m, null));
        for (Entry<K, V> entry : entries) {
            int pos = computeHash(entry.key, m);
            table.set(pos, entry);
        }
    }

    private boolean findHashForBucket(List<K> bucket) {
        int MAX_ATTEMPTS = 100000;
        for (int i = 1; i < MAX_ATTEMPTS; ++i) {
            Set<Integer> positions = new HashSet<>();
            boolean isInjective = true;

            for (K key : bucket) {
                int position = computeSubHash(key, i);
                if (T[position] == 1 || !positions.add(position)) {
                    isInjective = false;
                    break;
                }
            }

            if (isInjective) {
                for (K key : bucket) {
                    int position = computeSubHash(key, i);
                    T[position] = 1;
                }
                return true;
            }
        }
        // Hash Not Found
        return false;
    }


    private int computeHash(K key, int r) {
        return Math.abs(key.hashCode()) % r;
    }

    private int computeSubHash(K key, int l) {
        return Math.abs(key.hashCode() + l) % m;
    }

    public V locate(K key) {
        if (!isRebuilt) {
            throw new IllegalStateException("The map is not rebuilt. Call rebuild() first.");
        }

        if (table.isEmpty()) return null;
        int pos = computeHash(key, m);
        Entry<K, V> entry = table.get(pos);
        if (entry != null && entry.key.equals(key)) {
            return entry.value;
        }
        return null;
    }

    public void delete(K key) {
        if (!isRebuilt) {
            throw new IllegalStateException("The map is not rebuilt. Call rebuild() first.");
        }

        int pos = computeHash(key, m);
        Entry<K, V> entry = table.get(pos);
        if (entry != null && entry.key.equals(key)) {
            // Mark the position as empty by setting it to null
            table.set(pos, null);
            // Optionally, remove the entry from the `entries` list
            entries.removeIf(e -> e.key.equals(key));
        }
    }
}
